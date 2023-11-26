package com.vasnatech.datation.ui.component.schema;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum BasicPropertyType implements PropertyType {
    BOOLEAN("boolean"),
    INTEGER("integer"),
    FLOAT("float"),
    STRING("string"),
    STRING_ARRAY("string[]"),
    INSETS("insets"),
    RECTANGLE("rectangle"),
    DIMENSION("dimension"),
    URI("uri")
    ;

    final String value;

    BasicPropertyType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    static final Map<String, BasicPropertyType> MAP = Stream.of(values()).collect(Collectors.toMap(BasicPropertyType::value, it -> it));

    public static BasicPropertyType findByValue(String value) {
        return MAP.get(value);
    }
}
