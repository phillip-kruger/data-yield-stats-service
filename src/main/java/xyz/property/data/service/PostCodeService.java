package xyz.property.data.service;


import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import xyz.property.data.model.PostCode;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/postcodes")
@RegisterRestClient(configKey = "postcode-validator-service")
public interface PostCodeService {

    @GET
    @Path("/{postcode}")
    @Produces(MediaType.APPLICATION_JSON)
    PostCode lookupPostCode(@PathParam("postcode") String postcode);
}
