package com.vasnatech.datation.ui.design.validate;

import com.vasnatech.commons.schema.validate.SchemaValidatorFactory;
import com.vasnatech.datation.ui.design.schema.UIDesignSchema;

public class UIDesignValidatorFactory implements SchemaValidatorFactory<UIDesignSchema, UIDesignValidator> {

    public UIDesignValidator create(String version) {
        return new UIDesignValidator();
    }
}
