package com.vasnatech.datation.entity.validate;

import com.vasnatech.datation.entity.schema.EntitySchemas;
import com.vasnatech.datation.validate.SchemaValidatorFactory;

public class EntityValidatorFactory implements SchemaValidatorFactory<EntitySchemas, EntityValidator> {

    public EntityValidator create(String version) {
        return new EntityValidator();
    }
}
