package xyz.property.data.service;


import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jdk.jfr.Description;
import lombok.NonNull;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import xyz.property.data.model.YieldStats;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/yields")
@Description("For a given UK postcode (full, district or sector) and optional filters, returns average property yield" +
             "from the smallest radius at which there is reasonable data.")
@RegisterRestClient(configKey = "yield-service")
public interface YieldStatsService {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @CacheResult(cacheName = "yield-data")
    Uni<YieldStats> getByFullPostCode(@NonNull @QueryParam("key") String key,
                                      @CacheKey @NonNull @QueryParam("postcode") String postcode,
                                      @CacheKey @QueryParam("bedrooms") Integer bedrooms,
                                      @CacheKey @QueryParam("type") String houseType);

}
