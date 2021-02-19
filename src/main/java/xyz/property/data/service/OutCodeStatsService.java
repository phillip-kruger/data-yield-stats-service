package xyz.property.data.service;


import io.smallrye.common.constraint.NotNull;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import xyz.property.data.model.HealthStatus;
import xyz.property.data.model.OutCodeStats;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/")
@RegisterRestClient(configKey = "outcode-stats-service")
public interface OutCodeStatsService {

    @GET()
    @Path("key-stats")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<OutCodeStats> getStats(@NotNull @QueryParam("outcode") String outcode);


    @GET
    @Path("q/health")
    @Produces(MediaType.APPLICATION_JSON)
    HealthStatus getStatus();
}
