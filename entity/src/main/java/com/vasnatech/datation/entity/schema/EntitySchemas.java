package com.vasnatech.datation.entity.schema;

import com.vasnatech.commons.schema.schema.AbstractSchema;

import java.util.LinkedHashMap;
import java.util.Map;

public class EntitySchemas extends AbstractSchema {
    final LinkedHashMap<String, EntitySchema> schemas;

    private EntitySchemas(LinkedHashMap<String, String> meta, LinkedHashMap<String, EntitySchema> schemas) {
        super("entities", "entity", meta);
        this.schemas = schemas;
    }

    public Map<String, EntitySchema> schemas() {
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
            this.schemas.put(schema.name(), schema);
            return this;
        }

        public EntitySchemas build() {
            return new EntitySchemas(meta, schemas);
        }
    }
}
