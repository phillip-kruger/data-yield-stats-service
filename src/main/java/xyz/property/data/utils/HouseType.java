package xyz.property.data.utils;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum HouseType {
    flat("flat"),
    terraced("terraced_house"),
    semiDetached("semi-detached_house"),
    detached("detached_house");
    String typeName;
}
