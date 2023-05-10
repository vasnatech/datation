package com.vasnatech.datation;

import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.commons.serialize.MediaTypes;
import com.vasnatech.datation.parse.SchemaParser;
import com.vasnatech.datation.parse.SchemaParserFactory;
import com.vasnatech.datation.schema.Schema;
import com.vasnatech.datation.validate.SchemaValidator;
import com.vasnatech.datation.validate.SchemaValidatorFactory;

import java.util.HashMap;
import java.util.Map;

public class Modules {

    static Map<String, Module<?, ? ,? ,?, ?>> MODULES = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static
    <
        S extends Schema,
        SP extends SchemaParser<S>,
        SPF extends SchemaParserFactory<S, SP>,
        SV extends SchemaValidator<S>,
        SVF extends SchemaValidatorFactory<S, SV>
    >
    Module<S, SP, SPF, SV, SVF> get(String type) {
        return (Module<S, SP, SPF, SV, SVF>) MODULES.get(type);
    }

    public static
    <
        S extends Schema,
        SP extends SchemaParser<S>,
        SPF extends SchemaParserFactory<S, SP>,
        SV extends SchemaValidator<S>,
        SVF extends SchemaValidatorFactory<S, SV>
    >
    void add(Module<S, SP, SPF, SV, SVF> module) {
        MODULES.put(module.type(), module);
    }




    public static <S extends Schema, SP extends SchemaParser<S>, SPF extends SchemaParserFactory<S, SP>>
    SPF getSchemaParserFactoryByMediaType(String schemaType, MediaType mediaType) {
        Module<S, SP, SPF, ?, ?> module = Modules.get(schemaType);
        if (module == null) {
            throw new DatationException("Unsupported schema type " + schemaType);
        }
        SPF factory = module.parserFactories().get(mediaType);
        if (factory == null) {
            throw new DatationException("Unsupported media type " + mediaType.name() + " for schema type " + schemaType);
        }
        return factory;
    }


    public static <S extends Schema, SP extends SchemaParser<S>, SPF extends SchemaParserFactory<S, SP>>
    SPF getSchemaParserFactoryByFileExtension(String schemaType, String fileExtension) {
        MediaType mediaType = MediaTypes.getByFileExtension(fileExtension);
        if (mediaType == null) {
            throw new DatationException("Unsupported file extension " + fileExtension);
        }
        return getSchemaParserFactoryByMediaType(schemaType, mediaType);
    }

    public static <S extends Schema, SP extends SchemaParser<S>, SPF extends SchemaParserFactory<S, SP>>
    SPF getSchemaParserFactoryByMimeType(String schemaType, String mimeType) {
        MediaType mediaType = MediaTypes.getByMimeType(mimeType);
        if (mediaType == null) {
            throw new DatationException("Unsupported mime type " + mimeType);
        }
        return getSchemaParserFactoryByMediaType(schemaType, mediaType);
    }


    public static <S extends Schema, SV extends SchemaValidator<S>, SVF extends SchemaValidatorFactory<S, SV>>
    SVF getSchemaValidatorFactory(String schemaType) {
        Module<S, ?, ?, SV, SVF> module = Modules.get(schemaType);
        if (module == null) {
            throw new DatationException("Unsupported schema type " + schemaType);
        }

        SVF factory = module.validatorFactory();
        if (factory == null) {
            throw new DatationException("Unsupported validator for schema type " + schemaType);
        }
        return factory;
    }
}
