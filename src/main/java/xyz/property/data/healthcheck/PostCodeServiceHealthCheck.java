package xyz.property.data.healthcheck;


import io.netty.handler.codec.http.HttpResponseStatus;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import xyz.property.data.service.PostCodeService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;

@Readiness
@Singleton
public class PostCodeServiceHealthCheck implements HealthCheck {

    @Inject
    @RestClient
    PostCodeService postCodeService;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Upstream health check");

        int postcodeServiceStatus = postCodeService.validateFullPostcode("NG54AU")
                .await()
                .atMost(Duration.ofMillis(2000))
                .status;

        if (postcodeServiceStatus == HttpResponseStatus.OK.code()) {
            responseBuilder
                    .up()
                    .withData("Postcodes service:", "OK");
        } else {
            responseBuilder
                    .down()
                    .withData("Postcodes service:", "KO");
        }

        return responseBuilder.build();
    }

}
