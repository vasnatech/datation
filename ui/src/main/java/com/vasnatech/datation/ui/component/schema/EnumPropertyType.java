package com.vasnatech.datation.ui.component.schema;

import java.util.Set;

public record EnumPropertyType(Set<String> enumLiterals) implements PropertyType {

    @Override
    public String value() {
        return "enum";
    }
}
