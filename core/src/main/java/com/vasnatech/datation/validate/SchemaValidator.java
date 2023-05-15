package com.vasnatech.datation.validate;

import com.vasnatech.datation.schema.Schema;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public interface SchemaValidator<S extends Schema> {

    List<ValidationInfo> validate(S schemas);

    default void validateAndThrow(S schema) {
        List<ValidationInfo> validationInfoList = validate(schema);
        if (!validationInfoList.isEmpty()) {
            throw new ValidationException(validationInfoList);
        }
    }

    default boolean isIdentifier(String identifier) {
        if (StringUtils.isEmpty(identifier)) {
            return false;
        }
        int index = 0;
        if (!Character.isJavaIdentifierStart(identifier.charAt(index++))) {
            return false;
        }
        for (; index < identifier.length(); ++index) {
            if (!Character.isJavaIdentifierPart(identifier.charAt(index))) {
                return false;
            }
        }
        return true;
    }
}
