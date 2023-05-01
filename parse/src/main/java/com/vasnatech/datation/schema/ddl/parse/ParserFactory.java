package com.vasnatech.datation.schema.ddl.parse;

public abstract class ParserFactory {

    static ParserFactory instance;

    public static ParserFactory instance() {
        if (instance == null) {
            instance(new JacksonParserFactory());
        }
        return instance;
    }

    public static void instance(ParserFactory instance) {
        ParserFactory.instance = instance;
    }

    public abstract Parser create();
}
