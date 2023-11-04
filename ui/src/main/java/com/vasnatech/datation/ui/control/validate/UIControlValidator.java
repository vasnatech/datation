package com.vasnatech.datation.ui.control.validate;

import com.vasnatech.datation.ui.control.schema.UIControlSchema;
import com.vasnatech.commons.schema.validate.SchemaValidator;
import com.vasnatech.commons.schema.validate.ValidationInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class UIControlValidator implements SchemaValidator<UIControlSchema> {

    @Override
    public List<ValidationInfo> validate(UIControlSchema schema) {
        ValidationInfo.Builder resultBuilder = ValidationInfo.builder();
        if (!schema.getMeta().containsKey("@schema-type")) {
            resultBuilder.message("meta @schema-type is missing.");
        }
        if (!schema.getMeta().containsKey("@schema-version")) {
            resultBuilder.message("meta @schema-version is missing.");
        }
        if (StringUtils.isEmpty(schema.getName())) {
            resultBuilder.message("schema name is missing");
        }
        if (schema.getControls().isEmpty()) {
            resultBuilder.message("schema has no controls");
        }
        return resultBuilder.build();
    }
}
