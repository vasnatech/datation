package com.vasnatech.datation.ui.control.parse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.datation.ui.control.schema.UIControlSchema;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class JacksonJsonUIControlParser implements UIControlParser {

    final JsonFactory jsonFactory;

    public JacksonJsonUIControlParser(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Override
    public MediaType mediaType() {
        return SupportedMediaTypes.JSON;
    }
    @Override
    public UIControlSchema parse(InputStream in) throws IOException {
        return parse(jsonFactory.createParser(in));
    }

    public UIControlSchema parse(JsonParser parser) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            UIControlSchema.Builder schemaBuilder = UIControlSchema.builder();
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

    private void parseMeta(JsonParser parser, UIControlSchema.Builder schemaBuilder) throws IOException {
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
    public UIControlSchema continueParsing(JsonParser parser, Map<String, String> meta) throws IOException {
        UIControlSchema.Builder schemaBuilder = UIControlSchema.builder();
        schemaBuilder.meta(meta);
        parseSchema(parser, schemaBuilder);
        return schemaBuilder.build();
    }

    void parseSchema(JsonParser parser, UIControlSchema.Builder schemaBuilder) throws IOException {
        while (parser.currentToken() == JsonToken.FIELD_NAME) {
            schemaBuilder.control(parser.currentName(), parser.nextTextValue());
            parser.nextToken();
        }
    }

    @Override
    public UIControlSchema normalize(UIControlSchema schema) {
        return schema;
    }
}
