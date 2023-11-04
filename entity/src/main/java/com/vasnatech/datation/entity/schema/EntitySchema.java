package com.vasnatech.datation.entity.schema;

import com.vasnatech.commons.schema.schema.Node;

import java.util.LinkedHashMap;

public class EntitySchema extends Node {

    final DDL.Schema ddl;
    final LinkedHashMap<String, Entity> definitions;
    final LinkedHashMap<String, Entity> entities;

    private EntitySchema(
            String name,
            DDL.Schema ddl,
            LinkedHashMap<String, Entity> definitions,
            LinkedHashMap<String, Entity> entities
    ) {
        super(name);
        this.ddl = ddl;
        this.definitions = definitions;
        this.entities = entities;
    }

    public DDL.Schema getDDL() {
        return ddl;
    }

    public LinkedHashMap<String, Entity> getDefinitions() {
        return definitions;
    }

    public LinkedHashMap<String, Entity> getEntities() {
        return entities;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        DDL.Schema ddl;
        LinkedHashMap<String, Entity> definitions = new LinkedHashMap<>();
        LinkedHashMap<String, Entity> entities = new LinkedHashMap<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder ddl(DDL.Schema ddl) {
            this.ddl = ddl;
            return this;
        }

        public Builder definition(Entity entity) {
            this.definitions.put(entity.getName(), entity);
            return this;
        }

        public Builder entity(Entity entity) {
            this.entities.put(entity.getName(), entity);
            return this;
        }

        public EntitySchema build() {
            return new EntitySchema(name, ddl, definitions, entities);
        }
    }
}
