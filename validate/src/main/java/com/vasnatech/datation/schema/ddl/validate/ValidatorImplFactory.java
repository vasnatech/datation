package com.vasnatech.datation.schema.ddl.validate;

public class ValidatorImplFactory extends ValidatorFactory {

    @Override
    public Validator create() {
        return new ValidatorImpl();
    }
}
