package xyz.property.data.validator;

import lombok.NonNull;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import xyz.property.data.model.PostCodeValidation;
import xyz.property.data.service.PostCodeService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.ConstraintValidatorContext;

@Singleton
public class PostCodeValidator {

    @Inject
    @RestClient
    PostCodeService postCodeService;

    @Inject
    Logger log;

    public boolean isValidFullPostCode(@NonNull String postcode) {
        log.infof("Validating postcode: %s", postcode);
        return postCodeService.validateFullPostcode(postcode).result;
    }

    public boolean isValidOutCode(@NonNull String outcode) {
        log.infof("Validating outcode %s ", outcode);

        /*
           Postcodes.io does not offer am outcodeValidation api. Hence,
           we use autocomplete to check if the outcode is valid.
         */
        return postCodeService.validateOutCode(outcode).result != null;
    }
}
