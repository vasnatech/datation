package com.vasnatech.datation.ddl.parse;

import com.vasnatech.datation.ddl.schema.DDLSchema;
import com.vasnatech.datation.ddl.schema.DDLSchemas;
import com.vasnatech.datation.ddl.schema.Table;
import com.vasnatech.commons.schema.schema.Anchor;
import com.vasnatech.commons.schema.schema.Append;
import com.vasnatech.commons.schema.parse.ParserException;
import com.vasnatech.commons.schema.parse.SchemaParser;

public interface DDLParser extends SchemaParser<DDLSchemas> {

    default DDLSchemas normalize(DDLSchemas schemas) {
        DDLSchemas.Builder schemasBuilder = DDLSchemas.builder();
        schemasBuilder.meta(schemas.meta());
        for (DDLSchema schema : schemas.schemas().values()) {
            schemasBuilder.schema(normalize(schema));
        }
        return schemasBuilder.build();
    }

    private DDLSchema normalize(DDLSchema schema) {
        if (schema.definitions().isEmpty()) {
            return schema;
        }
        DDLSchema.Builder schemaBuilder = DDLSchema.builder();
        schemaBuilder.name(schema.name());
        for (Table table : schema.tables().values()) {
            schemaBuilder.table(normalize(schema, table));
        }
        return schemaBuilder.build();
    }

    private Table normalize(DDLSchema schema, Table table) {
        Table.Builder tableBuilder = Table.builder();
        tableBuilder.name(table.name())
                .primaryKey(table.primaryKey())
                .indexes(table.indexes())
                .foreignKeys(table.foreignKeys());
        for (Append append : table.appends().values()) {
            Table definition = schema.definitions().get(append.name());
            if (definition == null) {
                throw new ParserException(String.format("Unable to find definition %s in %s.%s", append.name(), schema.name(), table.name()));
            }
            if (append.anchor() == Anchor.HEAD) {
                tableBuilder.columns(definition.columns());
            }
            tableBuilder.primaryKey(definition.primaryKey())
                    .indexes(definition.indexes())
                    .foreignKeys(definition.foreignKeys());
        }
        tableBuilder.columns(table.columns());
        for (Append append : table.appends().values()) {
            Table definition = schema.definitions().get(append.name());
            if (append.anchor() == Anchor.TAIL) {
                tableBuilder.columns(definition.columns());
            }
        }

        return tableBuilder.build();
    }
}
