package xyz.property.data.formatters.postcode;

import xyz.property.data.formatters.Formatter;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostCodeFormatter implements Formatter<String> {
    private static final String UK_POSTCODE_FORMAT = "(?i)^([A-Z]{1,2}\\d[A-Z\\d]?)\\s?(\\d[A-Z]{2})?$";


    @Override
    public String format(String toFormat) {
        return toFormat.toUpperCase().replaceAll(UK_POSTCODE_FORMAT, "$1 $2");
    }
}
