package xyz.property.data.resource;

import io.smallrye.mutiny.Uni;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import xyz.property.data.model.Response;
import xyz.property.data.model.YieldStats;
import xyz.property.data.service.OutCodeStatsService;
import xyz.property.data.service.PostCodeService;
import xyz.property.data.service.YieldService;
import xyz.property.data.utils.Validator;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.temporal.ChronoUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Path("/yield")
@Slf4j
public class YieldResource {

    @Inject
    @RestClient
    YieldService yieldService;

    @Inject
    @RestClient
    OutCodeStatsService outCodeStatsService;

    @Inject
    @RestClient
    PostCodeService postCodeService;


    @Inject
    Validator validator;

    @ConfigProperty(name = "provider.propertydata.api.key")
    String apiKey;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @CircuitBreaker(skipOn = IllegalArgumentException.class)
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    public Uni<Response> getYieldStats(@NonNull @QueryParam("postcode") String postcode,
                                       @QueryParam("bedrooms") Integer bedrooms,
                                       @QueryParam("type") String houseType) {

        validateRequest(postcode, bedrooms, houseType);

        Uni<YieldStats> yield = yieldService.getByFullPostcode(apiKey, postcode, bedrooms, houseType);

        return yield.onItem()
                .call(this::validateResponse)
                .onItem()
                .transformToUni(result -> Uni.createFrom().item(Response.builder()
                        .postcode(result.postcode)
                        .avgYield(result.data.long_let.gross_yield).build()))
                .onFailure()
                .recoverWithUni(() -> getOutcodeYield(postcode));
    }

    private Uni<Response> getOutcodeYield(String postcode) {
        String outcode = postCodeService.lookupPostCode(postcode).result.outcode;
        return outCodeStatsService.getStats(outcode)
                .onItem()
                .transformToUni(outCodeStats -> Uni.createFrom().item(Response.builder().postcode(outCodeStats.outcode)
                        .avgYield(String.valueOf(outCodeStats.avgYield)).build()));
    }


    void validateRequest(String postcode, Integer bedrooms, String houseType) {
        checkNotNull(postcode);
        checkArgument(validator.isValidFullPostcode(postcode));

        if (bedrooms != null) checkArgument(bedrooms > 1 && bedrooms <= 5);
        if (houseType != null) checkArgument(validator.isValidHouseType(houseType));
    }

    private Uni<?> validateResponse(YieldStats response) {
        //ref: https://propertydata.co.uk/api/documentation/error-reference)
        if (response.status.equals("error")) {
            log.warn("[YieldStats]: received error code {} while retrieving yield stats.", response.code);
            return Uni.createFrom().failure(new NotFoundException());
        }
        return Uni.createFrom().voidItem();
    }
}