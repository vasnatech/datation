package com.vasnatech.datation.schema.validate.ddl;

public abstract class DDLValidatorFactory {

    static DDLValidatorFactory instance;

    public static DDLValidatorFactory instance() {
        if (instance == null) {
            instance(new DefaultDDLValidatorFactory());
        }
        return instance;
    }

    public static void instance(DDLValidatorFactory instance) {
        DDLValidatorFactory.instance = instance;
    }

    public abstract DDLValidator create();
}
