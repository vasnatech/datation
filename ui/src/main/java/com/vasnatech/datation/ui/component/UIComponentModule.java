package com.vasnatech.datation.ui.component;

import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.commons.schema.Module;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.datation.ui.component.parse.JacksonJsonUIComponentParserFactory;
import com.vasnatech.datation.ui.component.parse.UIComponentParser;
import com.vasnatech.datation.ui.component.parse.UIComponentParserFactory;
import com.vasnatech.datation.ui.component.schema.UIComponentSchema;
import com.vasnatech.datation.ui.component.validate.UIComponentValidator;
import com.vasnatech.datation.ui.component.validate.UIComponentValidatorFactory;

import java.util.Map;

public final class UIComponentModule implements Module<UIComponentSchema, UIComponentParser, UIComponentParserFactory, UIComponentValidator, UIComponentValidatorFactory> {

    private static final UIComponentModule INSTANCE = new UIComponentModule();
    public static UIComponentModule instance() {
        return INSTANCE;
    }

    private final Module<UIComponentSchema, UIComponentParser, UIComponentParserFactory, UIComponentValidator, UIComponentValidatorFactory> values;

    private UIComponentModule() {
        values = Module.of(
            "ui-control",
            UIComponentSchema.class,
            Map.of(SupportedMediaTypes.JSON, new JacksonJsonUIComponentParserFactory()),
            new UIComponentValidatorFactory()
        );
    }

    @Override
    public String type() {
        return values.type();
    }

    @Override
    public Class<UIComponentSchema> schemaClass() {
        return values.schemaClass();
    }

    @Override
    public Map<MediaType, UIComponentParserFactory> parserFactories() {
        return values.parserFactories();
    }

    @Override
    public UIComponentValidatorFactory validatorFactory() {
        return values.validatorFactory();
    }
}
