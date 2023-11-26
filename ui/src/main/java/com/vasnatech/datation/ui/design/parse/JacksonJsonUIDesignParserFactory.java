package com.vasnatech.datation.ui.design.parse;

import com.fasterxml.jackson.core.JsonFactory;
import com.vasnatech.datation.ui.component.schema.UIComponentFactory;

public class JacksonJsonUIDesignParserFactory extends UIDesignParserFactory {

    @Override
    public UIDesignParser create(String version) {
        return create(UIComponentFactory.instance(), new JsonFactory());
    }

    public UIDesignParser create(UIComponentFactory uiControlFactory, JsonFactory jsonFactory) {
        return new JacksonJsonUIDesignParser(uiControlFactory, jsonFactory);
    }
}
