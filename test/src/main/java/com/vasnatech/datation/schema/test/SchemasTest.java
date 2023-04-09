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

public class SchemasTest {

    @Test
    void test() {
        Schemas db = null;
        try (InputStream in = Resources.asInputStream("schema.json")) {
            Parser parser = ParserFactory.instance().create();
            db = parser.parseAndNormalize(in);
            Validator validator = ValidatorFactory.instance().create();
            List<Validator.Result> results = validator.validate(db);
            results.forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
