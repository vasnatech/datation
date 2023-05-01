package com.vasnatech.datation.schema.ddl.validate;

import com.vasnatech.datation.schema.ddl.Schemas;

import java.util.List;

public interface Validator {

    List<Result> validate(Schemas schemas);

    interface Result {
        String path();
        String message();
    }
}
