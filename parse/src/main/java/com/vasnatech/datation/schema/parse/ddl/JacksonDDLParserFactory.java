package com.vasnatech.datation.schema.parse.ddl;

import com.fasterxml.jackson.core.JsonFactory;

public class JacksonDDLParserFactory extends DDLParserFactory {

    @Override
    public DDLParser create() {
        return new JacksonDDLParser();
    }

    public DDLParser create(JsonFactory jsonFactory) {
        return new JacksonDDLParser(jsonFactory);
    }
}
