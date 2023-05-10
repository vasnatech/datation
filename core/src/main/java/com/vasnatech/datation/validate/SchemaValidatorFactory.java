package com.vasnatech.datation.validate;

import com.vasnatech.datation.schema.Schema;

public interface SchemaValidatorFactory<S extends Schema, SV extends SchemaValidator<S>> {

    SV create(String version);
}
