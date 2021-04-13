package xyz.property.data.healthcheck;


import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import xyz.property.data.service.OutCodeStatsService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Liveness
@Singleton
public class OutcodeServiceHealthCheck implements HealthCheck {


    @Inject
    @RestClient
    OutCodeStatsService outCodeStatsService;


    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("OutcodeStats service health check");

        if ((outCodeStatsService.getStatus().status.equals("UP"))) {
            responseBuilder
                    .up()
                    .withData("Outcode Stats service:", "OK");
        } else {
            responseBuilder
                    .down()
                    .withData("Outcode Stats service:", "KO");
        }

        return responseBuilder.build();
    }
}
