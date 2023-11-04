package com.vasnatech.datation.entity.schema;

import com.vasnatech.commons.schema.schema.Node;
import com.vasnatech.commons.schema.schema.Schema;

import java.util.LinkedHashMap;
import java.util.Map;

public class EntitySchemas extends Node implements Schema {
    final LinkedHashMap<String, String> meta;
    final LinkedHashMap<String, EntitySchema> schemas;

    private EntitySchemas(LinkedHashMap<String, String> meta, LinkedHashMap<String, EntitySchema> schemas) {
        super("entities");
        this.meta = meta;
        this.schemas = schemas;
    }

    @Override
    public String type() {
        return "entity";
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public Map<String, EntitySchema> getSchemas() {
        return schemas;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        LinkedHashMap<String, String> meta = new LinkedHashMap<>();
        LinkedHashMap<String, EntitySchema> schemas = new LinkedHashMap<>();

        public Builder meta(String metaKey, String metaValue) {
            this.meta.put(metaKey, metaValue);
            return this;
        }

        public Builder meta(Map<String, String> meta) {
            this.meta.putAll(meta);
            return this;
        }

        public Builder schema(EntitySchema schema) {
            this.schemas.put(schema.getName(), schema);
            return this;
        }

        public EntitySchemas build() {
            return new EntitySchemas(meta, schemas);
        }
    }
}
