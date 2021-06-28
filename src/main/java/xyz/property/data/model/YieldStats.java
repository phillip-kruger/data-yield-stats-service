package xyz.property.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.infinispan.protostream.annotations.ProtoField;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@RegisterForReflection
public class YieldStats {

    public String status;
    public String code;
    public String message;
    public String process_time;
    public Long effective_date;

    public String postcode;
    public String postcode_type;
    public String url;
    public Integer bedrooms;
    public String type;
    public Data data;

    public static class LongLet{
        @ProtoField(number = 1)
        public Integer points_analysed;
        @ProtoField(number = 2)
        public String radius;
        @ProtoField(number = 3)
        public String gross_yield;
    }

    public static class Data{
        @ProtoField(number = 1)
        public LongLet long_let;
    }

    @ProtoField(number = 1)
    public String getStatus() {
        return status;
    }

    @ProtoField(number = 2)
    public String getCode() {
        return code;
    }

    @ProtoField(number = 3)
    public String getMessage() {
        return message;
    }

    @ProtoField(number = 4)
    public String getProcess_time() {
        return process_time;
    }

    @ProtoField(number = 5)
    public Long getEffective_date() {
        return effective_date;
    }

    @ProtoField(number = 6)
    public String getPostcode() {
        return postcode;
    }

    @ProtoField(number = 7)
    public String getPostcode_type() {
        return postcode_type;
    }

    @ProtoField(number = 8)
    public String getUrl() {
        return url;
    }

    @ProtoField(number = 9)
    public Integer getBedrooms() {
        return bedrooms;
    }

    @ProtoField(number = 10)
    public String getType() {
        return type;
    }

    @ProtoField(number = 11)
    public Data getData() {
        return data;
    }
}
