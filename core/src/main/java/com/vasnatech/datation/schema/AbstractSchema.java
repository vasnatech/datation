package com.vasnatech.datation.schema;

import java.util.LinkedHashMap;

public abstract class AbstractSchema extends Node implements Schema {

    protected final LinkedHashMap<String, String> meta;
    protected final String type;

    public AbstractSchema(String name, String type, LinkedHashMap<String, String> meta) {
        super(name);
        this.type = type;
        this.meta = meta;
    }

    @Override
    public String type() {
        return type;
    }

    public LinkedHashMap<String, String> getMeta() {
        return meta;
    }
}
