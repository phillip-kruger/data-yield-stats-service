package xyz.property.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostCode {
    public int status;
    public Result result;

    public static class Result {
        public String postcode;
        public int quality;
        public int eastings;
        public int northings;
        public String country;
        public String nhs_ha;
        public double longitude;
        public double latitude;
        public String european_electoral_region;
        public String primary_care_trust;
        public String region;
        public String lsoa;
        public String msoa;
        public String incode;
        public String outcode;
        public String parliamentary_constituency;
        public String admin_district;
        public String parish;
        public Object admin_county;
        public String admin_ward;
        public Object ced;
        public String ccg;
        public String nuts;
    }


}
