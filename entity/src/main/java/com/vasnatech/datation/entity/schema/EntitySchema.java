package com.vasnatech.datation.entity.schema;

import com.vasnatech.commons.schema.schema.Node;

import java.util.LinkedHashMap;

public record EntitySchema(
        String name,
        DDL.Schema ddl,
        LinkedHashMap<String, Entity> definitions,
        LinkedHashMap<String, Entity> entities
) implements Node {

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
            this.definitions.put(entity.name(), entity);
            return this;
        }

        public Builder entity(Entity entity) {
            this.entities.put(entity.name(), entity);
            return this;
        }

        public EntitySchema build() {
            return new EntitySchema(name, ddl, definitions, entities);
        }
    }
}
