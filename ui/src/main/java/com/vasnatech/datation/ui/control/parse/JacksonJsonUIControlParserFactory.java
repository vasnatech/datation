package com.vasnatech.datation.ui.control.parse;

import com.fasterxml.jackson.core.JsonFactory;

public class JacksonJsonUIControlParserFactory extends UIControlParserFactory {

    @Override
    public UIControlParser create(String version) {
        return create(new JsonFactory());
    }

    public UIControlParser create(JsonFactory jsonFactory) {
        return new JacksonJsonUIControlParser(jsonFactory);
    }
}
