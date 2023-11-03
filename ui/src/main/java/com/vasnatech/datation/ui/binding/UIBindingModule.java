package com.vasnatech.datation.ui.binding;

import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.datation.Module;
import com.vasnatech.datation.SupportedMediaTypes;
import com.vasnatech.datation.ui.binding.parse.JacksonJsonUIBindingParserFactory;
import com.vasnatech.datation.ui.binding.parse.UIBindingParser;
import com.vasnatech.datation.ui.binding.parse.UIBindingParserFactory;
import com.vasnatech.datation.ui.binding.schema.UIBindingSchema;
import com.vasnatech.datation.ui.binding.validate.UIBindingValidator;
import com.vasnatech.datation.ui.binding.validate.UIBindingValidatorFactory;

import java.util.Map;

public final class UIBindingModule implements Module<UIBindingSchema, UIBindingParser, UIBindingParserFactory, UIBindingValidator, UIBindingValidatorFactory> {

    private static final UIBindingModule INSTANCE = new UIBindingModule();
    public static UIBindingModule instance() {
        return INSTANCE;
    }

    private final Module<UIBindingSchema, UIBindingParser, UIBindingParserFactory, UIBindingValidator, UIBindingValidatorFactory> values;

    private UIBindingModule() {
        values = Module.of(
            "ui-binding",
            UIBindingSchema.class,
            Map.of(SupportedMediaTypes.JSON, new JacksonJsonUIBindingParserFactory()),
            new UIBindingValidatorFactory()
        );
    }

    @Override
    public String type() {
        return values.type();
    }

    @Override
    public Class<UIBindingSchema> schemaClass() {
        return values.schemaClass();
    }

    @Override
    public Map<MediaType, UIBindingParserFactory> parserFactories() {
        return values.parserFactories();
    }

    @Override
    public UIBindingValidatorFactory validatorFactory() {
        return values.validatorFactory();
    }
}
