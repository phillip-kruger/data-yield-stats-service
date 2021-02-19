package xyz.property.data.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import xyz.property.data.model.OutCodeValidation;
import xyz.property.data.model.PostCodeValidation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/postcodes")
@RegisterRestClient(configKey = "postcode-validator-service")
public interface PostCodeService {

    @GET
    @Path("/{postcode}/validate")
    @Produces(MediaType.APPLICATION_JSON)
    PostCodeValidation validateFullPostcode(@PathParam("postcode") String postcode);

    @GET
    @Path("/{outcode}/autocomplete")
    @Produces(MediaType.APPLICATION_JSON)
    OutCodeValidation validateOutCode(@PathParam("outcode") String outcode);
}
