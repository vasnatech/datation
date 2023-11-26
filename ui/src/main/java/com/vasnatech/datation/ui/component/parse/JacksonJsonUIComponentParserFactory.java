package com.vasnatech.datation.ui.component.parse;

import com.fasterxml.jackson.core.JsonFactory;

public class JacksonJsonUIComponentParserFactory extends UIComponentParserFactory {

    @Override
    public UIComponentParser create(String version) {
        return create(new JsonFactory());
    }

    public UIComponentParser create(JsonFactory jsonFactory) {
        return new JacksonJsonUIComponentParser(jsonFactory);
    }
}
