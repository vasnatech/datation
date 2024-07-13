package com.vasnatech.datation.ui.component.parse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.datation.ui.component.schema.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JacksonJsonUIComponentParser implements UIComponentParser {

    final JsonFactory jsonFactory;

    public JacksonJsonUIComponentParser(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Override
    public MediaType mediaType() {
        return SupportedMediaTypes.JSON;
    }
    @Override
    public UIComponentSchema parse(InputStream in) throws IOException {
        return parse(jsonFactory.createParser(in));
    }

    public UIComponentSchema parse(JsonParser parser) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            UIComponentSchema.Builder schemaBuilder = UIComponentSchema.builder();
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

    private void parseMeta(JsonParser parser, UIComponentSchema.Builder schemaBuilder) throws IOException {
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
    public UIComponentSchema continueParsing(JsonParser parser, Map<String, String> meta) throws IOException {
        UIComponentSchema.Builder schemaBuilder = UIComponentSchema.builder();
        schemaBuilder.meta(meta);
        parseSchema(parser, schemaBuilder);
        return schemaBuilder.build();
    }

    void parseSchema(JsonParser parser, UIComponentSchema.Builder schemaBuilder) throws IOException {
        while (parser.currentToken() == JsonToken.FIELD_NAME) {
            String fieldName = parser.currentName();
            if ("properties".equals(fieldName)) {
                parseProperties(parser, schemaBuilder);
            } else if ("events".equals(fieldName)) {
                parseEvents(parser, schemaBuilder);
            } else if ("controls".equals(fieldName)) {
                parseControls(parser, schemaBuilder);
            } else if ("containers".equals(fieldName)) {
                parseContainers(parser, schemaBuilder);
            }
            parser.nextToken();
        }
    }

    void parseProperties(JsonParser parser, UIComponentSchema.Builder schemaBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                Property.Builder propertyBuilder = Property.builder();
                String fieldName = parser.currentName();
                propertyBuilder.name(fieldName);
                parseProperty(parser, propertyBuilder);
                schemaBuilder.property(propertyBuilder.build());
                parser.nextToken();
            }
        }
    }

    void parseProperty(JsonParser parser, Property.Builder propertyBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("type".equals(fieldName)) {
                    parsePropertyType(parser, propertyBuilder);
                } else if ("default".equals(fieldName)) {
                    parser.nextToken();
                    Object value = parseValue(parser);
                    propertyBuilder.defaultValue(value == null ? null : value.toString());
                } else if ("title".equals(fieldName)) {
                    propertyBuilder.title(parser.nextTextValue());
                }
                parser.nextToken();
            }
        }
    }

    void parsePropertyType(JsonParser parser, Property.Builder propertyBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_ARRAY) {
            List<?> enumLiteralNames = parseArray(parser);
            Set<String> enumLiterals = enumLiteralNames.stream().map(String::valueOf).collect(Collectors.toCollection(LinkedHashSet::new));
            propertyBuilder.propertyType(enumLiterals);
        } else {
            propertyBuilder.propertyType(parser.getValueAsString());
        }
    }

    void parseEvents(JsonParser parser, UIComponentSchema.Builder schemaBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                Event.Builder eventBuilder = Event.builder();
                String fieldName = parser.currentName();
                eventBuilder.name(fieldName);
                parseEvent(parser, eventBuilder);
                schemaBuilder.event(eventBuilder.build());
                parser.nextToken();
            }
        }
    }

    void parseEvent(JsonParser parser, Event.Builder eventBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                eventBuilder.property(parser.currentName(), parser.nextTextValue());
                parser.nextToken();
            }
        }
    }

    void parseControls(JsonParser parser, UIComponentSchema.Builder schemaBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                Control.Builder controlBuilder = Control.builder();
                String fieldName = parser.currentName();
                controlBuilder.name(fieldName);
                parseControl(parser, controlBuilder);
                schemaBuilder.control(controlBuilder.build());
                parser.nextToken();
            }
        }
    }

    void parseControl(JsonParser parser, Control.Builder controlBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("properties".equals(fieldName)) {
                    parser.nextToken();
                    List<?> propertyNames = parseArray(parser);
                    propertyNames.stream().map(String::valueOf).forEach(controlBuilder::property);
                } else if ("events".equals(fieldName)) {
                    parser.nextToken();
                    List<?> eventNames = parseArray(parser);
                    eventNames.stream().map(String::valueOf).forEach(controlBuilder::event);
                }
                parser.nextToken();
            }
        }
    }

    void parseContainers(JsonParser parser, UIComponentSchema.Builder schemaBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                Container.Builder containerBuilder = Container.builder();
                String fieldName = parser.currentName();
                containerBuilder.name(fieldName);
                parseContainer(parser, containerBuilder);
                schemaBuilder.container(containerBuilder.build());
                parser.nextToken();
            }
        }
    }

    void parseContainer(JsonParser parser, Container.Builder containerBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("properties".equals(fieldName)) {
                    parser.nextToken();
                    List<?> propertyNames = parseArray(parser);
                    propertyNames.stream().map(String::valueOf).forEach(containerBuilder::property);
                } else if ("child-properties".equals(fieldName)) {
                    parser.nextToken();
                    List<?> childPropertyNames = parseArray(parser);
                    childPropertyNames.stream().map(String::valueOf).forEach(containerBuilder::childProperty);
                } else if ("events".equals(fieldName)) {
                    parser.nextToken();
                    List<?> eventNames = parseArray(parser);
                    eventNames.stream().map(String::valueOf).forEach(containerBuilder::event);
                }
                parser.nextToken();
            }
        }
    }

    Object parseValue(JsonParser parser) throws IOException {
        JsonToken currentToken = parser.currentToken();
        return switch (currentToken) {
            case NOT_AVAILABLE -> null;
            case FIELD_NAME -> null;
            case VALUE_NULL -> null;
            case START_OBJECT -> parseObject(parser);
            case END_OBJECT -> null;
            case START_ARRAY -> parseArray(parser);
            case END_ARRAY -> null;
            case VALUE_EMBEDDED_OBJECT -> parser.currentValue();
            case VALUE_NUMBER_INT -> parser.getIntValue();
            case VALUE_NUMBER_FLOAT -> parser.getFloatValue();
            case VALUE_FALSE -> false;
            case VALUE_TRUE -> true;
            case VALUE_STRING -> parser.getText();
        };
    }

    Map<String, ?> parseObject(JsonParser parser) throws IOException {
        Map<String, Object> map = new LinkedHashMap<>();
        parser.nextToken();
        while (parser.currentToken() == JsonToken.FIELD_NAME) {
            String fieldName = parser.currentName();
            parser.nextToken();
            map.put(fieldName, parseValue(parser));
            parser.nextToken();
        }
        return map;
    }

    List<?> parseArray(JsonParser parser) throws IOException {
        List<Object> list = new ArrayList<>();
        parser.nextToken();
        while (parser.currentToken() != JsonToken.END_ARRAY) {
            list.add(parseValue(parser));
            parser.nextToken();
        }
        return list;
    }


    @Override
    public UIComponentSchema normalize(UIComponentSchema schema) {
        schema.controls().values().stream()
                .map(Control::properties)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .forEach(entry -> entry.setValue(schema.properties().get(entry.getKey())));
        schema.controls().values().stream()
                .map(Control::events)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .forEach(entry -> entry.setValue(schema.events().get(entry.getKey())));

        schema.containers().values().stream()
                .map(Container::properties)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .forEach(entry -> entry.setValue(schema.properties().get(entry.getKey())));
        schema.containers().values().stream()
                .map(Container::childProperties)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .forEach(entry -> entry.setValue(schema.properties().get(entry.getKey())));
        schema.containers().values().stream()
                .map(Container::events)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .forEach(entry -> entry.setValue(schema.events().get(entry.getKey())));
        return schema;
    }
}
