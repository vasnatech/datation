package com.vasnatech.datation.ddl;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.datation.DatationException;
import com.vasnatech.datation.Modules;
import com.vasnatech.datation.SupportedMediaTypes;
import com.vasnatech.datation.ddl.schema.DDLSchemas;
import com.vasnatech.datation.load.SchemaLoader;
import com.vasnatech.datation.load.SchemaLoaderFactories;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class SchemaLoaderTest {

    @Test
    void loadDDLSchema() {
        Modules.add(DDLModule.instance());
        try (InputStream in = Resources.asInputStream("ddl-schema.json")) {
            final SchemaLoader schemaLoader = SchemaLoaderFactories.get(SupportedMediaTypes.JSON).create(
                    Map.of(
                            "normalize", Boolean.TRUE,
                            "validate", Boolean.TRUE
                    )
            );
            final DDLSchemas db = schemaLoader.load(in);
            System.out.println(db);
        } catch (IOException | DatationException e) {
            e.printStackTrace();
            assert false;
        }
    }
}
