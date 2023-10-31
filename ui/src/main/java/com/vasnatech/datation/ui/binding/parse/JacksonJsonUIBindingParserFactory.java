package com.vasnatech.datation.ui.binding.parse;

import com.fasterxml.jackson.core.JsonFactory;

public class JacksonJsonUIBindingParserFactory extends UIBindingParserFactory {

    @Override
    public UIBindingParser create(String version) {
        return create(new JsonFactory());
    }

    public UIBindingParser create(JsonFactory jsonFactory) {
        return new JacksonJsonUIBindingParser(jsonFactory);
    }
}
