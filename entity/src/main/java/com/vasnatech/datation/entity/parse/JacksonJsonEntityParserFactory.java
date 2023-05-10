package com.vasnatech.datation.entity.parse;

import com.fasterxml.jackson.core.JsonFactory;

public class JacksonJsonEntityParserFactory extends EntityParserFactory {

    @Override
    public EntityParser create(String version) {
        return new JacksonJsonEntityParser();
    }

    public EntityParser create(JsonFactory jsonFactory) {
        return new JacksonJsonEntityParser(jsonFactory);
    }
}
