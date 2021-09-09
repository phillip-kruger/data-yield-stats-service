package xyz.property.data.healthcheck;


import io.smallrye.health.api.AsyncHealthCheck;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import xyz.property.data.resources.YieldResource;
import xyz.property.data.resources.SearchCriteria;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Readiness
@ApplicationScoped
public class YieldServiceHealthCheck implements AsyncHealthCheck {

    @Inject
    YieldResource yieldResource;


    @Override
    public Uni<HealthCheckResponse> call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Yield service health check");

        SearchCriteria searchCriteria = new SearchCriteria("B11BB", null, null);
        return yieldResource.getStats(searchCriteria)
                .onItem().transform(postCodeValidation -> responseBuilder
                        .up()
                        .withData("Yield service:", "OK").build())
                .onFailure()
                .recoverWithItem(failure -> responseBuilder
                        .down()
                        .withData("Yield service KO:", failure.getMessage()).build());
    }
}
