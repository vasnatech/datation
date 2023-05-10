package com.vasnatech.datation.entity.parse;


import com.vasnatech.datation.entity.schema.EntitySchemas;
import com.vasnatech.datation.parse.SchemaParserFactory;

public abstract class EntityParserFactory implements SchemaParserFactory<EntitySchemas, EntityParser> {

    public abstract EntityParser create(String version);
}
