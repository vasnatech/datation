package com.vasnatech.datation.entity.parse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.datation.SupportedMediaTypes;
import com.vasnatech.datation.entity.schema.EntitySchemas;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class JacksonJsonEntityParser implements EntityParser {

    JsonFactory jsonFactory;

    public JacksonJsonEntityParser() {
        this(new JsonFactory());
    }

    public JacksonJsonEntityParser(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Override
    public MediaType mediaType() {
        return SupportedMediaTypes.JSON;
    }

    @Override
    public EntitySchemas parse(InputStream in) throws IOException {
        return null;
    }

    @Override
    public EntitySchemas continueParsing(JsonParser parser, Map<String, String> meta) throws IOException {
        return null;
    }

    @Override
    public EntitySchemas normalize(EntitySchemas schema) {
        return null;
    }
}
