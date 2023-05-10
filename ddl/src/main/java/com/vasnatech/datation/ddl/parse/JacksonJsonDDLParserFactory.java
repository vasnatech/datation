package com.vasnatech.datation.ddl.parse;

import com.fasterxml.jackson.core.JsonFactory;

public class JacksonJsonDDLParserFactory extends DDLParserFactory {

    @Override
    public DDLParser create(String version) {
        return create(new JsonFactory());
    }

    public DDLParser create(JsonFactory jsonFactory) {
        return new JacksonJsonDDLParser(jsonFactory);
    }
}
