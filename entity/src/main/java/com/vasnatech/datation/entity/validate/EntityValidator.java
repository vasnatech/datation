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
        if (!schemas.meta().containsKey("@schema-type")) {
            resultBuilder.message("meta @schema-type is missing.");
        }
        if (!schemas.meta().containsKey("@schema-version")) {
            resultBuilder.message("meta @schema-version is missing.");
        }
        for (EntitySchema schema : schemas.schemas().values()) {
            validateSchema(schema, resultBuilder);
        }
        return resultBuilder.build();
    }

    private void validateSchema(EntitySchema schema, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(schema.name())) {
            validationInfoBuilder.message("Schema has no name.");
        }
        validationInfoBuilder.addPath(schema.name());
        if (!isIdentifier(schema.name())) {
            validationInfoBuilder.message("Schema has invalid name.");
        }

        validateSchemaDDL(schema.ddl(), validationInfoBuilder);

        if (schema.entities().isEmpty()) {
            validationInfoBuilder.message("Schema has no entities.");
        }
        for (Entity entity : schema.definitions().values()) {
            validateEntity(schema, entity, validationInfoBuilder);
        }
        for (Entity entity : schema.entities().values()) {
            validateEntity(schema, entity, validationInfoBuilder);
        }

        validationInfoBuilder.removePath();
    }

    private void validateSchemaDDL(DDL.Schema ddl, ValidationInfo.Builder validationInfoBuilder) {
        if (ddl == null) {
            validationInfoBuilder.message("Schema has no dll.");
        } else {
            validationInfoBuilder.addPath("ddl");
            if (StringUtils.isEmpty(ddl.schema())) {
                validationInfoBuilder.message("ddl has no schema.");
            }
            validationInfoBuilder.removePath();
        }
    }

    private void validateEntity(EntitySchema schema, Entity entity, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(entity.name())) {
            validationInfoBuilder.message("Entity has no name.");
        }

        validationInfoBuilder.addPath(entity.name());

        if (!isIdentifier(entity.name())) {
            validationInfoBuilder.message("Entity has invalid name.");
        }
        if (entity.fields().isEmpty()) {
            validationInfoBuilder.message("Entity has no columns.");
        }
        if (entity.ids().isEmpty()) {
            validationInfoBuilder.message("Entity has no id.");
        }

        validateEntityDDL(entity.ddl(), validationInfoBuilder);
        validateEntityInherits(schema, entity, validationInfoBuilder);

        for (Field field : entity.fields().values()) {
            validateField(schema, field, validationInfoBuilder);
        }

        validationInfoBuilder.removePath();
    }

    private void validateEntityDDL(DDL.Table ddl, ValidationInfo.Builder validationInfoBuilder) {
        if (ddl == null) {
            validationInfoBuilder.message("Entity has no dll.");
        } else {
            validationInfoBuilder.addPath("ddl");
            if (StringUtils.isEmpty(ddl.table())) {
                validationInfoBuilder.message("ddl has no table.");
            }
            validationInfoBuilder.removePath();
        }
    }

    private void validateEntityInherits(EntitySchema schema, Entity entity, ValidationInfo.Builder validationInfoBuilder) {
        Entity.Inherits inherits = entity.inherits();
        if (inherits == null) {
            return;
        }
        validationInfoBuilder.addPath("inherits");
        if (StringUtils.isEmpty(inherits.base())) {
            validationInfoBuilder.message("inherits has no base.");
        } else {
            if (Optional.of(inherits)
                    .map(Entity.Inherits::base)
                    .map(schema.entities()::get)
                    .isEmpty()
            ) {
                validationInfoBuilder.message("Schema has no entity named " + inherits.base() + ".");
            }
        }
        validateEntityInheritsDDL(schema, entity, validationInfoBuilder);
        validationInfoBuilder.removePath();
    }

    private void validateEntityInheritsDDL(EntitySchema schema, Entity entity, ValidationInfo.Builder validationInfoBuilder) {
        Entity.Inherits inherits = entity.inherits();
        if (inherits.ddl() == null) {
            validationInfoBuilder.message("inherits has no ddl.");
        } else {
            validationInfoBuilder.addPath("inherits");
            DDL.RelationColumn ddl = inherits.ddl();
            if (ddl.type() != RelationType.ONE_TO_ONE) {
                validationInfoBuilder.message("relation should be one-to-one.");
            }
            if (ddl.table() == null) {
                validationInfoBuilder.message("ddl has no table or columns.");
            } else {
                if (StringUtils.isEmpty(ddl.table().name())) {
                    validationInfoBuilder.message("ddl has no table.");
                } else {
                    if (Optional.of(inherits)
                            .map(Entity.Inherits::base)
                            .map(schema.entities()::get)
                            .map(Entity::ddl)
                            .map(DDL.Table::table)
                            .filter(ddl.table().name()::equals)
                            .isEmpty()
                    ) {
                        validationInfoBuilder.message("ddl table does not match base entity.");
                    }
                    if (ddl.table().columns().isEmpty()) {
                        validationInfoBuilder.message("ddl has no columns.");
                    }
                }
            }

            validationInfoBuilder.removePath();
        }
    }

    private void validateField(EntitySchema schema, Field field, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(field.name())) {
            validationInfoBuilder.message("Field has no name.");
        }

        validationInfoBuilder.addPath(field.name());

        if (!isIdentifier(field.name())) {
            validationInfoBuilder.message("Field has invalid name.");
        }
        if (field.type() == null) {
            validationInfoBuilder.message("Field has no type.");
        } else {
            if (!field.type().isPrimitive()) {
                if (StringUtils.isEmpty(field.itemType())) {
                    validationInfoBuilder.message("Field has no item-type.");
                } else {
                    if (!schema.entities().containsKey(field.itemType())) {
                        validationInfoBuilder.message("Invalid item-type" + field.itemType() + ".");
                    }
                }
            }
            if (field.isEnum()) {
                if (!field.type().groups().contains(FieldTypeGroup.TEXT) && !field.type().groups().contains(FieldTypeGroup.NUMBER)) {
                    validationInfoBuilder.message("Field has wrong enum type. Type should be text or number type.");
                }
                for (Map.Entry<String, ?> entry : field.enumValues().entrySet()) {
                    String enumLiteralName = entry.getKey();
                    if (!isIdentifier(enumLiteralName)) {
                        validationInfoBuilder.addPath(enumLiteralName).message("Invalid enum literal.").removePath();
                    }
                    Object enumLiteralValue = entry.getValue();
                    if (field.type().groups().contains(FieldTypeGroup.TEXT) && !(enumLiteralValue instanceof CharSequence)) {
                        validationInfoBuilder.addPath(enumLiteralName).message("Invalid enum literal value. Expecting text.").removePath();
                    }
                    if (field.type().groups().contains(FieldTypeGroup.NUMBER) && !(enumLiteralValue instanceof Integer)) {
                        validationInfoBuilder.addPath(enumLiteralName).message("Invalid enum literal value. Expecting integer.").removePath();
                    }
                }
            }
        }
        if (field.isRelational() && field.fetch() == null) {
            validationInfoBuilder.message("Field has no fetch.");
        }

        validateFieldDDL(schema, field, validationInfoBuilder);

        validationInfoBuilder.removePath();
    }

    private void validateFieldDDL(EntitySchema schema, Field field, ValidationInfo.Builder validationInfoBuilder) {
        if (field.ddl() == null) {
            validationInfoBuilder.message("Field has no ddl.");
            return;
        }

        validationInfoBuilder.addPath("ddl");

        if (field.ddl() instanceof DDL.SimpleColumn ddl) {
            if (StringUtils.isEmpty(ddl.column())) {
                validationInfoBuilder.message("ddl has no column.");
            }
        } else if (field.ddl() instanceof DDL.RelationColumn ddl) {
            if (ddl.type() == null) {
                validationInfoBuilder.message("ddl has no relation.");
            } else {
                if (ddl.type().isCollection()) {
                    if (Optional.of(field)
                            .map(Field::type)
                            .filter(Predicates.of(FieldType::isArray).or(FieldType::isCollection))
                            .isEmpty()
                    ) {
                        validationInfoBuilder.message("ddl relation and field type does not match.");
                    }
                } else {
                    if (Optional.of(field)
                            .map(Field::type)
                            .filter(FieldType::isObject)
                            .isEmpty()
                    ) {
                        validationInfoBuilder.message("ddl relation and field type does not match.");
                    }
                }

                if (ddl.type() == RelationType.MANY_TO_MANY) {
                    if (ddl.inverseTable() == null) {
                        validationInfoBuilder.message("ddl has no inverse-table or inverse-columns.");
                    } else {
                        if (StringUtils.isEmpty(ddl.inverseTable().name())) {
                            validationInfoBuilder.message("ddl has no inverse-table.");
                        }
                        if (ddl.inverseTable().columns().isEmpty()) {
                            validationInfoBuilder.message("ddl has no inverse-columns.");
                        }
                    }
                }
            }
            if (ddl.table() == null) {
                validationInfoBuilder.message("ddl has no table or columns.");
            } else {
                if (StringUtils.isEmpty(ddl.table().name())) {
                    validationInfoBuilder.message("ddl has no table.");
                }
                if (ddl.table().columns().isEmpty()) {
                    validationInfoBuilder.message("ddl has no columns.");
                }
            }
        }

        validationInfoBuilder.removePath();
    }
}
