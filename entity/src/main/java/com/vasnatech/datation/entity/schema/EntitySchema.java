package com.vasnatech.datation.entity.schema;

import com.vasnatech.datation.schema.Node;

import java.util.LinkedHashMap;

public class EntitySchema extends Node {

    final LinkedHashMap<String, Entity> definitions;
    final LinkedHashMap<String, Entity> tables;

    private EntitySchema(
            String name,
            LinkedHashMap<String, Entity> definitions,
            LinkedHashMap<String, Entity> tables
    ) {
        super(name);
        this.definitions = definitions;
        this.tables = tables;
    }

    public LinkedHashMap<String, Entity> getDefinitions() {
        return definitions;
    }

    public LinkedHashMap<String, Entity> getTables() {
        return tables;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        LinkedHashMap<String, Entity> definitions = new LinkedHashMap<>();
        LinkedHashMap<String, Entity> tables = new LinkedHashMap<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder definition(Entity entity) {
            this.definitions.put(entity.getName(), entity);
            return this;
        }

        public Builder entity(Entity entity) {
            this.tables.put(entity.getName(), entity);
            return this;
        }

        public EntitySchema build() {
            return new EntitySchema(name, definitions, tables);
        }
    }
}
