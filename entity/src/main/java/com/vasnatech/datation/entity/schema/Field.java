package com.vasnatech.datation.entity.schema;

import com.vasnatech.datation.schema.Node;

import java.util.LinkedHashMap;

public class Field extends Node {

    final DDL.Column ddl;
    final FieldType type;
    final String itemType;
    final FieldFetch fetch;
    final boolean nullable;
    final LinkedHashMap<String, ?> enumValues;

    Field(
            String name,
            DDL.Column ddl,
            FieldType type,
            String itemType,
            FieldFetch fetch,
            boolean nullable,
            LinkedHashMap<String, ?> enumValues
    ) {
        super(name);
        this.ddl = ddl;
        this.type = type;
        this.itemType = itemType;
        this.fetch = fetch;
        this.nullable = nullable;
        this.enumValues = enumValues;
    }

    public DDL.Column getDDL() {
        return ddl;
    }

    public FieldType getType() {
        return type;
    }

    public String getItemType() {
        return itemType;
    }

    public FieldFetch getFetch() {
        return fetch;
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

    public boolean isSimple() {
        return ddl != null && ddl.isSimple();
    }

    public boolean isRelational() {
        return ddl != null && ddl.isRelational();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        DDL.Column ddl;
        FieldType type;
        String itemType;
        FieldFetch fetch;
        boolean nullable = true;
        LinkedHashMap<String, Object> enumValues = new LinkedHashMap<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder ddl(DDL.Column ddl) {
            this.ddl = ddl;
            return this;
        }

        public Builder type(FieldType type) {
            this.type = type;
            return this;
        }

        public Builder itemType(String itemType) {
            this.itemType = itemType;
            return this;
        }

        public Builder fetch(FieldFetch fetch) {
            this.fetch = fetch;
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
            return new Field(name, ddl, type, itemType, fetch, nullable, enumValues);
        }
    }
}
