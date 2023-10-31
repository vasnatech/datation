package com.vasnatech.datation.ui.control;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.datation.DatationException;
import com.vasnatech.datation.Modules;
import com.vasnatech.datation.SupportedMediaTypes;
import com.vasnatech.datation.load.SchemaLoader;
import com.vasnatech.datation.load.SchemaLoaderFactories;
import com.vasnatech.datation.ui.control.schema.UIControlSchema;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class SchemaLoaderTest {

    @Test
    void loadDDLSchema() {
        Modules.add(UIControlModule.instance());
        try (InputStream in = Resources.asInputStream(SchemaLoaderTest.class, "default--ui-control.json")) {
            final SchemaLoader schemaLoader = SchemaLoaderFactories.get(SupportedMediaTypes.JSON).create(
                    Map.of(
                            "normalize", Boolean.FALSE,
                            "validate", Boolean.TRUE
                    )
            );
            final UIControlSchema schema = schemaLoader.load(in);
            System.out.println(schema);
        } catch (IOException | DatationException e) {
            e.printStackTrace();
            assert false;
        }
    }
}
