package com.vasnatech.datation.ui.binding.validate;

import com.vasnatech.datation.ui.binding.schema.UIBindingSchema;
import com.vasnatech.commons.schema.validate.SchemaValidatorFactory;

public class UIBindingValidatorFactory implements SchemaValidatorFactory<UIBindingSchema, UIBindingValidator> {

    public UIBindingValidator create(String version) {
        return new UIBindingValidator();
    }
}
