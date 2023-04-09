package com.vasnatech.datation.schema.validate;

public class ValidatorImplFactory extends ValidatorFactory {

    @Override
    public Validator create() {
        return new ValidatorImpl();
    }
}
