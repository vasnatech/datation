package com.vasnatech.datation.ddl.validate;

import com.vasnatech.datation.ddl.schema.Column;
import com.vasnatech.datation.ddl.schema.DDLSchema;
import com.vasnatech.datation.ddl.schema.DDLSchemas;
import com.vasnatech.datation.ddl.schema.ForeignKey;
import com.vasnatech.datation.ddl.schema.Index;
import com.vasnatech.datation.ddl.schema.Table;
import com.vasnatech.commons.schema.validate.SchemaValidator;
import com.vasnatech.commons.schema.validate.ValidationInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class DDLValidator implements SchemaValidator<DDLSchemas> {

    @Override
    public List<ValidationInfo> validate(DDLSchemas schemas) {
        ValidationInfo.Builder resultBuilder = ValidationInfo.builder();
        if (!schemas.meta().containsKey("@schema-type")) {
            resultBuilder.message("meta @schema-type is missing.");
        }
        if (!schemas.meta().containsKey("@schema-version")) {
            resultBuilder.message("meta @schema-version is missing.");
        }
        for (DDLSchema schema : schemas.schemas().values()) {
            validateSchema(schema, resultBuilder);
        }
        return resultBuilder.build();
    }

    private void validateSchema(DDLSchema schema, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(schema.name())) {
            validationInfoBuilder.message("Schema has no name.");
        }
        validationInfoBuilder.addPath(schema.name());
        if (!isIdentifier(schema.name())) {
            validationInfoBuilder.message("Schema has invalid name.");
        }
        if (schema.tables().isEmpty()) {
            validationInfoBuilder.message("Schema has no tables.");
        }

        for (Table table : schema.definitions().values()) {
            validateTable(schema, table, validationInfoBuilder);
        }

        for (Table table : schema.tables().values()) {
            validateTable(schema, table, validationInfoBuilder);
        }

        validationInfoBuilder.removePath();
    }

    private void validateTable(DDLSchema schema, Table table, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(table.name())) {
            validationInfoBuilder.message("Table has no name.");
        }

        validationInfoBuilder.addPath(table.name());

        if (!isIdentifier(table.name())) {
            validationInfoBuilder.message("Table has invalid name.");
        }
        if (table.columns().isEmpty()) {
            validationInfoBuilder.message("Table has no columns.");
        }
        if (table.primaryKey().isEmpty()) {
            validationInfoBuilder.message("Table has no primary key.");
        }

        for (Column column : table.columns().values()) {
            validateColumn(column, validationInfoBuilder);
        }

        for (Index index : table.indexes().values()) {
            validateIndex(table, index, validationInfoBuilder);
        }

        for (ForeignKey foreignKey : table.foreignKeys().values()) {
            validateForeignKey(schema, table, foreignKey, validationInfoBuilder);
        }

        validationInfoBuilder.removePath();
    }

    private void validateColumn(Column column, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(column.name())) {
            validationInfoBuilder.message("Column has no name.");
        }

        validationInfoBuilder.addPath(column.name());

        if (!isIdentifier(column.name())) {
            validationInfoBuilder.message("Column has invalid name.");
        }
        if (column.type() == null) {
            validationInfoBuilder.message("Column has no type.");
        } else {
            if (column.type().isLengthRequired() && column.length() <= 0) {
                validationInfoBuilder.message("Column has no length.");
            }
            if (column.type().isLength2Required() && column.length2() <= 0) {
                validationInfoBuilder.message("Column has no length2.");
            }
            if (column.isEnum()) {
                if (!column.type().isText() && !column.type().isNumber()) {
                    validationInfoBuilder.message("Column has wrong enum type. Type should be text or number type.");
                }
                for (Map.Entry<String, ?> entry : column.enumValues().entrySet()) {
                    String enumLiteralName = entry.getKey();
                    if (!isIdentifier(enumLiteralName)) {
                        validationInfoBuilder.addPath(enumLiteralName).message("Invalid enum literal.").removePath();
                    }
                    Object enumLiteralValue = entry.getValue();
                    if (column.type().isText() && !(enumLiteralValue instanceof CharSequence)) {
                        validationInfoBuilder.addPath(enumLiteralName).message("Invalid enum literal value. Expecting text.").removePath();
                    }
                    if (column.type().isNumber() && !(enumLiteralValue instanceof Integer)) {
                        validationInfoBuilder.addPath(enumLiteralName).message("Invalid enum literal value. Expecting integer.").removePath();
                    }
                }
            }
        }

        validationInfoBuilder.removePath();
    }

    private void validateIndex(Table table, Index index, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(index.name())) {
            validationInfoBuilder.message("Index has no name.");
        }

        validationInfoBuilder.addPath(index.name());

        if (!isIdentifier(index.name())) {
            validationInfoBuilder.message("Index has invalid name.");
        }
        if (index.columns().isEmpty()) {
            validationInfoBuilder.message("Index has no columns.");
        }

        for (String columnName : index.columns()) {
            if (!table.columns().containsKey(columnName)) {
                validationInfoBuilder.addPath(columnName).message("Index column does not exist.").removePath();
            }
        }

        validationInfoBuilder.removePath();
    }

    private void validateForeignKey(DDLSchema schema, Table table, ForeignKey foreignKey, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(foreignKey.name())) {
            validationInfoBuilder.message("Foreign Key has no name.");
        }

        validationInfoBuilder.addPath(foreignKey.name());

        if (!isIdentifier(foreignKey.name())) {
            validationInfoBuilder.message("Foreign Key has invalid name.");
        }
        if (foreignKey.columns().isEmpty()) {
            validationInfoBuilder.message("Foreign Key has no columns.");
        }

        Table primaryTable = schema.tables().get(foreignKey.referenceTable());
        if (primaryTable == null) {
            validationInfoBuilder.addPath(foreignKey.referenceTable()).message("Foreign Key reference table does not exist.").removePath();
        } else {
            for (Map.Entry<String, String> columns : foreignKey.columns().entrySet()) {
                if (!table.columns().containsKey(columns.getKey())) {
                    validationInfoBuilder.addPath(columns.getKey()).message("Foreign Key column does not exist.").removePath();
                }
                if (!primaryTable.columns().containsKey(columns.getValue())) {
                    validationInfoBuilder.addPath(columns.getValue()).message("Foreign Key column does not exist.").removePath();
                }
            }
        }

        validationInfoBuilder.removePath();
    }

}
