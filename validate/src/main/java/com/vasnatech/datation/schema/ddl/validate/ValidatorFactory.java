package com.vasnatech.datation.schema.ddl.validate;

public abstract class ValidatorFactory {

    static ValidatorFactory instance;

    public static ValidatorFactory instance() {
        if (instance == null) {
            instance(new ValidatorImplFactory());
        }
        return instance;
    }

    public static void instance(ValidatorFactory instance) {
        ValidatorFactory.instance = instance;
    }

    public abstract Validator create();
}
