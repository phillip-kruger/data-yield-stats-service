package xyz.property.data.model;

import lombok.ToString;

@ToString
public enum HouseType {
    flat("flat"),
    terraced("terraced_house"),
    semiDetached("semi-detached_house"),
    detached("detached_house");
    String typeName;

    HouseType(String houseType) {
        typeName = houseType;
    }
}
