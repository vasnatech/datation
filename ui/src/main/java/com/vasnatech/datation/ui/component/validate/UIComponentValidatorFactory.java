package com.vasnatech.datation.ui.component.validate;

import com.vasnatech.datation.ui.component.schema.UIComponentSchema;
import com.vasnatech.commons.schema.validate.SchemaValidatorFactory;

public class UIComponentValidatorFactory implements SchemaValidatorFactory<UIComponentSchema, UIComponentValidator> {

    public UIComponentValidator create(String version) {
        return new UIComponentValidator();
    }
}
