package com.vasnatech.datation.ui.design.parse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.datation.ui.component.schema.*;
import com.vasnatech.datation.ui.design.schema.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class JacksonJsonUIDesignParser implements UIDesignParser {

    final UIComponentFactory componentFactory;
    final JsonFactory jsonFactory;

    public JacksonJsonUIDesignParser(UIComponentFactory componentFactory, JsonFactory jsonFactory) {
        this.componentFactory = componentFactory;
        this.jsonFactory = jsonFactory;
    }

    @Override
    public MediaType mediaType() {
        return SupportedMediaTypes.JSON;
    }

    @Override
    public UIDesignSchema parse(InputStream in) throws IOException {
        return parse(jsonFactory.createParser(in));
    }

    public UIDesignSchema parse(JsonParser parser) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            UIDesignSchema.Builder schemaBuilder = UIDesignSchema.builder();
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

    private void parseMeta(JsonParser parser, UIDesignSchema.Builder schemaBuilder) throws IOException {
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
    public UIDesignSchema continueParsing(JsonParser parser, Map<String, String> meta) throws IOException {
        UIDesignSchema.Builder schemaBuilder = UIDesignSchema.builder();
        schemaBuilder.meta(meta);
        parseSchema(parser, schemaBuilder);
        return schemaBuilder.build();
    }

    void parseSchema(JsonParser parser, UIDesignSchema.Builder schemaBuilder) throws IOException {
        while (parser.currentToken() == JsonToken.FIELD_NAME) {
            parseComponent(parser, schemaBuilder::element);
        }
    }

    void parseComponent(JsonParser parser, Consumer<Element> elementConsumer) throws IOException {
        if (parser.currentToken() == JsonToken.FIELD_NAME) {
            String fieldName = parser.currentName();
            String[] fieldNameAndType = fieldName.split("::");
            if (fieldNameAndType.length != 3) {
                throw new IOException("Invalid element " + fieldName);
            }
            Element.Builder elementBuilder = Element.builder();
            elementBuilder.name(fieldNameAndType[0]);
            if ("control".equals(fieldNameAndType[1])) {
                parseControl(parser, elementBuilder, componentFactory.control(fieldNameAndType[2]));
            } else if ("container".equals(fieldNameAndType[1])) {
                parseContainer(parser, elementBuilder, componentFactory.container(fieldNameAndType[2]));
            }
            elementConsumer.accept(elementBuilder.build());
            parser.nextToken();
        }
    }

    void parseControl(JsonParser parser, Element.Builder elementBuilder, Control control) throws IOException {
        elementBuilder.type(control);
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                parser.nextToken();
                Object fieldValue = parseValue(parser);
                if (control.properties().containsKey(fieldName))
                    elementBuilder.property(fieldName, fieldValue);
                else
                    elementBuilder.containerProperty(fieldName, fieldValue);
                parser.nextToken();
            }
        }
    }

    void parseContainer(JsonParser parser, Element.Builder elementBuilder, Container container) throws IOException {
        elementBuilder.type(container);
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("children".equals(fieldName)) {
                    parser.nextToken();
                    if (parser.currentToken() == JsonToken.START_OBJECT) {
                        parser.nextToken();
                        while (parser.currentToken() == JsonToken.FIELD_NAME) {
                            parseComponent(parser, elementBuilder::child);
                        }
                    }
                } else {
                    parser.nextToken();
                    Object fieldValue = parseValue(parser);
                    if (container.properties().containsKey(fieldName))
                        elementBuilder.property(fieldName, fieldValue);
                    else
                        elementBuilder.containerProperty(fieldName, fieldValue);
                    parser.nextToken();
                }
            }
        }
    }

    Object parseValue(JsonParser parser) throws IOException {
        JsonToken currentToken = parser.currentToken();
        return switch (currentToken) {
            case NOT_AVAILABLE -> null;
            case FIELD_NAME -> null;
            case VALUE_NULL -> null;
            case START_OBJECT ->  parseObject(parser);
            case END_OBJECT -> null;
            case START_ARRAY -> parseArray(parser);
            case END_ARRAY -> null;
            case VALUE_EMBEDDED_OBJECT -> parser.getCurrentValue();
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
    public UIDesignSchema normalize(UIDesignSchema schema) {
        normalizeElement(null, schema.getRoot());
        return schema;
    }

    void normalizeElement(Element parent, Element child) {
        if (child.type() == null) {
            return;
        }

        child.properties().entrySet().forEach(entry -> {
            Property property = child.type().properties().get(entry.getKey());
            if (property == null) {
                entry.setValue(null);
            } else {
                Object normalizedValue = normalizeValue(property, entry.getValue());
                entry.setValue(normalizedValue);
            }
        });

        child.containerProperties().entrySet().forEach(entry -> {
            if (parent != null && (parent.type() instanceof Container container)) {
                Property property = container.childProperties().get(entry.getKey());
                if (property == null) {
                    entry.setValue(null);
                } else {
                    Object normalizedValue = normalizeValue(property, entry.getValue());
                    entry.setValue(normalizedValue);
                }
            } else {
                entry.setValue(null);
            }
        });

        if (child.type().isContainer()) {
            child.children().values().forEach(element -> normalizeElement(child, element));
        } else {
            child.children().clear();
        }
    }

    Object normalizeValue(Property property, Object value) {
        if (value == null) {
            return property.defaultValue();
        }
        if (property.type() instanceof EnumPropertyType enumPropertyType) {
            String stringValue = value.toString();
            return enumPropertyType.enumLiterals().contains(stringValue) ? stringValue : property.defaultValue();
        } else if (property.type() instanceof BasicPropertyType basicPropertyType) {
            return switch (basicPropertyType) {
                case BOOLEAN      -> (value instanceof Boolean booleanValue) ? booleanValue : Boolean.valueOf(value.toString());
                case INTEGER      -> (value instanceof Number numberValue) ? numberValue.intValue() : Integer.parseInt(value.toString());
                case FLOAT        -> (value instanceof Number numberValue) ? numberValue.floatValue(): Float.parseFloat(value.toString());
                case STRING       -> value.toString();
                case STRING_ARRAY -> Stream.of(value.toString().split(",")).map(String::trim).toArray(String[]::new);
                case INSETS       -> Insets.valueOf(value.toString());
                case RECTANGLE    -> Rectangle.valueOf(value.toString());
                case DIMENSION    -> Dimension.valueOf(value.toString());
                case URI          -> URI.create(value.toString());
            };
        }
        return property.defaultValue();
    }
}
