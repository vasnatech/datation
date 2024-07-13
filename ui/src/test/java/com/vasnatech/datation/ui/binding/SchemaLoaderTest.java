package com.vasnatech.datation.ui.binding;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.commons.schema.SchemaException;
import com.vasnatech.commons.schema.Modules;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.commons.schema.load.SchemaLoader;
import com.vasnatech.commons.schema.load.SchemaLoaderFactories;
import com.vasnatech.datation.ui.binding.schema.UIBindingSchema;
import com.vasnatech.datation.ui.component.UIComponentModule;
import com.vasnatech.datation.ui.component.schema.UIComponentSchema;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class SchemaLoaderTest {

    @BeforeAll
    static void init() throws IOException {
        Modules.add(UIComponentModule.instance());
        try (InputStream in = Resources.asInputStream(com.vasnatech.datation.ui.component.SchemaLoaderTest.class, "default--ui-control.json")) {
            final SchemaLoader schemaLoader = SchemaLoaderFactories.get(SupportedMediaTypes.JSON).create(
                    Map.of(
                            "normalize", Boolean.TRUE,
                            "validate", Boolean.TRUE
                    )
            );
            final UIComponentSchema schema = schemaLoader.load(in);
        }
    }

    @Test
    void loadUIBindingSchema() {
        Modules.add(UIBindingModule.instance());
        try (InputStream in = Resources.asInputStream(SchemaLoaderTest.class, "text--ui-binding.json")) {
            final SchemaLoader schemaLoader = SchemaLoaderFactories.get(SupportedMediaTypes.JSON).create(
                    Map.of(
                            "normalize", Boolean.FALSE,
                            "validate", Boolean.TRUE
                    )
            );
            final UIBindingSchema schema = schemaLoader.load(in);
            System.out.println(schema);
        } catch (IOException | SchemaException e) {
            e.printStackTrace();
            assert false;
        }
    }
}
