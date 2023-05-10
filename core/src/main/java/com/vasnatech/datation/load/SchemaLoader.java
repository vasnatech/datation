package com.vasnatech.datation.load;

import com.vasnatech.commons.serialize.MediaType;
import com.vasnatech.datation.schema.Schema;

import java.io.IOException;
import java.io.InputStream;

public interface SchemaLoader {

    MediaType mediaType();

    <S extends Schema> S load(InputStream in) throws IOException;
}
