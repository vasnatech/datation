package com.vasnatech.datation.parse;

import com.fasterxml.jackson.core.JsonParser;
import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.datation.schema.Schema;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface SchemaParser<S extends Schema> {

    MediaType mediaType();

    S parse(InputStream in) throws IOException;

    S continueParsing(JsonParser parser, Map<String, String> meta) throws IOException;

    S normalize(S schema);
}
