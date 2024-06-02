package com.vasnatech.datation.ui.component.schema;

import java.util.Set;

public interface PropertyType {

    String key();

    Object parseValue(String stringValue);

    @SuppressWarnings("unchecked")
    static PropertyType of(Object value) {
        if (value instanceof Set<?> enumLiterals) {
            return new EnumPropertyType((Set<String>)enumLiterals);
        }
        return BasicPropertyType.findByKey((String) value);
    }
}
