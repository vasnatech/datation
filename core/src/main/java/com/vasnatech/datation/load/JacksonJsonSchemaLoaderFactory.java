package com.vasnatech.datation.load;

import com.fasterxml.jackson.core.JsonFactory;

import java.util.Map;

public class JacksonJsonSchemaLoaderFactory extends SchemaLoaderFactory {

    @Override
    public SchemaLoader create(Map<String, ?> config) {
        return new JacksonJsonSchemaLoader(config);
    }

    public SchemaLoader create(JsonFactory jsonFactory, Map<String, ?> config) {
        return new JacksonJsonSchemaLoader(jsonFactory, config);
    }
}
