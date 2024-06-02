package com.vasnatech.datation.entity.parse;

import com.vasnatech.datation.entity.schema.Entity;
import com.vasnatech.datation.entity.schema.EntitySchema;
import com.vasnatech.datation.entity.schema.EntitySchemas;
import com.vasnatech.commons.schema.parse.ParserException;
import com.vasnatech.commons.schema.parse.SchemaParser;
import com.vasnatech.commons.schema.schema.Anchor;
import com.vasnatech.commons.schema.schema.Append;

public interface EntityParser extends SchemaParser<EntitySchemas> {

    @Override
    default EntitySchemas normalize(EntitySchemas schemas) {
        EntitySchemas.Builder schemasBuilder = EntitySchemas.builder();
        schemasBuilder.meta(schemas.meta());
        for (EntitySchema schema : schemas.schemas().values()) {
            schemasBuilder.schema(normalize(schema));
        }
        return schemasBuilder.build();
    }

    private EntitySchema normalize(EntitySchema schema) {
        if (schema.definitions().isEmpty()) {
            return schema;
        }
        EntitySchema.Builder schemaBuilder = EntitySchema.builder();
        schemaBuilder.name(schema.name());
        schemaBuilder.ddl(schema.ddl());
        for (Entity entity : schema.entities().values()) {
            schemaBuilder.entity(normalize(schema, entity));
        }
        return schemaBuilder.build();
    }

    private Entity normalize(EntitySchema schema, Entity entity) {
        Entity.Builder entityBuilder = Entity.builder();
        entityBuilder
                .name(entity.name())
                .ddl(entity.ddl())
                .ids(entity.ids())
                .inherits(entity.inherits());
        for (Append append : entity.appends().values()) {
            Entity definition = schema.definitions().get(append.name());
            if (definition == null) {
                throw new ParserException(String.format("Unable to find definition %s in %s.%s", append.name(), schema.name(), entity.name()));
            }
            if (append.anchor() == Anchor.HEAD) {
                entityBuilder.fields(definition.fields());
            }
            entityBuilder.ids(definition.ids());
        }
        entityBuilder.fields(entity.fields());
        for (Append append : entity.appends().values()) {
            Entity definition = schema.definitions().get(append.name());
            if (append.anchor() == Anchor.TAIL) {
                entityBuilder.fields(definition.fields());
            }
        }

        return entityBuilder.build();
    }
}
