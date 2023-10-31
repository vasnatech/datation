package com.vasnatech.datation.ui.binding.parse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.datation.SupportedMediaTypes;
import com.vasnatech.datation.ui.binding.schema.Field;
import com.vasnatech.datation.ui.binding.schema.UIBindingSchema;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class JacksonJsonUIBindingParser implements UIBindingParser {

    final JsonFactory jsonFactory;

    public JacksonJsonUIBindingParser(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Override
    public MediaType mediaType() {
        return SupportedMediaTypes.JSON;
    }

    @Override
    public UIBindingSchema parse(InputStream in) throws IOException {
        return parse(jsonFactory.createParser(in));
    }

    public UIBindingSchema parse(JsonParser parser) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            UIBindingSchema.Builder schemaBuilder = UIBindingSchema.builder();
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("meta".equals(fieldName)) {
                    parseMeta(parser, schemaBuilder);
                } else {
                    parseSchema(parser, schemaBuilder);
                }
                parser.nextToken();
            }
            return schemaBuilder.build();
        }
        return null;
    }

    private void parseMeta(JsonParser parser, UIBindingSchema.Builder schemaBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                schemaBuilder.meta(parser.currentName(), parser.nextTextValue());
                parser.nextToken();
            }
        }
    }

    @Override
    public UIBindingSchema continueParsing(JsonParser parser, Map<String, String> meta) throws IOException {
        UIBindingSchema.Builder schemaBuilder = UIBindingSchema.builder();
        schemaBuilder.meta(meta);
        parseSchema(parser, schemaBuilder);
        return schemaBuilder.build();
    }

    void parseSchema(JsonParser parser, UIBindingSchema.Builder schemaBuilder) throws IOException {
        while (parser.currentToken() == JsonToken.FIELD_NAME) {
            String fieldName = parser.currentName();
            if ("name".equals(fieldName)) {
                schemaBuilder.name(parser.nextTextValue());
            } else if ("fields".equals(fieldName)) {
                parseFields(parser, schemaBuilder);
            }
            parser.nextToken();
        }
    }

    void parseFields(JsonParser parser, UIBindingSchema.Builder schemaBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                Field.Builder fieldBuilder = Field.builder();
                String fieldName = parser.currentName();
                fieldBuilder.name(fieldName);
                parseField(parser, fieldBuilder);
                schemaBuilder.field(fieldBuilder.build());

                parser.nextToken();
            }
        }
    }

    private void parseField(JsonParser parser, Field.Builder fieldBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("control".equals(fieldName)) {
                    fieldBuilder.control(parser.nextTextValue());
                } else if ("binding".equals(fieldName)) {
                    fieldBuilder.binding(parser.nextTextValue());
                } else if ("getter".equals(fieldName)) {
                    fieldBuilder.getter(parser.nextTextValue());
                } else if ("setter".equals(fieldName)) {
                    fieldBuilder.setter(parser.nextTextValue());
                } else if ("data-source".equals(fieldName)) {
                    fieldBuilder.dataSource(parser.nextTextValue());
                }
                parser.nextToken();
            }
        }
    }

    @Override
    public UIBindingSchema normalize(UIBindingSchema schema) {
        return schema;
    }
}
