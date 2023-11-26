package com.vasnatech.datation.ui.design;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.commons.schema.Modules;
import com.vasnatech.commons.schema.SchemaException;
import com.vasnatech.commons.schema.SupportedMediaTypes;
import com.vasnatech.commons.schema.load.SchemaLoader;
import com.vasnatech.commons.schema.load.SchemaLoaderFactories;
import com.vasnatech.datation.ui.design.schema.UIDesignSchema;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class SchemaLoaderTest {

    @Test
    void loadUIDesignSchema() {
        Modules.add(UIDesignModule.instance());
        try (InputStream in = Resources.asInputStream(SchemaLoaderTest.class, "contact--ui-design.json")) {
            final SchemaLoader schemaLoader = SchemaLoaderFactories.get(SupportedMediaTypes.JSON).create(
                    Map.of(
                            "normalize", Boolean.TRUE,
                            "validate", Boolean.TRUE
                    )
            );
            final UIDesignSchema schema = schemaLoader.load(in);
            System.out.println(schema);
        } catch (IOException | SchemaException e) {
            e.printStackTrace();
            assert false;
        }
    }
}
