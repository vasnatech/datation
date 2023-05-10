package com.vasnatech.datation.load;

import java.util.Map;

public abstract class SchemaLoaderFactory {

    public abstract SchemaLoader create(Map<String, ?> config);
}
