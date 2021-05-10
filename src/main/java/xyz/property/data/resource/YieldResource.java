package xyz.property.data.resource;

import io.smallrye.common.constraint.Nullable;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.jboss.logging.Logger;
import xyz.property.data.annotations.ValidHouseType;
import xyz.property.data.mapper.OutcodeStatsMapper;
import xyz.property.data.model.YieldStats;
import xyz.property.data.service.OutCodeStatsService;
import xyz.property.data.service.PostCodeService;
import xyz.property.data.service.YieldStatsService;
import xyz.property.data.validator.PostCodeValidator;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Path("/yield")
public class YieldResource {

    @Inject
    @RestClient
    PostCodeService postCodeService;
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


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @CircuitBreaker(skipOn = IllegalArgumentException.class)
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    public Uni<YieldStats> getYieldStats(@QueryParam("postcode")
                                         @Length(min = 2, max = 8, message = "A valid postcode must contain between 2 and 8 alphanumeric characters.")
                                                 String postcode,
                                         @Range(min = 1, max = 5, message = "Number of bedrooms must be within 1 and 5.")
                                         @Nullable
                                         @QueryParam("bedrooms") Integer bedrooms,
                                         @ValidHouseType
                                         @Nullable
                                         @QueryParam("type") String houseType) {

        log.infof("Getting yield stats for postcode: %s ", postcode);

        return postCodeValidator.isValidFullPostCode(postcode)
                .onItemOrFailure().transformToUni((value, error) -> {
                    if (error == null) {
                        return getYieldStatsByPostCode(postcode, bedrooms, houseType);
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


    public Uni<YieldStats> getYieldStatsByOutcode(String outcode) {

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