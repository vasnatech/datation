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
        if (!schema.getMeta().containsKey("@schema-type")) {
            validationInfoBuilder.message("meta @schema-type is missing.");
        }
        if (!schema.getMeta().containsKey("@schema-version")) {
            validationInfoBuilder.message("meta @schema-version is missing.");
        }
        if (StringUtils.isEmpty(schema.getName())) {
            validationInfoBuilder.message("schema name is missing");
        }
        if (schema.getFields().isEmpty()) {
            validationInfoBuilder.message("schema has no fields");
        }

        for(Field field : schema.getFields().values()) {
            validateField(field, validationInfoBuilder);
        }

        return validationInfoBuilder.build();
    }

    void validateField(Field field, ValidationInfo.Builder validationInfoBuilder) {
        validationInfoBuilder.addPath(field.getName());

        if (StringUtils.isEmpty(field.getName())) {
            validationInfoBuilder.message("Field has no name.");
        }
        if (!isIdentifier(field.getName())) {
            validationInfoBuilder.message("Field has invalid name.");
        }
        if (field.getControl() == null) {
            validationInfoBuilder.message("Field has invalid control.");
        }
        if (field.getGetExpression() == null) {
            validationInfoBuilder.message("Field has invalid get binding.");
        }

        validationInfoBuilder.removePath();
    }
}
