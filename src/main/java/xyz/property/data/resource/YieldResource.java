package xyz.property.data.resource;

import io.smallrye.common.constraint.Nullable;
import io.smallrye.mutiny.Uni;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.hibernate.validator.constraints.Range;
import org.jboss.logging.Logger;
import xyz.property.data.annotations.ValidHouseType;
import xyz.property.data.mapper.OutcodeStatsMapper;
import xyz.property.data.model.OutCodeStats;
import xyz.property.data.model.YieldStats;
import xyz.property.data.service.OutCodeStatsService;
import xyz.property.data.service.PostCodeService;
import xyz.property.data.service.YieldStatsService;
import xyz.property.data.validator.PostCodeValidator;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.temporal.ChronoUnit;


@Path("/yield")
public class YieldResource {

    @Inject
    @RestClient
    YieldStatsService yieldStatsService;

    @Inject
    @RestClient
    OutCodeStatsService outCodeStatsService;

    @Inject
    @RestClient
    PostCodeService postCodeService;

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
    public Uni<YieldStats> getYieldStats(@NonNull @QueryParam("postcode") String postcode,
                                         @Range(min = 1, max = 5, message = "Number of bedrooms must be within 1 and 5.")
                                         @Nullable
                                         @QueryParam("bedrooms") Integer bedrooms,
                                         @ValidHouseType
                                         @Nullable
                                         @QueryParam("type") String houseType) {


        log.tracef("Getting yield stats for postcode: %s ",postcode);

        Uni<YieldStats> yieldStats;

        if (postCodeValidator.isValidFullPostCode(postcode)) {
            yieldStats = yieldStatsService.getByFullPostCode(apiKey, postcode, bedrooms, houseType);
        } else if (postCodeValidator.isValidOutCode(postcode)) {
            Uni<OutCodeStats> outCodeStats = outCodeStatsService.getStats(postcode);
            yieldStats = outCodeStats
                    .onItem()
                    .transformToUni(stats -> Uni.createFrom()
                            .item(OutcodeStatsMapper.INSTANCE.outcodeStatsToYieldStats(stats)))
                    .onFailure().retry().atMost(3);
        } else {
            log.warn("Postcode " + postcode + " is deemed invalid.");
            yieldStats = Uni.createFrom().failure(NotFoundException::new);
        }
        return yieldStats;
    }
}