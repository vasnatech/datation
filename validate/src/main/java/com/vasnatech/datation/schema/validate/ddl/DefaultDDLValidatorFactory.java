package com.vasnatech.datation.schema.validate.ddl;

public class DefaultDDLValidatorFactory extends DDLValidatorFactory {

    @Override
    public DDLValidator create() {
        return new DefaultDDLValidator();
    }
}
