package com.vasnatech.datation.schema;

import java.util.LinkedHashMap;

public class Schema extends Node {

    final LinkedHashMap<String, Table> definitions;
    final LinkedHashMap<String, Table> tables;

    private Schema(
            String name,
            LinkedHashMap<String, Table> definitions,
            LinkedHashMap<String, Table> tables
    ) {
        super(name);
        this.definitions = definitions;
        this.tables = tables;
    }

    public LinkedHashMap<String, Table> getDefinitions() {
        return definitions;
    }

    public LinkedHashMap<String, Table> getTables() {
        return tables;
    }

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
            this.definitions.put(table.name, table);
            return this;
        }

        public Builder table(Table table) {
            this.tables.put(table.name, table);
            return this;
        }

        public Schema build() {
            return new Schema(name, definitions, tables);
        }
    }
}
