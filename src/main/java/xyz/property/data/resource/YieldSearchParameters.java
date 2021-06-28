package xyz.property.data.resource;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.Pattern;
import javax.ws.rs.QueryParam;

@Data
public class YieldSearchParameters {

    @Parameter(required = true)
    @Pattern(regexp = "(?i)^([A-Z]{1,2}\\d[A-Z\\d]?)\\s?(\\d[A-Z]{2})?$" )
    @QueryParam("postcode")
    String postcode;

    @QueryParam("bedrooms")
    @Range(min = 1, max = 5, message = "Number of bedrooms must be within 1 and 5.")
    Integer bedrooms;

    @QueryParam("type")
    @Pattern(regexp = "flat|terraced_house|semi-detached_house|detached_house")
    String houseType;
}
