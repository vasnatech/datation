package com.vasnatech.datation.schema.validate;

import com.vasnatech.datation.schema.Schemas;

import java.util.List;

public interface Validator {

    List<Result> validate(Schemas schemas);

    interface Result {
        String path();
        String message();
    }
}
