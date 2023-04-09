package com.vasnatech.datation.schema;

import java.util.LinkedHashMap;
import java.util.Map;

public class Schemas extends Node {
    final LinkedHashMap<String, String> meta;
    final LinkedHashMap<String, Schema> schemas;

    private Schemas(LinkedHashMap<String, String> meta, LinkedHashMap<String, Schema> schemas) {
        super("db");
        this.meta = meta;
        this.schemas = schemas;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public Map<String, Schema> getSchemas() {
        return schemas;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        LinkedHashMap<String, String> meta = new LinkedHashMap<>();
        LinkedHashMap<String, Schema> schemas = new LinkedHashMap<>();

        public Builder meta(String metaKey, String metaValue) {
            this.meta.put(metaKey, metaValue);
            return this;
        }

        public Builder meta(Map<String, String> meta) {
            this.meta.putAll(meta);
            return this;
        }

        public Builder schema(Schema schema) {
            this.schemas.put(schema.name, schema);
            return this;
        }

        public Schemas build() {
            return new Schemas(meta, schemas);
        }
    }
}
