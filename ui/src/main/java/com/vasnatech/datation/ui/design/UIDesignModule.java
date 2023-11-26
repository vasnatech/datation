package com.vasnatech.datation.ui.design;

import com.vasnatech.commons.schema.Module;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.datation.ui.design.parse.JacksonJsonUIDesignParserFactory;
import com.vasnatech.datation.ui.design.parse.UIDesignParser;
import com.vasnatech.datation.ui.design.parse.UIDesignParserFactory;
import com.vasnatech.datation.ui.design.schema.UIDesignSchema;
import com.vasnatech.datation.ui.design.validate.UIDesignValidator;
import com.vasnatech.datation.ui.design.validate.UIDesignValidatorFactory;

import java.util.Map;

public final class UIDesignModule implements Module<UIDesignSchema, UIDesignParser, UIDesignParserFactory, UIDesignValidator, UIDesignValidatorFactory> {

    private static final UIDesignModule INSTANCE = new UIDesignModule();
    public static UIDesignModule instance() {
        return INSTANCE;
    }

    private final Module<UIDesignSchema, UIDesignParser, UIDesignParserFactory, UIDesignValidator, UIDesignValidatorFactory> values;

    private UIDesignModule() {
        values = Module.of(
            "ui-design",
            UIDesignSchema.class,
            Map.of(SupportedMediaTypes.JSON, new JacksonJsonUIDesignParserFactory()),
            new UIDesignValidatorFactory()
        );
    }

    @Override
    public String type() {
        return values.type();
    }

    @Override
    public Class<UIDesignSchema> schemaClass() {
        return values.schemaClass();
    }

    @Override
    public Map<MediaType, UIDesignParserFactory> parserFactories() {
        return values.parserFactories();
    }

    @Override
    public UIDesignValidatorFactory validatorFactory() {
        return values.validatorFactory();
    }
}
