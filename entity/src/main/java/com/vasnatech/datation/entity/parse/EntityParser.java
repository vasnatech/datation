package com.vasnatech.datation.entity.parse;

import com.vasnatech.datation.entity.schema.Entity;
import com.vasnatech.datation.entity.schema.EntitySchema;
import com.vasnatech.datation.entity.schema.EntitySchemas;
import com.vasnatech.datation.parse.ParserException;
import com.vasnatech.datation.parse.SchemaParser;
import com.vasnatech.datation.schema.Anchor;
import com.vasnatech.datation.schema.Append;

public interface EntityParser extends SchemaParser<EntitySchemas> {

    @Override
    default EntitySchemas normalize(EntitySchemas schemas) {
        EntitySchemas.Builder schemasBuilder = EntitySchemas.builder();
        schemasBuilder.meta(schemas.getMeta());
        for (EntitySchema schema : schemas.getSchemas().values()) {
            schemasBuilder.schema(normalize(schema));
        }
        return schemasBuilder.build();
    }

    private EntitySchema normalize(EntitySchema schema) {
        if (schema.getDefinitions().isEmpty()) {
            return schema;
        }
        EntitySchema.Builder schemaBuilder = EntitySchema.builder();
        schemaBuilder.name(schema.getName());
        schemaBuilder.ddl(schema.getDDL());
        for (Entity entity : schema.getEntities().values()) {
            schemaBuilder.entity(normalize(schema, entity));
        }
        return schemaBuilder.build();
    }

    private Entity normalize(EntitySchema schema, Entity entity) {
        Entity.Builder entityBuilder = Entity.builder();
        entityBuilder
                .name(entity.getName())
                .ddl(entity.getDDL())
                .ids(entity.getIds())
                .inherits(entity.getInherits());
        for (Append append : entity.getAppends().values()) {
            Entity definition = schema.getDefinitions().get(append.getName());
            if (definition == null) {
                throw new ParserException(String.format("Unable to find definition %s in %s.%s", append.getName(), schema.getName(), entity.getName()));
            }
            if (append.getAnchor() == Anchor.HEAD) {
                entityBuilder.fields(definition.getFields());
            }
            entityBuilder.ids(definition.getIds());
        }
        entityBuilder.fields(entity.getFields());
        for (Append append : entity.getAppends().values()) {
            Entity definition = schema.getDefinitions().get(append.getName());
            if (append.getAnchor() == Anchor.TAIL) {
                entityBuilder.fields(definition.getFields());
            }
        }

        return entityBuilder.build();
    }
}
