package com.vasnatech.datation.ddl.parse;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.datation.SupportedMediaTypes;
import com.vasnatech.datation.ddl.schema.*;
import com.vasnatech.datation.schema.Anchor;
import com.vasnatech.datation.schema.Append;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

public class JacksonJsonDDLParser implements DDLParser {

    JsonFactory jsonFactory;

    public JacksonJsonDDLParser(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Override
    public MediaType mediaType() {
        return SupportedMediaTypes.JSON;
    }

    public DDLSchemas parse(InputStream in) throws IOException {
        return parse(jsonFactory.createParser(in));
    }

    public DDLSchemas parse(JsonParser parser) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            DDLSchemas.Builder schemasBuilder = DDLSchemas.builder();
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("meta".equals(fieldName)) {
                    parseMeta(parser, schemasBuilder);
                } else {
                    DDLSchema.Builder schemaBuilder = DDLSchema.builder();
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

    private void parseMeta(JsonParser parser, DDLSchemas.Builder databaseBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                databaseBuilder.meta(parser.currentName(), parser.nextTextValue());
                parser.nextToken();
            }
        }
    }

    public DDLSchemas continueParsing(JsonParser parser, Map<String, String> meta) throws IOException {
        DDLSchemas.Builder schemasBuilder = DDLSchemas.builder();
        schemasBuilder.meta(meta);
        while (parser.currentToken() == JsonToken.FIELD_NAME) {
            String fieldName = parser.currentName();

            DDLSchema.Builder schemaBuilder = DDLSchema.builder();
            schemaBuilder.name(fieldName);
            parseSchema(parser, schemaBuilder);
            schemasBuilder.schema(schemaBuilder.build());

            parser.nextToken();
        }
        return schemasBuilder.build();
    }

    void parseSchema(JsonParser parser, DDLSchema.Builder schemaBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("definitions".equals(fieldName)) {
                    parseTables(parser, schemaBuilder, true);
                } else if ("tables".equals(fieldName)) {
                    parseTables(parser, schemaBuilder, false);
                }
                parser.nextToken();
            }
        }
    }

    private void parseTables(JsonParser parser, DDLSchema.Builder schemaBuilder, boolean definitions) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                Table.Builder tableBuilder = Table.builder();
                tableBuilder.name(parser.currentName());
                parseTable(parser, tableBuilder);
                if (definitions) {
                    schemaBuilder.definition(tableBuilder.build());
                } else {
                    schemaBuilder.table(tableBuilder.build());
                }
                parser.nextToken();
            }
        }
    }

    private void parseTable(JsonParser parser, Table.Builder tableBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("columns".equals(fieldName)) {
                    parseColumns(parser, tableBuilder);
                } else if ("primary-key".equals(fieldName)) {
                    parsePrimaryKey(parser, tableBuilder);
                } else if ("indexes".equals(fieldName)) {
                    parseIndexes(parser, tableBuilder);
                } else if ("foreign-keys".equals(fieldName)) {
                    parseForeignKeys(parser, tableBuilder);
                } else if ("appends".equals(fieldName)) {
                    parseAppends(parser, tableBuilder);
                }
                parser.nextToken();
            }
        }
    }

    private void parseColumns(JsonParser parser, Table.Builder tableBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                Column.Builder columnBuilder = Column.builder();
                columnBuilder.name(parser.currentName());
                parseColumn(parser, columnBuilder);
                tableBuilder.column(columnBuilder.build());

                parser.nextToken();
            }
        }
    }

    private void parseColumn(JsonParser parser, Column.Builder columnBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("type".equals(fieldName)) {
                    columnBuilder.type(ColumnType.valueOf(parser.nextTextValue().toUpperCase(Locale.ENGLISH)));
                } else if ("length".equals(fieldName)) {
                    columnBuilder.length(parser.nextIntValue(0));
                } else if ("length2".equals(fieldName)) {
                    columnBuilder.length2(parser.nextIntValue(0));
                } else if ("nullable".equals(fieldName)) {
                    columnBuilder.nullable(parser.nextBooleanValue());
                } else if ("enum-values".equals(fieldName)) {
                    parseEnumValues(parser, columnBuilder);
                }
                parser.nextToken();
            }
        }
    }

    private void parseEnumValues(JsonParser parser, Column.Builder columnBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String enumLiteralName = parser.currentName();
                parser.nextToken();
                if (parser.currentToken().isNumeric()) {
                    columnBuilder.enumValue(enumLiteralName, parser.getIntValue());
                } else {
                    columnBuilder.enumValue(enumLiteralName, parser.getText());
                }
                parser.nextToken();
            }
        }
    }

    private void parsePrimaryKey(JsonParser parser, Table.Builder tableBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_ARRAY) {
            parser.nextToken();
            while (parser.currentToken() != JsonToken.END_ARRAY) {
                tableBuilder.primaryKey(parser.getText());
                parser.nextToken();
            }
        }
    }

    private void parseIndexes(JsonParser parser, Table.Builder tableBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                Index.Builder indexBuilder = Index.builder();
                indexBuilder.name(parser.currentName());
                parseIndex(parser, indexBuilder);
                tableBuilder.index(indexBuilder.build());

                parser.nextToken();
            }
        }
    }

    private void parseIndex(JsonParser parser, Index.Builder indexBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("unique".equals(fieldName)) {
                    indexBuilder.unique(parser.nextBooleanValue());
                } else if ("real".equals(fieldName)) {
                    indexBuilder.real(parser.nextBooleanValue());
                } else if ("columns".equals(fieldName)) {
                    parseIndexColumns(parser, indexBuilder);
                }
                parser.nextToken();
            }
        }
    }

    private void parseIndexColumns(JsonParser parser, Index.Builder indexBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_ARRAY) {
            parser.nextToken();
            while (parser.currentToken() != JsonToken.END_ARRAY) {
                indexBuilder.column(parser.getText());
                parser.nextToken();
            }
        }
    }

    private void parseForeignKeys(JsonParser parser, Table.Builder tableBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                ForeignKey.Builder foreignKeyBuilder = ForeignKey.builder();
                foreignKeyBuilder.name(parser.currentName());
                parseForeignKey(parser, foreignKeyBuilder);
                tableBuilder.foreignKey(foreignKeyBuilder.build());

                parser.nextToken();
            }
        }
    }

    private void parseForeignKey(JsonParser parser, ForeignKey.Builder foreignKeyBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                if ("reference-table".equals(fieldName)) {
                    foreignKeyBuilder.referenceTable(parser.nextTextValue());
                } else if ("real".equals(fieldName)) {
                    foreignKeyBuilder.real(parser.nextBooleanValue());
                } else if ("columns".equals(fieldName)) {
                    parseForeignKeyColumns(parser, foreignKeyBuilder);
                }
                parser.nextToken();
            }
        }
    }

    private void parseForeignKeyColumns(JsonParser parser, ForeignKey.Builder foreignKeyBuilder) throws IOException {
        parser.nextToken();
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                foreignKeyBuilder.column(parser.currentName(), parser.nextTextValue());
                parser.nextToken();
            }
        }
    }

    private void parseAppends(JsonParser parser, Table.Builder tableBuilder) throws IOException {
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
                        if ("column-anchor".equals(fieldName)) {
                            appendBuilder.anchor(Anchor.valueOf(parser.nextTextValue().toUpperCase(Locale.ENGLISH)));
                        }
                        parser.nextToken();
                    }
                    tableBuilder.append(appendBuilder.build());
                }
                parser.nextToken();
            }
        }
    }
}
