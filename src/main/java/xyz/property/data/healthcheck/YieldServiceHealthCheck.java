package xyz.property.data.healthcheck;


import io.smallrye.health.api.AsyncHealthCheck;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import xyz.property.data.resource.YieldResource;
import xyz.property.data.resource.YieldSearchParameters;
import xyz.property.data.service.OutCodeStatsService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Readiness
@ApplicationScoped
public class YieldServiceHealthCheck implements AsyncHealthCheck {

    @Inject
    YieldResource yieldResource;

    @Inject
    YieldSearchParameters yieldSearchParameters;


    @Override
    public Uni<HealthCheckResponse> call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Yield service health check");

        yieldSearchParameters.setPostcode("B11BB");
        return yieldResource.getYieldStats(yieldSearchParameters)
                .onItem().transform(postCodeValidation -> responseBuilder
                        .up()
                        .withData("Yield service:", "OK").build())
                .onFailure()
                .recoverWithItem(failure -> responseBuilder
                        .down()
                        .withData("Yield service KO:", failure.getMessage()).build());
    }
}
