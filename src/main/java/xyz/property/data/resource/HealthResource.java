package xyz.property.data.resource;

import org.apache.http.HttpStatus;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import xyz.property.data.model.HealthStatus;
import xyz.property.data.model.PostCodeValidation;
import xyz.property.data.service.OutCodeStatsService;
import xyz.property.data.service.PostCodeService;

import javax.inject.Inject;
import javax.inject.Singleton;


@Liveness
@Singleton
public class HealthResource implements HealthCheck {


    @Inject
    @RestClient
    OutCodeStatsService outCodeStatsService;

    @Inject
    @RestClient
    PostCodeService postCodeService;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Upstream health check");

        checkPostcodeService(responseBuilder);
        checkOutcodeService(responseBuilder);

        return responseBuilder.build();
    }

    private void checkOutcodeService(HealthCheckResponseBuilder responseBuilder) {
        HealthStatus outcodeServiceStatus = outCodeStatsService.getStatus();

        if ((outcodeServiceStatus.status.equals("UP"))) {
            responseBuilder
                    .up()
                    .withData("Outcode Stats service:", "OK");
        } else {
            responseBuilder
                    .down()
                    .withData("Outcode Stats service:", "KO");
        }
    }

    private void checkPostcodeService(HealthCheckResponseBuilder responseBuilder) {
        PostCodeValidation postCodeValidation = postCodeService.validateFullPostcode("NG5 4AU");
        if (postCodeValidation.status == HttpStatus.SC_OK) {
            responseBuilder
                    .up()
                    .withData("Postcodes service:", "OK");
        } else {
            responseBuilder
                    .down()
                    .withData("Postcodes service:", "KO");
        }
    }
}
