package com.vasnatech.datation.ddl;

import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.commons.schema.Module;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.datation.ddl.parse.DDLParser;
import com.vasnatech.datation.ddl.parse.DDLParserFactory;
import com.vasnatech.datation.ddl.parse.JacksonJsonDDLParserFactory;
import com.vasnatech.datation.ddl.schema.DDLSchemas;
import com.vasnatech.datation.ddl.validate.DDLValidator;
import com.vasnatech.datation.ddl.validate.DDLValidatorFactory;

import java.util.Map;

public final class DDLModule implements Module<DDLSchemas, DDLParser, DDLParserFactory, DDLValidator, DDLValidatorFactory> {

    private static final DDLModule INSTANCE = new DDLModule();
    public static DDLModule instance() {
        return INSTANCE;
    }

    private final Module<DDLSchemas, DDLParser, DDLParserFactory, DDLValidator, DDLValidatorFactory> values;

    private DDLModule() {
        values = Module.of(
            "ddl",
            DDLSchemas.class,
            Map.of(SupportedMediaTypes.JSON, new JacksonJsonDDLParserFactory()),
            new DDLValidatorFactory()
        );
    }

    @Override
    public String type() {
        return values.type();
    }

    @Override
    public Class<DDLSchemas> schemaClass() {
        return values.schemaClass();
    }

    @Override
    public Map<MediaType, DDLParserFactory> parserFactories() {
        return values.parserFactories();
    }

    @Override
    public DDLValidatorFactory validatorFactory() {
        return values.validatorFactory();
    }
}
