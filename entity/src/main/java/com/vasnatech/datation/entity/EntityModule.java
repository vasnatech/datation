package com.vasnatech.datation.entity;

import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.datation.Module;
import com.vasnatech.datation.SupportedMediaTypes;
import com.vasnatech.datation.entity.parse.EntityParser;
import com.vasnatech.datation.entity.parse.EntityParserFactory;
import com.vasnatech.datation.entity.parse.JacksonJsonEntityParserFactory;
import com.vasnatech.datation.entity.schema.EntitySchemas;
import com.vasnatech.datation.entity.validate.EntityValidator;
import com.vasnatech.datation.entity.validate.EntityValidatorFactory;

import java.util.Map;

public final class EntityModule implements Module<EntitySchemas, EntityParser, EntityParserFactory, EntityValidator, EntityValidatorFactory> {

    private static final EntityModule INSTANCE = new EntityModule();
    public static EntityModule instance() {
        return INSTANCE;
    }

    private final Module<EntitySchemas, EntityParser, EntityParserFactory, EntityValidator, EntityValidatorFactory> values;

    private EntityModule() {
        values = Module.of(
            "ddl",
            EntitySchemas.class,
            Map.of(SupportedMediaTypes.JSON, new JacksonJsonEntityParserFactory()),
            new EntityValidatorFactory()
        );
    }

    @Override
    public String type() {
        return values.type();
    }

    @Override
    public Class<EntitySchemas> schemaClass() {
        return values.schemaClass();
    }

    @Override
    public Map<MediaType, EntityParserFactory> parserFactories() {
        return values.parserFactories();
    }

    @Override
    public EntityValidatorFactory validatorFactory() {
        return values.validatorFactory();
    }
}
