package com.vasnatech.datation.entity.schema;

import com.vasnatech.datation.schema.Node;

import java.util.LinkedHashMap;

public class Field extends Node {

    final String ddlColumn;
    final FieldType type;
    final FieldType itemType;
    final boolean nullable;
    final LinkedHashMap<String, ?> enumValues;

    Field(
            String name,
            String ddlColumn,
            FieldType type,
            FieldType itemType,
            boolean nullable,
            LinkedHashMap<String, ?> enumValues
    ) {
        super(name);
        this.ddlColumn = ddlColumn;
        this.type = type;
        this.itemType = itemType;
        this.nullable = nullable;
        this.enumValues = enumValues;
    }

    public String getDdlColumn() {
        return ddlColumn;
    }

    public FieldType getType() {
        return type;
    }

    public FieldType getItemType() {
        return itemType;
    }

    public boolean isNullable() {
        return nullable;
    }

    public LinkedHashMap<String, ?> getEnumValues() {
        return enumValues;
    }

    public boolean isEnum() {
        return !enumValues.isEmpty();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        String ddlColumn;
        FieldType type;
        FieldType itemType;
        boolean nullable = true;
        LinkedHashMap<String, Object> enumValues = new LinkedHashMap<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder ddlColumn(String ddlColumn) {
            this.ddlColumn = ddlColumn;
            return this;
        }

        public Builder type(FieldType type) {
            this.type = type;
            return this;
        }

        public Builder itemType(FieldType itemType) {
            this.itemType = itemType;
            return this;
        }

        public Builder nullable(boolean nullable) {
            this.nullable = nullable;
            return this;
        }

        public Builder enumValue(String key, Object value) {
            this.enumValues.put(key, value);
            return this;
        }

        public Field build() {
            return new Field(name, ddlColumn, type, itemType, nullable, enumValues);
        }
    }
}
