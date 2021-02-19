package xyz.property.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YieldStats {

    public String status;
    public String code;
    public String message;
    public String process_time;

    public String postcode;
    public String postcode_type;
    public String url;
    public int bedrooms;
    public Data data;

    public static class LongLet{
        public int points_analysed;
        public String radius;
        public String gross_yield;
    }

    public static class Data{
        public LongLet long_let;
    }

    public YieldStats() {
        this.data = new Data();
        this.data.long_let = new LongLet();
    }
}
