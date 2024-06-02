package com.vasnatech.datation.ddl.schema;

import com.vasnatech.commons.schema.schema.Node;

import java.util.LinkedHashMap;

public record DDLSchema(
        String name,
        LinkedHashMap<String, Table> definitions,
        LinkedHashMap<String, Table> tables
) implements Node {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        LinkedHashMap<String, Table> definitions = new LinkedHashMap<>();
        LinkedHashMap<String, Table> tables = new LinkedHashMap<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder definition(Table table) {
            this.definitions.put(table.name(), table);
            return this;
        }

        public Builder table(Table table) {
            this.tables.put(table.name(), table);
            return this;
        }

        public DDLSchema build() {
            return new DDLSchema(name, definitions, tables);
        }
    }
}
