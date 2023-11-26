package com.vasnatech.datation.ui.component.schema;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.commons.schema.Modules;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.commons.schema.load.SchemaLoader;
import com.vasnatech.commons.schema.load.SchemaLoaderFactories;
import com.vasnatech.datation.ui.component.UIComponentModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class DefaultUIComponentFactory extends UIComponentFactory {

    final UIComponentSchema schema;

    public DefaultUIComponentFactory() {
        this(loadDefaultSchema());
    }

    public DefaultUIComponentFactory(UIComponentSchema schema) {
        this.schema = schema;
    }

    @Override
    public Control control(String name) {
        return schema.getControl(name);
    }

    @Override
    public Container container(String name) {
        return schema.getContainer(name);
    }

    static UIComponentSchema loadDefaultSchema() {
        Modules.add(UIComponentModule.instance());
        try (InputStream in = Resources.asInputStream(UIComponentModule.class, "default--ui-control.json")) {
            final SchemaLoader schemaLoader = SchemaLoaderFactories.get(SupportedMediaTypes.JSON).create(
                    Map.of(
                            "normalize", Boolean.TRUE,
                            "validate", Boolean.TRUE
                    )
            );
            return schemaLoader.load(in);
        } catch (IOException e) {
            return null;
        }
    }
}
