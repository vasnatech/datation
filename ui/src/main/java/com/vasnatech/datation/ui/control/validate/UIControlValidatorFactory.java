package com.vasnatech.datation.ui.control.validate;

import com.vasnatech.datation.ui.control.schema.UIControlSchema;
import com.vasnatech.commons.schema.validate.SchemaValidatorFactory;

public class UIControlValidatorFactory implements SchemaValidatorFactory<UIControlSchema, UIControlValidator> {

    public UIControlValidator create(String version) {
        return new UIControlValidator();
    }
}
