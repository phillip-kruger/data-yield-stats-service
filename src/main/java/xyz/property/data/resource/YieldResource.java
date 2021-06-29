package xyz.property.data.resource;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import xyz.property.cache.CacheKey;
import xyz.property.cache.Cached;
import xyz.property.data.mapper.OutcodeStatsMapper;
import xyz.property.data.model.YieldStats;
import xyz.property.data.service.OutCodeStatsService;
import xyz.property.data.service.YieldStatsService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;


@RegisterForReflection
@Path("/yield")
public class YieldResource {


    @Inject
    @RestClient
    YieldStatsService yieldStatsService;

    @Inject
    @RestClient
    OutCodeStatsService outCodeStatsService;

    @ConfigProperty(name = "provider.propertydata.api.key")
    String apiKey;

    @Inject
    Logger log;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Cached(cacheName = "yield-stats")
    public Uni<YieldStats> getYieldStats(@CacheKey @BeanParam YieldSearchParameters searchParams) {

        log.infof("Getting yield stats for postcode: %s ", searchParams.getPostcode());

        return yieldStatsService.getByFullPostCode(apiKey, searchParams.getPostcode(), searchParams.getBedrooms(), searchParams.getHouseType())
                .onItem()
                    .invoke(() -> log.infof("Successfully fetched stats for postcode:%s", searchParams.getPostcode()))
                .onFailure()
                    .recoverWithUni(()-> getYieldStatsByOutcode(searchParams.getPostcode()))
                .onItem()
                    .invoke(yieldStats -> yieldStats.effective_date = new Date().getTime())
                .onFailure()
                    .recoverWithUni(o -> Uni.createFrom().failure(new NotFoundException()));
    }

    private Uni<YieldStats> getYieldStatsByOutcode(String outcode) {

        log.infof("Getting yield stats for outcode: %s ", outcode);

        return outCodeStatsService.getStats(outcode)
                .onItem()
                    .invoke(i -> log.infof("Successfully fetched stats for outcode:%s", outcode))
                .onFailure()
                    .retry()
                    .atMost(3)
                .onItem()
                    .transform(OutcodeStatsMapper.INSTANCE::outcodeStatsToYieldStats)
                .onFailure()
                    .invoke(error -> log.warnf("Failed to fetch stats for outcode %s: %s", outcode, error.getMessage()));
    }
}