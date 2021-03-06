package xyz.property.data.converters;

import io.smallrye.common.constraint.NotNull;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.regex.Pattern;

@ApplicationScoped
public class PostCodeConverter {

    static Pattern postCodePattern;
    public static final String POST_CODE_PATTERN = "(?i)^([A-Z]{1,2}\\d[A-Z\\d]?)\\s?(\\d[A-Z]{2})?$";

    @PostConstruct
    void setMatcher() {
        postCodePattern = Pattern.compile(POST_CODE_PATTERN, Pattern.MULTILINE | Pattern.DOTALL);
    }

    public String formatPostCode(@NotNull String postcode) {
        return postCodePattern
                .matcher(postcode.toUpperCase())
                .replaceAll("$1$2");
    }
}
