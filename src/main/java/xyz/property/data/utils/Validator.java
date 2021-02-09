package xyz.property.data.utils;

import lombok.val;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import xyz.property.data.model.PostCode;
import xyz.property.data.service.PostCodeService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicBoolean;

@Singleton
public class Validator {

    @Inject
    @RestClient
    PostCodeService postCodeService;

    public boolean isValidPostcode(String postcode) {
        PostCode lookupPostCode = postCodeService.lookupPostCode(postcode);
        return lookupPostCode.status == HttpStatus.SC_OK;
    }

    public boolean isValidHouseType(String houseType) {
        val response = new AtomicBoolean(false);
        try {
            Enum.valueOf(HouseType.class, houseType);
            response.set(true);
        } catch (IllegalArgumentException e) {
            response.set(false);
        }
        return response.get();
    }
}
