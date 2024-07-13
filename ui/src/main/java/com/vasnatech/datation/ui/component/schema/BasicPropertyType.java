package com.vasnatech.datation.ui.component.schema;

import com.vasnatech.datation.ui.design.schema.Dimension;
import com.vasnatech.datation.ui.design.schema.Insets;
import com.vasnatech.datation.ui.design.schema.Rectangle;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum BasicPropertyType implements PropertyType {

    BOOLEAN("boolean", Boolean::valueOf),
    INTEGER("integer", Integer::valueOf),
    FLOAT("float", Float::valueOf),
    STRING("string", Function.identity()),
    STRING_ARRAY("string[]", s -> s.split(",")),
    INSETS("insets", Insets::valueOf),
    RECTANGLE("rectangle",  Rectangle::valueOf),
    DIMENSION("dimension", Dimension::valueOf),
    URI("uri", java.net.URI::create)
    ;

    final String key;
    final Function<String, ?> parser;

    BasicPropertyType(String key, Function<String, ?> parser) {
        this.key = key;
        this.parser = parser;
    }

    public String key() {
        return key;
    }

    @Override
    public Object parseValue(String stringValue) {
        return stringValue == null ? null : parser.apply(stringValue);
    }

    static final Map<String, BasicPropertyType> MAP = Stream.of(values()).collect(Collectors.toMap(BasicPropertyType::key, it -> it));

    public static BasicPropertyType findByKey(String key) {
        return MAP.get(key);
    }
}
