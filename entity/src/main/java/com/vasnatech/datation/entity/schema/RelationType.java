package com.vasnatech.datation.entity.schema;

import java.util.stream.Stream;

public enum RelationType {
    ONE_TO_ONE("one-to-one", false),
    ONE_TO_MANY("one-to-many", true),
    MANY_TO_ONE("many-to-one", false),
    MANY_TO_MANY("many-to-many", true)
    ;

    final String value;
    final boolean collection;

    RelationType(String value, boolean collection) {
        this.value = value;
        this.collection = collection;
    }

    public String value() {
        return value;
    }

    public boolean isCollection() {
        return collection;
    }

    public boolean isEntity() {
        return !collection;
    }

    public static RelationType finByValue(String value) {
        return Stream.of(values()).filter(it -> it.value().equals(value)).findAny().orElse(null);
    }
}
