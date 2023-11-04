package com.vasnatech.datation.ddl.schema;

import com.vasnatech.commons.schema.schema.Node;
import com.vasnatech.commons.schema.schema.Schema;

import java.util.LinkedHashMap;

public class DDLSchema extends Node implements Schema {

    final LinkedHashMap<String, Table> definitions;
    final LinkedHashMap<String, Table> tables;

    private DDLSchema(
            String name,
            LinkedHashMap<String, Table> definitions,
            LinkedHashMap<String, Table> tables
    ) {
        super(name);
        this.definitions = definitions;
        this.tables = tables;
    }

    @Override
    public String type() {
        return "ddl";
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
            this.definitions.put(table.getName(), table);
            return this;
        }

        public Builder table(Table table) {
            this.tables.put(table.getName(), table);
            return this;
        }

        public DDLSchema build() {
            return new DDLSchema(name, definitions, tables);
        }
    }
}
