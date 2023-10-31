package com.vasnatech.datation.ui.control;

import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.datation.Module;
import com.vasnatech.datation.SupportedMediaTypes;
import com.vasnatech.datation.ui.control.parse.JacksonJsonUIControlParserFactory;
import com.vasnatech.datation.ui.control.parse.UIControlParser;
import com.vasnatech.datation.ui.control.parse.UIControlParserFactory;
import com.vasnatech.datation.ui.control.schema.UIControlSchema;
import com.vasnatech.datation.ui.control.validate.UIControlValidator;
import com.vasnatech.datation.ui.control.validate.UIControlValidatorFactory;

import java.util.Map;

public final class UIControlModule implements Module<UIControlSchema, UIControlParser, UIControlParserFactory, UIControlValidator, UIControlValidatorFactory> {

    private static final UIControlModule INSTANCE = new UIControlModule();
    public static UIControlModule instance() {
        return INSTANCE;
    }

    private final Module<UIControlSchema, UIControlParser, UIControlParserFactory, UIControlValidator, UIControlValidatorFactory> values;

    private UIControlModule() {
        values = Module.of(
            "ui-control",
            UIControlSchema.class,
            Map.of(SupportedMediaTypes.JSON, new JacksonJsonUIControlParserFactory()),
            new UIControlValidatorFactory()
        );
    }

    @Override
    public String type() {
        return values.type();
    }

    @Override
    public Class<UIControlSchema> schemaClass() {
        return values.schemaClass();
    }

    @Override
    public Map<MediaType, UIControlParserFactory> parserFactories() {
        return values.parserFactories();
    }

    @Override
    public UIControlValidatorFactory validatorFactory() {
        return values.validatorFactory();
    }
}
