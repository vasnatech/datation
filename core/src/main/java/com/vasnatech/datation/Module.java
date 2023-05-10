package com.vasnatech.datation;

import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.datation.parse.SchemaParser;
import com.vasnatech.datation.parse.SchemaParserFactory;
import com.vasnatech.datation.schema.Schema;
import com.vasnatech.datation.validate.SchemaValidator;
import com.vasnatech.datation.validate.SchemaValidatorFactory;

import java.util.Map;

public interface Module<
        S extends Schema,
        SP extends SchemaParser<S>,
        SPF extends SchemaParserFactory<S, SP>,
        SV extends SchemaValidator<S>,
        SVF extends SchemaValidatorFactory<S, SV>
> {

    String type();
    Class<S> schemaClass();
    Map<MediaType, SPF> parserFactories();
    SVF validatorFactory();

    static <
        S extends Schema,
        SP extends SchemaParser<S>,
        SPF extends SchemaParserFactory<S, SP>,
        SV extends SchemaValidator<S>,
        SVF extends SchemaValidatorFactory<S, SV>
        >
    Module<S, SP, SPF, SV, SVF> of(
            String type,
            Class<S> schemaClass,
            Map<MediaType, SPF> parserFactoryClasses,
            SVF validatorFactoryClass
    ) {
        return new DefaultModelInfo<>(type, schemaClass, parserFactoryClasses, validatorFactoryClass);
    }

    record DefaultModelInfo<
            S extends Schema,
            SP extends SchemaParser<S>,
            SPF extends SchemaParserFactory<S, SP>,
            SV extends SchemaValidator<S>,
            SVF extends SchemaValidatorFactory<S, SV>
    > (
            String type,
            Class<S> schemaClass,
            Map<MediaType, SPF> parserFactories,
            SVF validatorFactory
    ) implements Module<S, SP, SPF, SV, SVF>
    {}
}
