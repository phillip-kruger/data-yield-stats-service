package xyz.property.data.handlers;


import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.graphql.*;
import xyz.property.cache.CacheKey;
import xyz.property.cache.Cached;
import xyz.property.data.converters.PostCodeConverter;
import xyz.property.data.model.YieldStats;
import xyz.property.data.resources.YieldResource;
import xyz.property.data.resources.YieldSearchCriteria;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLApi
@ApplicationScoped
public class GraphqlHandler {

    @Inject
    YieldResource yieldResource;

    @Inject
    PostCodeConverter postCodeConverter;

    @Query
    @Description("Fetch yield stats for the give full postcode")
    @Cached(cacheName = "yield-data")
    public Uni<YieldStats> getYield(@CacheKey YieldSearchCriteria searchCriteria) {
        return yieldResource.getStats(searchCriteria);
    }
}
