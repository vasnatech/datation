package com.vasnatech.datation.schema.ddl;

import java.util.LinkedHashMap;

public class Column extends Node {

    final ColumnType type;
    final int length;
    final int length2;
    final boolean nullable;
    final LinkedHashMap<String, ?> enumValues;

    Column(
            String name,
            ColumnType type,
            int length,
            int length2,
            boolean nullable,
            LinkedHashMap<String, ?> enumValues
    ) {
        super(name);
        this.type = type;
        this.length = length;
        this.length2 = length2;
        this.nullable = nullable;
        this.enumValues = enumValues;
    }

    public ColumnType getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public int getLength2() {
        return length2;
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
        ColumnType type;
        int length = 0;
        int length2 = 0;
        boolean nullable = true;
        LinkedHashMap<String, Object> enumValues = new LinkedHashMap<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(ColumnType type) {
            this.type = type;
            return this;
        }

        public Builder length(int length) {
            this.length = length;
            return this;
        }

        public Builder length2(int length2) {
            this.length2 = length2;
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

        public Column build() {
            return new Column(name, type, length, length2, nullable, enumValues);
        }
    }
}
