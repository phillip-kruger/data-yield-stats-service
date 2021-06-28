package xyz.property.data.healthcheck;


import io.netty.handler.codec.http.HttpResponseStatus;
import io.smallrye.health.api.AsyncHealthCheck;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import xyz.property.data.service.PostCodeService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;

@Readiness
@ApplicationScoped
public class PostCodeServiceHealthCheck implements AsyncHealthCheck {

    @Inject
    @RestClient
    PostCodeService postCodeService;


    @Override
    public Uni<HealthCheckResponse> call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Postcode service health check");

        return postCodeService.validateFullPostcode("NG54AU")
                .onItem().transform(postCodeValidation -> responseBuilder
                        .up()
                        .withData("Postcodes service:", "OK").build())
                .onFailure()
                .recoverWithItem(failure -> responseBuilder
                        .down()
                        .withData("Postcodes service KO:", failure.getMessage()).build());
    }
}