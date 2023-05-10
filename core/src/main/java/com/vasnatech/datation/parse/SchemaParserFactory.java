package com.vasnatech.datation.parse;

import com.vasnatech.datation.schema.Schema;

public interface SchemaParserFactory<S extends Schema, SP extends SchemaParser<S>> {

    SP create(String version);
}
