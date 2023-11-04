package com.vasnatech.datation.ddl.validate;

import com.vasnatech.datation.ddl.schema.DDLSchemas;
import com.vasnatech.commons.schema.validate.SchemaValidatorFactory;

public class DDLValidatorFactory implements SchemaValidatorFactory<DDLSchemas, DDLValidator> {

    public DDLValidator create(String version) {
        return new DDLValidator();
    }
}
