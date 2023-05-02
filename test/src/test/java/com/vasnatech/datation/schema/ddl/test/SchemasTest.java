package com.vasnatech.datation.schema.ddl.test;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.datation.schema.ddl.Schemas;
import com.vasnatech.datation.schema.parse.ddl.DDLParser;
import com.vasnatech.datation.schema.parse.ddl.DDLParserFactory;
import com.vasnatech.datation.schema.validate.ValidationInfo;
import com.vasnatech.datation.schema.validate.ddl.DDLValidator;
import com.vasnatech.datation.schema.validate.ddl.DDLValidatorFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SchemasTest {

    @Test
    void test() {
        try (InputStream in = Resources.asInputStream(SchemasTest.class, "schema.json")) {
            final DDLParser parser = DDLParserFactory.instance().create();
            final Schemas db = parser.parseAndNormalize(in);
            final DDLValidator validator = DDLValidatorFactory.instance().create();
            final List<ValidationInfo> results = validator.validate(db);
            assertThat(results).isEmpty();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
