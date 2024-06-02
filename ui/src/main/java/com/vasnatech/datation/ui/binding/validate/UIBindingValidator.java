package com.vasnatech.datation.ui.binding.validate;

import com.vasnatech.datation.ui.binding.schema.Field;
import com.vasnatech.datation.ui.binding.schema.UIBindingSchema;
import com.vasnatech.commons.schema.validate.SchemaValidator;
import com.vasnatech.commons.schema.validate.ValidationInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class UIBindingValidator implements SchemaValidator<UIBindingSchema> {

    @Override
    public List<ValidationInfo> validate(UIBindingSchema schema) {
        ValidationInfo.Builder validationInfoBuilder = ValidationInfo.builder();
        if (!schema.meta().containsKey("@schema-type")) {
            validationInfoBuilder.message("meta @schema-type is missing.");
        }
        if (!schema.meta().containsKey("@schema-version")) {
            validationInfoBuilder.message("meta @schema-version is missing.");
        }
        if (StringUtils.isEmpty(schema.name())) {
            validationInfoBuilder.message("schema name is missing");
        }
        if (schema.fields().isEmpty()) {
            validationInfoBuilder.message("schema has no fields");
        }

        for(Field field : schema.fields().values()) {
            validateField(field, validationInfoBuilder);
        }

        return validationInfoBuilder.build();
    }

    void validateField(Field field, ValidationInfo.Builder validationInfoBuilder) {
        validationInfoBuilder.addPath(field.name());

        if (StringUtils.isEmpty(field.name())) {
            validationInfoBuilder.message("Field has no name.");
        }
        if (!isIdentifier(field.name())) {
            validationInfoBuilder.message("Field has invalid name.");
        }
        if (field.control() == null) {
            validationInfoBuilder.message("Field has invalid control.");
        }
        if (field.getExpression() == null) {
            validationInfoBuilder.message("Field has invalid get binding.");
        }

        validationInfoBuilder.removePath();
    }
}
