package com.vasnatech.datation.schema.test;

import com.vasnatech.commons.resource.Resources;
import com.vasnatech.datation.schema.Schemas;
import com.vasnatech.datation.schema.parse.Parser;
import com.vasnatech.datation.schema.parse.ParserFactory;
import com.vasnatech.datation.schema.validate.Validator;
import com.vasnatech.datation.schema.validate.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SchemasTest {

    @Test
    void test() {
        try (InputStream in = Resources.asInputStream(SchemasTest.class, "schema.json")) {
            final Parser parser = ParserFactory.instance().create();
            final Schemas db = parser.parseAndNormalize(in);
            final Validator validator = ValidatorFactory.instance().create();
            final List<Validator.Result> results = validator.validate(db);
            assertThat(results).isEmpty();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
