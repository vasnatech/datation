package com.vasnatech.datation.ui.component.schema;

import java.util.Set;

public record EnumPropertyType(Set<String> enumLiterals) implements PropertyType {

    @Override
    public String key() {
        return "enum";
    }

    @Override
    public Object parseValue(String stringValue) {
        return enumLiterals.contains(stringValue) ? stringValue : null;
    }
}
