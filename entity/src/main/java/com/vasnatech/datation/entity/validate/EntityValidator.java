package com.vasnatech.datation.entity.validate;

import com.vasnatech.commons.function.Predicates;
import com.vasnatech.datation.entity.schema.*;
import com.vasnatech.commons.schema.validate.SchemaValidator;
import com.vasnatech.commons.schema.validate.ValidationInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EntityValidator implements SchemaValidator<EntitySchemas> {

    @Override
    public List<ValidationInfo> validate(EntitySchemas schemas) {
        ValidationInfo.Builder resultBuilder = ValidationInfo.builder();
        if (!schemas.getMeta().containsKey("@schema-type")) {
            resultBuilder.message("meta @schema-type is missing.");
        }
        if (!schemas.getMeta().containsKey("@schema-version")) {
            resultBuilder.message("meta @schema-version is missing.");
        }
        for (EntitySchema schema : schemas.getSchemas().values()) {
            validateSchema(schema, resultBuilder);
        }
        return resultBuilder.build();
    }

    private void validateSchema(EntitySchema schema, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(schema.getName())) {
            validationInfoBuilder.message("Schema has no name.");
        }
        validationInfoBuilder.addPath(schema.getName());
        if (!isIdentifier(schema.getName())) {
            validationInfoBuilder.message("Schema has invalid name.");
        }

        validateSchemaDDL(schema.getDDL(), validationInfoBuilder);

        if (schema.getEntities().isEmpty()) {
            validationInfoBuilder.message("Schema has no entities.");
        }
        for (Entity entity : schema.getDefinitions().values()) {
            validateEntity(schema, entity, validationInfoBuilder);
        }
        for (Entity entity : schema.getEntities().values()) {
            validateEntity(schema, entity, validationInfoBuilder);
        }

        validationInfoBuilder.removePath();
    }

    private void validateSchemaDDL(DDL.Schema ddl, ValidationInfo.Builder validationInfoBuilder) {
        if (ddl == null) {
            validationInfoBuilder.message("Schema has no dll.");
        } else {
            validationInfoBuilder.addPath("ddl");
            if (StringUtils.isEmpty(ddl.getSchema())) {
                validationInfoBuilder.message("ddl has no schema.");
            }
            validationInfoBuilder.removePath();
        }
    }

    private void validateEntity(EntitySchema schema, Entity entity, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(entity.getName())) {
            validationInfoBuilder.message("Entity has no name.");
        }

        validationInfoBuilder.addPath(entity.getName());

        if (!isIdentifier(entity.getName())) {
            validationInfoBuilder.message("Entity has invalid name.");
        }
        if (entity.getFields().isEmpty()) {
            validationInfoBuilder.message("Entity has no columns.");
        }
        if (entity.getIds().isEmpty()) {
            validationInfoBuilder.message("Entity has no id.");
        }

        validateEntityDDL(entity.getDDL(), validationInfoBuilder);
        validateEntityInherits(schema, entity, validationInfoBuilder);

        for (Field field : entity.getFields().values()) {
            validateField(schema, field, validationInfoBuilder);
        }

        validationInfoBuilder.removePath();
    }

    private void validateEntityDDL(DDL.Table ddl, ValidationInfo.Builder validationInfoBuilder) {
        if (ddl == null) {
            validationInfoBuilder.message("Entity has no dll.");
        } else {
            validationInfoBuilder.addPath("ddl");
            if (StringUtils.isEmpty(ddl.getTable())) {
                validationInfoBuilder.message("ddl has no table.");
            }
            validationInfoBuilder.removePath();
        }
    }

    private void validateEntityInherits(EntitySchema schema, Entity entity, ValidationInfo.Builder validationInfoBuilder) {
        Entity.Inherits inherits = entity.getInherits();
        if (inherits == null) {
            return;
        }
        validationInfoBuilder.addPath("inherits");
        if (StringUtils.isEmpty(inherits.getBase())) {
            validationInfoBuilder.message("inherits has no base.");
        } else {
            if (Optional.of(inherits)
                    .map(Entity.Inherits::getBase)
                    .map(schema.getEntities()::get)
                    .isEmpty()
            ) {
                validationInfoBuilder.message("Schema has no entity named " + inherits.getBase() + ".");
            }
        }
        validateEntityInheritsDDL(schema, entity, validationInfoBuilder);
        validationInfoBuilder.removePath();
    }

    private void validateEntityInheritsDDL(EntitySchema schema, Entity entity, ValidationInfo.Builder validationInfoBuilder) {
        Entity.Inherits inherits = entity.getInherits();
        if (inherits.getDDL() == null) {
            validationInfoBuilder.message("inherits has no ddl.");
        } else {
            validationInfoBuilder.addPath("inherits");
            DDL.RelationColumn ddl = inherits.getDDL();
            if (ddl.getType() != RelationType.ONE_TO_ONE) {
                validationInfoBuilder.message("relation should be one-to-one.");
            }
            if (ddl.getTable() == null) {
                validationInfoBuilder.message("ddl has no table or columns.");
            } else {
                if (StringUtils.isEmpty(ddl.getTable().getName())) {
                    validationInfoBuilder.message("ddl has no table.");
                } else {
                    if (Optional.of(inherits)
                            .map(Entity.Inherits::getBase)
                            .map(schema.getEntities()::get)
                            .map(Entity::getDDL)
                            .map(DDL.Table::getTable)
                            .filter(ddl.getTable().getName()::equals)
                            .isEmpty()
                    ) {
                        validationInfoBuilder.message("ddl table does not match base entity.");
                    }
                    if (ddl.getTable().getColumns().isEmpty()) {
                        validationInfoBuilder.message("ddl has no columns.");
                    }
                }
            }

            validationInfoBuilder.removePath();
        }
    }

    private void validateField(EntitySchema schema, Field field, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(field.getName())) {
            validationInfoBuilder.message("Field has no name.");
        }

        validationInfoBuilder.addPath(field.getName());

        if (!isIdentifier(field.getName())) {
            validationInfoBuilder.message("Field has invalid name.");
        }
        if (field.getType() == null) {
            validationInfoBuilder.message("Field has no type.");
        } else {
            if (!field.getType().isPrimitive()) {
                if (StringUtils.isEmpty(field.getItemType())) {
                    validationInfoBuilder.message("Field has no item-type.");
                } else {
                    if (!schema.getEntities().containsKey(field.getItemType())) {
                        validationInfoBuilder.message("Invalid item-type" + field.getItemType() + ".");
                    }
                }
            }
        }
        if (field.isRelational() && field.getFetch() == null) {
            validationInfoBuilder.message("Field has no fetch.");
        }
        if (field.isEnum()) {
            if (!field.getType().getGroups().contains(FieldTypeGroup.TEXT) && !field.getType().getGroups().contains(FieldTypeGroup.NUMBER)) {
                validationInfoBuilder.message("Field has wrong enum type. Type should be text or number type.");
            }
            for (Map.Entry<String, ?> entry : field.getEnumValues().entrySet()) {
                String enumLiteralName = entry.getKey();
                if (!isIdentifier(enumLiteralName)) {
                    validationInfoBuilder.addPath(enumLiteralName).message("Invalid enum literal.").removePath();
                }
                Object enumLiteralValue = entry.getValue();
                if (field.getType().getGroups().contains(FieldTypeGroup.TEXT) && !(enumLiteralValue instanceof CharSequence)) {
                    validationInfoBuilder.addPath(enumLiteralName).message("Invalid enum literal value. Expecting text.").removePath();
                }
                if (field.getType().getGroups().contains(FieldTypeGroup.NUMBER) && !(enumLiteralValue instanceof Integer)) {
                    validationInfoBuilder.addPath(enumLiteralName).message("Invalid enum literal value. Expecting integer.").removePath();
                }
            }
        }

        validateFieldDDL(schema, field, validationInfoBuilder);

        validationInfoBuilder.removePath();
    }

    private void validateFieldDDL(EntitySchema schema, Field field, ValidationInfo.Builder validationInfoBuilder) {
        if (field.getDDL() == null) {
            validationInfoBuilder.message("Field has no ddl.");
            return;
        }

        validationInfoBuilder.addPath("ddl");

        if (field.getDDL() instanceof DDL.SimpleColumn ddl) {
            if (StringUtils.isEmpty(ddl.getColumn())) {
                validationInfoBuilder.message("ddl has no column.");
            }
        } else if (field.getDDL() instanceof DDL.RelationColumn ddl) {
            if (ddl.getType() == null) {
                validationInfoBuilder.message("ddl has no relation.");
            } else {
                if (ddl.getType().isCollection()) {
                    if (Optional.of(field)
                            .map(Field::getType)
                            .filter(Predicates.of(FieldType::isArray).or(FieldType::isCollection))
                            .isEmpty()
                    ) {
                        validationInfoBuilder.message("ddl relation and field type does not match.");
                    }
                } else {
                    if (Optional.of(field)
                            .map(Field::getType)
                            .filter(FieldType::isObject)
                            .isEmpty()
                    ) {
                        validationInfoBuilder.message("ddl relation and field type does not match.");
                    }
                }

                if (ddl.getType() == RelationType.MANY_TO_MANY) {
                    if (ddl.getInverseTable() == null) {
                        validationInfoBuilder.message("ddl has no inverse-table or inverse-columns.");
                    } else {
                        if (StringUtils.isEmpty(ddl.getInverseTable().getName())) {
                            validationInfoBuilder.message("ddl has no inverse-table.");
                        }
                        if (ddl.getInverseTable().getColumns().isEmpty()) {
                            validationInfoBuilder.message("ddl has no inverse-columns.");
                        }
                    }
                }
            }
            if (ddl.getTable() == null) {
                validationInfoBuilder.message("ddl has no table or columns.");
            } else {
                if (StringUtils.isEmpty(ddl.getTable().getName())) {
                    validationInfoBuilder.message("ddl has no table.");
                }
                if (ddl.getTable().getColumns().isEmpty()) {
                    validationInfoBuilder.message("ddl has no columns.");
                }
            }
        }

        validationInfoBuilder.removePath();
    }
}
