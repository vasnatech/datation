package com.vasnatech.datation.ui.control.schema;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.commons.schema.Modules;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.commons.schema.load.SchemaLoader;
import com.vasnatech.commons.schema.load.SchemaLoaderFactories;
import com.vasnatech.datation.ui.control.UIControlModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class DefaultUIControlFactory extends UIControlFactory {

    final UIControlSchema schema;

    public DefaultUIControlFactory() {
        this(loadDefaultSchema());
    }

    public DefaultUIControlFactory(UIControlSchema schema) {
        this.schema = schema;
    }

    @Override
    public Control create(String name) {
        return schema.getControl(name);
    }

    static UIControlSchema loadDefaultSchema() {
        Modules.add(UIControlModule.instance());
        try (InputStream in = Resources.asInputStream(UIControlModule.class, "default--ui-control.json")) {
            final SchemaLoader schemaLoader = SchemaLoaderFactories.get(SupportedMediaTypes.JSON).create(
                    Map.of(
                            "normalize", Boolean.FALSE,
                            "validate", Boolean.TRUE
                    )
            );
            return schemaLoader.load(in);
        } catch (IOException e) {
            return null;
        }
    }
}
