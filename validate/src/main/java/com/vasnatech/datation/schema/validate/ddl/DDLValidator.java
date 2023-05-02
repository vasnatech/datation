package com.vasnatech.datation.schema.validate.ddl;

import com.vasnatech.datation.schema.ddl.Schemas;
import com.vasnatech.datation.schema.validate.ValidationInfo;

import java.util.List;

public interface DDLValidator {

    List<ValidationInfo> validate(Schemas schemas);

}
