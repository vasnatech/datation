package com.vasnatech.datation.schema.parse;

import com.fasterxml.jackson.core.JsonFactory;

public class JacksonParserFactory extends ParserFactory {

    @Override
    public Parser create() {
        return new JacksonParser();
    }

    public Parser create(JsonFactory jsonFactory) {
        return new JacksonParser(jsonFactory);
    }
}
