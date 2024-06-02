package com.vasnatech.datation.ddl.schema;

import com.vasnatech.commons.schema.schema.Node;

import java.util.LinkedHashMap;

public record Column(
        String name,
        ColumnType type,
        int length,
        int length2,
        boolean nullable,
        LinkedHashMap<String, ?> enumValues
) implements Node {

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
