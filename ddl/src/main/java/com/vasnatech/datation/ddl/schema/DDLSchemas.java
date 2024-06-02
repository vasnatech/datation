package com.vasnatech.datation.ddl.schema;

import com.vasnatech.commons.schema.schema.AbstractSchema;

import java.util.LinkedHashMap;
import java.util.Map;

public class DDLSchemas extends AbstractSchema {
    final LinkedHashMap<String, DDLSchema> schemas;

    private DDLSchemas(LinkedHashMap<String, String> meta, LinkedHashMap<String, DDLSchema> schemas) {
        super("db", "ddl", meta);
        this.schemas = schemas;
    }

    public Map<String, DDLSchema> schemas() {
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
            this.schemas.put(schema.name(), schema);
            return this;
        }

        public DDLSchemas build() {
            return new DDLSchemas(meta, schemas);
        }
    }
}
