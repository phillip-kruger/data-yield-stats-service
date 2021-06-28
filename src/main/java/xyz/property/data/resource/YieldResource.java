package xyz.property.data.resource;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import xyz.property.cache.CacheKey;
import xyz.property.cache.Cached;
import xyz.property.data.formatters.postcode.PostCodeFormatter;
import xyz.property.data.mapper.OutcodeStatsMapper;
import xyz.property.data.model.YieldStats;
import xyz.property.data.service.OutCodeStatsService;
import xyz.property.data.service.PostCodeService;
import xyz.property.data.service.YieldStatsService;
import xyz.property.data.validators.PostCodeValidator;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;


@RegisterForReflection
@Path("/yield")
public class YieldResource {

    @Inject
    @RestClient
    PostCodeService postCodeService;

    @Inject
    @RestClient
    YieldStatsService yieldStatsService;

    @Inject
    @RestClient
    OutCodeStatsService outCodeStatsService;

    @Inject
    PostCodeValidator postCodeValidator;

    @ConfigProperty(name = "provider.propertydata.api.key")
    String apiKey;

    @Inject
    Logger log;

    @Inject
    PostCodeFormatter postCodeFormatter;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Cached(cacheName = "yield-stats")
    public Uni<YieldStats> getYieldStats(@CacheKey @BeanParam YieldSearchParameters searchParams) {

        String postcode =  postCodeFormatter.format(searchParams.getPostcode());

        log.infof("Getting yield stats for postcode: %s ", postcode);

        return postCodeValidator.isValidFullPostCode(postcode)
                .onItemOrFailure().transformToUni((value, error) -> {
                    if (error == null) {
                        return getYieldStatsByPostCode(postcode, searchParams.getBedrooms(), searchParams.getHouseType());
                    } else {
                        log.warnf("Postcode %s is deemed invalid. Trying to recover using outcode.", postcode);
                        return getYieldStatsByOutcode(postcode);
                    }
                }).onItem().invoke(yieldStats -> yieldStats.effective_date = new Date().getTime());
    }

    private Uni<YieldStats> getYieldStatsByPostCode(String postcode, Integer bedrooms, String houseType) {
        return yieldStatsService.getByFullPostCode(apiKey, postcode, bedrooms, houseType)
                .onItemOrFailure()
                .transformToUni((value, error) -> {
                    if (error == null) {
                        log.infof("Successfully fetched yield stats for postcode: %s", postcode);
                        return Uni.createFrom().item(value);
                    } else {
                        log.warnf("Unsuccessfully fetched yield stats for postcode: %s. Trying to recover using its outcode.", postcode);
                        return postCodeService.lookupPostcode(postcode)
                                .onFailure()
                                .invoke(() -> log.errorf("Error while looking up for postcode: %s", postcode))
                                .onFailure()
                                .recoverWithUni(() -> Uni.createFrom().failure(new NotFoundException()))
                                .onItem()
                                .transformToUni(postCodeLookUp -> getYieldStatsByOutcode(postCodeLookUp.result.outcode));
                    }
                });
    }


    private Uni<YieldStats> getYieldStatsByOutcode(String outcode) {

        log.infof("Getting yield stats for outcode: %s ", outcode);

        return postCodeValidator.isValidOutCode(outcode)
                .onItemOrFailure()
                .transformToUni((value, error) -> {
                    if (error == null) {
                        log.infof("Invoking outcode stats service for outcode: %s", outcode);
                        return outCodeStatsService.getStats(outcode)
                                .onFailure()
                                .retry()
                                .atMost(3)
                                .map(OutcodeStatsMapper.INSTANCE::outcodeStatsToYieldStats)
                                .onFailure()
                                .recoverWithUni(o -> Uni.createFrom().failure(new NotFoundException()));
                    } else {
                        log.warnf("Outcode %s is deemed invalid", outcode);
                        return Uni.createFrom().failure(new NotFoundException());
                    }
                });
    }
}