package com.vasnatech.datation.entity.parse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.datation.entity.schema.*;
import com.vasnatech.commons.schema.schema.Anchor;
import com.vasnatech.commons.schema.schema.Append;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

public class JacksonJsonEntityParser implements EntityParser {

    JsonFactory jsonFactory;

    public JacksonJsonEntityParser() {
        this(new JsonFactory());
    }

    public JacksonJsonEntityParser(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Override
    public MediaType mediaType() {
        return SupportedMediaTypes.JSON;
    }

    @Override
    public EntitySchemas parse(InputStream in) throws IOException {
        return parse(jsonFactory.createParser(in));
    }

    public EntitySchemas parse(JsonParser parser) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            EntitySchemas.Builder schemasBuilder = EntitySchemas.builder();
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("meta".equals(fieldName)) {
                    parseMeta(parser, schemasBuilder);
                } else {
                    EntitySchema.Builder schemaBuilder = EntitySchema.builder();
                    schemaBuilder.name(fieldName);
                    parseSchema(parser, schemaBuilder);
                    schemasBuilder.schema(schemaBuilder.build());
                }
                parser.nextToken();
            }
            return schemasBuilder.build();
        }
        return null;
    }

    private void parseMeta(JsonParser parser, EntitySchemas.Builder schemasBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                schemasBuilder.meta(parser.currentName(), parser.nextTextValue());
                parser.nextToken();
            }
        }
    }

    @Override
    public EntitySchemas continueParsing(JsonParser parser, Map<String, String> meta) throws IOException {
        EntitySchemas.Builder schemasBuilder = EntitySchemas.builder();
        schemasBuilder.meta(meta);
        while (parser.currentToken() == JsonToken.FIELD_NAME) {
            String fieldName = parser.currentName();

            EntitySchema.Builder schemaBuilder = EntitySchema.builder();
            schemaBuilder.name(fieldName);
            parseSchema(parser, schemaBuilder);
            schemasBuilder.schema(schemaBuilder.build());

            parser.nextToken();
        }
        return schemasBuilder.build();
    }

    void parseSchema(JsonParser parser, EntitySchema.Builder schemaBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                switch (fieldName) {
                    case "ddl"         -> parseSchemaDDL(parser, schemaBuilder);
                    case "definitions" -> parseEntities(parser, schemaBuilder, true);
                    case "entities"    -> parseEntities(parser, schemaBuilder, false);
                }
                parser.nextToken();
            }
        }
    }

    private void parseSchemaDDL(JsonParser parser, EntitySchema.Builder schemaBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            DDL.Schema.Builder ddlBuilder = DDL.Schema.builder();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("schema".equals(fieldName)) {
                    ddlBuilder.schema(parser.nextTextValue());
                }
                parser.nextToken();
            }
            schemaBuilder.ddl(ddlBuilder.build());
        }
    }

    private void parseEntities(JsonParser parser, EntitySchema.Builder schemaBuilder, boolean definitions) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                Entity.Builder entityBuilder = Entity.builder();
                entityBuilder.name(parser.currentName());
                parseEntity(parser, entityBuilder);
                if (definitions) {
                    schemaBuilder.definition(entityBuilder.build());
                } else {
                    schemaBuilder.entity(entityBuilder.build());
                }
                parser.nextToken();
            }
        }
    }

    private void parseEntity(JsonParser parser, Entity.Builder entityBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                switch (fieldName) {
                    case "ddl"      -> parseEntityDDL(parser, entityBuilder);
                    case "fields"   -> parseFields(parser, entityBuilder);
                    case "ids"      -> parseIds(parser, entityBuilder);
                    case "appends"  -> parseAppends(parser, entityBuilder);
                    case "inherits" -> parseInherits(parser, entityBuilder);
                }
                parser.nextToken();
            }
        }
    }

    private void parseEntityDDL(JsonParser parser, Entity.Builder entityBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            DDL.Table.Builder ddlBuilder = DDL.Table.builder();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("table".equals(fieldName)) {
                    ddlBuilder.table(parser.nextTextValue());
                }
                parser.nextToken();
            }
            entityBuilder.ddl(ddlBuilder.build());
        }
    }

    private void parseFields(JsonParser parser, Entity.Builder entityBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                Field.Builder fieldBuilder = Field.builder();
                fieldBuilder.name(parser.currentName());
                parseField(parser, fieldBuilder);
                entityBuilder.field(fieldBuilder.build());

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
                switch (fieldName) {
                    case "ddl"         -> parseFieldDDL(parser, fieldBuilder::ddl);
                    case "type"        -> fieldBuilder.type(FieldType.valueOf(parser.nextTextValue().toUpperCase(Locale.ENGLISH)));
                    case "item-type"   -> fieldBuilder.itemType(parser.nextTextValue());
                    case "fetch"       -> fieldBuilder.fetch(FieldFetch.valueOf(parser.nextTextValue().toUpperCase(Locale.ENGLISH)));
                    case "nullable"    -> fieldBuilder.nullable(parser.nextBooleanValue());
                    case "enum-values" -> parseEnumValues(parser, fieldBuilder);
                }
                parser.nextToken();
            }
        }
    }

    private void parseFieldDDL(JsonParser parser, Consumer<DDL.Column> consumer) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            DDL.Column.Builder ddlBuilder = DDL.Column.builder();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                switch (fieldName) {
                    case "column"          -> ddlBuilder.column(parser.nextTextValue());
                    case "relation"        -> ddlBuilder.type(RelationType.finByValue(parser.nextTextValue()));
                    case "table"           -> ddlBuilder.table(parser.nextTextValue());
                    case "columns"         -> parseFieldDDLColumns(parser, ddlBuilder, false);
                    case "inverse-table"   -> ddlBuilder.inverseTable(parser.nextTextValue());
                    case "inverse-columns" -> parseFieldDDLColumns(parser, ddlBuilder, true);
                }
                parser.nextToken();
            }
            consumer.accept(ddlBuilder.build());
        }
    }

    private void parseFieldDDLColumns(JsonParser parser, DDL.Column.Builder ddlBuilder, boolean inverse) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                if (inverse) {
                    ddlBuilder.inverseColumn(parser.currentName(), parser.nextTextValue());
                } else {
                    ddlBuilder.column(parser.currentName(), parser.nextTextValue());
                }
                parser.nextToken();
            }
        }
    }

    private void parseEnumValues(JsonParser parser, Field.Builder fieldBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String enumLiteralName = parser.currentName();
                parser.nextToken();
                if (parser.currentToken().isNumeric()) {
                    fieldBuilder.enumValue(enumLiteralName, parser.getIntValue());
                } else {
                    fieldBuilder.enumValue(enumLiteralName, parser.getText());
                }
                parser.nextToken();
            }
        }
    }

    private void parseIds(JsonParser parser, Entity.Builder entityBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_ARRAY) {
            parser.nextToken();
            while (parser.currentToken() != JsonToken.END_ARRAY) {
                entityBuilder.id(parser.getText());
                parser.nextToken();
            }
        }
    }

    private void parseAppends(JsonParser parser, Entity.Builder entityBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                Append.Builder appendBuilder = Append.builder();
                appendBuilder.name(parser.currentName());
                parser.nextToken();
                if (parser.currentToken() == JsonToken.START_OBJECT) {
                    parser.nextToken();
                    while (parser.currentToken() == JsonToken.FIELD_NAME) {
                        String fieldName = parser.currentName();
                        if ("anchor".equals(fieldName)) {
                            appendBuilder.anchor(Anchor.valueOf(parser.nextTextValue().toUpperCase(Locale.ENGLISH)));
                        }
                        parser.nextToken();
                    }
                    entityBuilder.append(appendBuilder.build());
                }
                parser.nextToken();
            }
        }
    }

    private void parseInherits(JsonParser parser, Entity.Builder entityBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            Entity.Inherits.Builder inheritsBuilder = Entity.Inherits.builder();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                switch (fieldName) {
                    case "base" -> inheritsBuilder.base(parser.nextTextValue());
                    case "ddl"  -> parseFieldDDL(parser, it -> inheritsBuilder.ddl((DDL.RelationColumn) it));
                }
                parser.nextToken();
            }
            entityBuilder.inherits(inheritsBuilder.build());
        }
    }
}
