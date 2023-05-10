package com.vasnatech.datation;

import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.commons.serialize.MediaTypes;

public final class SupportedMediaTypes {

    private SupportedMediaTypes() {}

    public static MediaType JSON = MediaType.of("JSON", "application/json", "json");

    static {
        MediaTypes.add(JSON);
    }

}
