package xyz.property.data.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@RegisterForReflection
public class PostCodeValidation {
    public int status;
    public boolean result;
}
