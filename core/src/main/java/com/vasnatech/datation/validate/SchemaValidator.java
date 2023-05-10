package com.vasnatech.datation.validate;

import com.vasnatech.datation.schema.Schema;

import java.util.List;

public interface SchemaValidator<S extends Schema> {

    List<ValidationInfo> validate(S schemas);

    default void validateAndThrow(S schema) {
        List<ValidationInfo> validationInfoList = validate(schema);
        if (!validationInfoList.isEmpty()) {
            throw new ValidationException(validationInfoList);
        }
    }
}
