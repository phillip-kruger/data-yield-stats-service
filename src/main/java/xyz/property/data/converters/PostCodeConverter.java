package xyz.property.data.converters;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotEmpty;
import java.util.regex.Pattern;

@ApplicationScoped
public class PostCodeConverter {

    static Pattern postCodePattern;
    public static final String POST_CODE_PATTERN = "(?i)^([A-Z]{1,2}\\d[A-Z\\d]?) ?(\\d[A-Z]{2})$";

    @PostConstruct
    void setMatcher() {
        postCodePattern = Pattern.compile(POST_CODE_PATTERN, Pattern.MULTILINE | Pattern.DOTALL);
    }

    public String formatPostCode(@NotEmpty String postcode) {
        return postCodePattern
                .matcher(postcode.toUpperCase())
                .replaceAll("$1$2");
    }
}
