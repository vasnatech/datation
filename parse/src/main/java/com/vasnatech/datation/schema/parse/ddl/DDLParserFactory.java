package com.vasnatech.datation.schema.parse.ddl;

public abstract class DDLParserFactory {

    static DDLParserFactory instance;

    public static DDLParserFactory instance() {
        if (instance == null) {
            instance(new JacksonDDLParserFactory());
        }
        return instance;
    }

    public static void instance(DDLParserFactory instance) {
        DDLParserFactory.instance = instance;
    }

    public abstract DDLParser create();
}
