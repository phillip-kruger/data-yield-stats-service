package xyz.property.data.healthcheck;


import io.smallrye.health.api.AsyncHealthCheck;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import xyz.property.data.service.OutCodeStatsService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Readiness
@ApplicationScoped
public class OutcodeServiceHealthCheck implements AsyncHealthCheck {

    @Inject
    @RestClient
    OutCodeStatsService outCodeStatsService;


    @Override
    public Uni<HealthCheckResponse> call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("OutcodeStats service health check");

        return outCodeStatsService.getStats("NG5")
                .onItem().transform(postCodeValidation -> responseBuilder
                        .up()
                        .withData("OutcodeStats service:", "OK").build())
                .onFailure()
                .recoverWithItem(failure -> responseBuilder
                        .down()
                        .withData("OutcodeStats service KO:", failure.getMessage()).build());
    }
}
