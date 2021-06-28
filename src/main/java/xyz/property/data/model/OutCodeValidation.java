package xyz.property.data.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import java.util.List;

@Data
@RegisterForReflection
public class OutCodeValidation {
    public int status;
    public List<String> result;
}
