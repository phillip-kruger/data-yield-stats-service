package xyz.property.data.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum HouseType {
    flat("flat"),
    terraced("terraced_house"),
    semiDetached("semi-detached_house"),
    detached("detached_house");
    String typeName;

    HouseType(String houseType) {
        typeName = houseType;
    }

    public String toString() {
        return typeName;
    }
}
