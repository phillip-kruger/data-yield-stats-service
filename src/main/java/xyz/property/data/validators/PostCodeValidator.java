package xyz.property.data.validators;

import io.smallrye.mutiny.Uni;
import lombok.NonNull;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import xyz.property.data.service.PostCodeService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.NotFoundException;

@ApplicationScoped
public class PostCodeValidator {

    @Inject
    @RestClient
    PostCodeService postCodeService;

    @Inject
    Logger log;

    public Uni<Void> isValidFullPostCode(@NonNull String postcode) {
        log.infof("Validating postcode: %s", postcode);
        return postCodeService.validateFullPostcode(postcode)
                .onItem()
                .transform(postCodeValidation -> {
                    if (postCodeValidation.result) {
                        return null;
                    } else {
                        throw new NotFoundException();
                    }
                });
    }

    public Uni<Void> isValidOutCode(@NonNull String outcode) {
        log.infof("Validating outcode %s ", outcode);

        /*
           Postcodes.io does not offer am outcodeValidation api. Hence,
           we use autocomplete to check if the outcode is valid.
         */
        return postCodeService.validateOutCode(outcode)
                .onItem()
                .transform(outCodeValidation -> {
                    if (outCodeValidation.result != null) {
                        return null;
                    } else {
                        throw new NotFoundException();
                    }
                });
    }
}
