package com.vasnatech.datation.entity;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.datation.DatationException;
import com.vasnatech.datation.Modules;
import com.vasnatech.datation.SupportedMediaTypes;
import com.vasnatech.datation.entity.schema.EntitySchemas;
import com.vasnatech.datation.load.SchemaLoader;
import com.vasnatech.datation.load.SchemaLoaderFactories;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class SchemaLoaderTest {

    @Test
    void loadEntitySchema() {
        Modules.add(EntityModule.instance());
        try (InputStream in = Resources.asInputStream("entity-schema.json")) {
            final SchemaLoader schemaLoader = SchemaLoaderFactories.get(SupportedMediaTypes.JSON).create(
                    Map.of(
                            "normalize", Boolean.TRUE,
                            "validate", Boolean.TRUE
                    )
            );
            final EntitySchemas schemas = schemaLoader.load(in);
            System.out.println(schemas);
        } catch (IOException | DatationException e) {
            e.printStackTrace();
            assert false;
        }
    }
}
