package xyz.property.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@RegisterForReflection
public class HealthStatus {
    public String status;
}
