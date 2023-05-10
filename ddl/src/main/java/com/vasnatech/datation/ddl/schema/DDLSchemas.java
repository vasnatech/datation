package com.vasnatech.datation.ddl.schema;

import com.vasnatech.datation.schema.Node;
import com.vasnatech.datation.schema.Schema;

import java.util.LinkedHashMap;
import java.util.Map;

public class DDLSchemas extends Node implements Schema {
    final LinkedHashMap<String, String> meta;
    final LinkedHashMap<String, DDLSchema> schemas;

    private DDLSchemas(LinkedHashMap<String, String> meta, LinkedHashMap<String, DDLSchema> schemas) {
        super("db");
        this.meta = meta;
        this.schemas = schemas;
    }

    @Override
    public String type() {
        return "ddl";
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public Map<String, DDLSchema> getSchemas() {
        return schemas;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        LinkedHashMap<String, String> meta = new LinkedHashMap<>();
        LinkedHashMap<String, DDLSchema> schemas = new LinkedHashMap<>();

        public Builder meta(String metaKey, String metaValue) {
            this.meta.put(metaKey, metaValue);
            return this;
        }

        public Builder meta(Map<String, String> meta) {
            this.meta.putAll(meta);
            return this;
        }

        public Builder schema(DDLSchema schema) {
            this.schemas.put(schema.getName(), schema);
            return this;
        }

        public DDLSchemas build() {
            return new DDLSchemas(meta, schemas);
        }
    }
}
