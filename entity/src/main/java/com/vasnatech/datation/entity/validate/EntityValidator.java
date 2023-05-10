package com.vasnatech.datation.entity.validate;

import com.vasnatech.datation.entity.schema.EntitySchemas;
import com.vasnatech.datation.validate.SchemaValidator;
import com.vasnatech.datation.validate.ValidationInfo;

import java.util.List;

public class EntityValidator implements SchemaValidator<EntitySchemas> {

    @Override
    public List<ValidationInfo> validate(EntitySchemas schemas) {
        return List.of();
    }
}
