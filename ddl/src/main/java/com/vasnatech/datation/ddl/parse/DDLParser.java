package com.vasnatech.datation.ddl.parse;

import com.vasnatech.datation.ddl.schema.DDLSchema;
import com.vasnatech.datation.ddl.schema.DDLSchemas;
import com.vasnatech.datation.ddl.schema.Table;
import com.vasnatech.datation.schema.Anchor;
import com.vasnatech.datation.schema.Append;
import com.vasnatech.datation.parse.ParserException;
import com.vasnatech.datation.parse.SchemaParser;

public interface DDLParser extends SchemaParser<DDLSchemas> {

    default DDLSchemas normalize(DDLSchemas schemas) {
        DDLSchemas.Builder databaseBuilder = DDLSchemas.builder();
        databaseBuilder.meta(schemas.getMeta());
        for (DDLSchema schema : schemas.getSchemas().values()) {
            databaseBuilder.schema(normalize(schema));
        }
        return databaseBuilder.build();
    }

    private DDLSchema normalize(DDLSchema schema) {
        if (schema.getDefinitions().isEmpty()) {
            return schema;
        }
        DDLSchema.Builder schemaBuilder = DDLSchema.builder();
        schemaBuilder.name(schema.getName());
        for (Table table : schema.getTables().values()) {
            schemaBuilder.table(normalize(schema, table));
        }
        return schemaBuilder.build();
    }

    private Table normalize(DDLSchema schema, Table table) {
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
            if (append.getAnchor() == Anchor.HEAD) {
                tableBuilder.columns(definition.getColumns());
            }
            tableBuilder.primaryKey(definition.getPrimaryKey())
                    .indexes(definition.getIndexes())
                    .foreignKeys(definition.getForeignKeys());
        }
        tableBuilder.columns(table.getColumns());
        for (Append append : table.getAppends().values()) {
            Table definition = schema.getDefinitions().get(append.getName());
            if (append.getAnchor() == Anchor.TAIL) {
                tableBuilder.columns(definition.getColumns());
            }
        }

        return tableBuilder.build();
    }
}
