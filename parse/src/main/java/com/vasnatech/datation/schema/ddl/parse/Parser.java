package com.vasnatech.datation.schema.ddl.parse;

import com.vasnatech.datation.schema.ddl.*;

import java.io.IOException;
import java.io.InputStream;

public interface Parser {

    Schemas parse(InputStream in) throws IOException;

    default Schemas parseAndNormalize(InputStream in) throws IOException {
        return normalize(parse(in));
    }

    default Schemas normalize(Schemas schemas) {
        Schemas.Builder databaseBuilder = Schemas.builder();
        databaseBuilder.meta(schemas.getMeta());
        for (Schema schema : schemas.getSchemas().values()) {
            databaseBuilder.schema(normalize(schema));
        }
        return databaseBuilder.build();
    }

    private Schema normalize(Schema schema) {
        if (schema.getDefinitions().isEmpty()) {
            return schema;
        }
        Schema.Builder schemaBuilder = Schema.builder();
        schemaBuilder.name(schema.getName());
        for (Table table : schema.getTables().values()) {
            schemaBuilder.table(normalize(schema, table));
        }
        return schemaBuilder.build();
    }

    private Table normalize(Schema schema, Table table) {
        Table.Builder tableBuilder = Table.builder();
        tableBuilder.name(table.getName())
                .primaryKey(table.getPrimaryKey())
                .indexes(table.getIndexes())
                .foreignKeys(table.getForeignKeys());
        for (Append append : table.getAppends().values()) {
            Table definition = schema.getDefinitions().get(append.getName());
            if (definition == null) {
                throw new ParserException(String.format("Unable to find definition %s in %s.%s", append.getName(), schema.getName(), table.getName()));
            }
            if (append.getColumnAnchor() == Anchor.HEAD) {
                tableBuilder.columns(definition.getColumns());
            }
            tableBuilder.primaryKey(definition.getPrimaryKey())
                    .indexes(definition.getIndexes())
                    .foreignKeys(definition.getForeignKeys());
        }
        tableBuilder.columns(table.getColumns());
        for (Append append : table.getAppends().values()) {
            Table definition = schema.getDefinitions().get(append.getName());
            if (append.getColumnAnchor() == Anchor.TAIL) {
                tableBuilder.columns(definition.getColumns());
            }
        }

        return tableBuilder.build();
    }
}
