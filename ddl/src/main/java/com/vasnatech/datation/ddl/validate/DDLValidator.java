package com.vasnatech.datation.ddl.validate;

import com.vasnatech.datation.ddl.schema.Column;
import com.vasnatech.datation.ddl.schema.DDLSchema;
import com.vasnatech.datation.ddl.schema.DDLSchemas;
import com.vasnatech.datation.ddl.schema.ForeignKey;
import com.vasnatech.datation.ddl.schema.Index;
import com.vasnatech.datation.ddl.schema.Table;
import com.vasnatech.datation.validate.SchemaValidator;
import com.vasnatech.datation.validate.ValidationInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class DDLValidator implements SchemaValidator<DDLSchemas> {

    @Override
    public List<ValidationInfo> validate(DDLSchemas schemas) {
        ValidationInfo.Builder resultBuilder = ValidationInfo.builder();
        if (!schemas.getMeta().containsKey("@schema-type")) {
            resultBuilder.message("meta @schema-type is missing.");
        }
        if (!schemas.getMeta().containsKey("@schema-version")) {
            resultBuilder.message("meta @schema-version is missing.");
        }
        for (DDLSchema schema : schemas.getSchemas().values()) {
            validateSchema(schema, resultBuilder);
        }
        return resultBuilder.build();
    }

    private void validateSchema(DDLSchema schema, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(schema.getName())) {
            validationInfoBuilder.message("Schema has no name.");
        }
        validationInfoBuilder.addPath(schema.getName());
        if (!isIdentifier(schema.getName())) {
            validationInfoBuilder.message("Schema has invalid name.");
        }
        if (schema.getTables().isEmpty()) {
            validationInfoBuilder.message("Schema has no tables.");
        }

        for (Table table : schema.getDefinitions().values()) {
            validateTable(schema, table, validationInfoBuilder);
        }

        for (Table table : schema.getTables().values()) {
            validateTable(schema, table, validationInfoBuilder);
        }

        validationInfoBuilder.removePath();
    }

    private void validateTable(DDLSchema schema, Table table, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(table.getName())) {
            validationInfoBuilder.message("Table has no name.");
        }

        validationInfoBuilder.addPath(table.getName());

        if (!isIdentifier(table.getName())) {
            validationInfoBuilder.message("Table has invalid name.");
        }
        if (table.getColumns().isEmpty()) {
            validationInfoBuilder.message("Table has no columns.");
        }
        if (table.getPrimaryKey().isEmpty()) {
            validationInfoBuilder.message("Table has no primary key.");
        }

        for (Column column : table.getColumns().values()) {
            validateColumn(column, validationInfoBuilder);
        }

        for (Index index : table.getIndexes().values()) {
            validateIndex(table, index, validationInfoBuilder);
        }

        for (ForeignKey foreignKey : table.getForeignKeys().values()) {
            validateForeignKey(schema, table, foreignKey, validationInfoBuilder);
        }

        validationInfoBuilder.removePath();
    }

    private void validateColumn(Column column, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(column.getName())) {
            validationInfoBuilder.message("Column has no name.");
        }

        validationInfoBuilder.addPath(column.getName());

        if (!isIdentifier(column.getName())) {
            validationInfoBuilder.message("Column has invalid name.");
        }
        if (column.getType() == null) {
            validationInfoBuilder.message("Column has no type.");
        } else {
            if (column.getType().isLengthRequired() && column.getLength() <= 0) {
                validationInfoBuilder.message("Column has no length.");
            }
            if (column.getType().isLength2Required() && column.getLength2() <= 0) {
                validationInfoBuilder.message("Column has no length2.");
            }
        }
        if (column.isEnum()) {
            if (!column.getType().isText() && !column.getType().isNumber()) {
                validationInfoBuilder.message("Column has wrong enum type. Type should be text or number type.");
            }
            for (Map.Entry<String, ?> entry : column.getEnumValues().entrySet()) {
                String enumLiteralName = entry.getKey();
                if (!isIdentifier(enumLiteralName)) {
                    validationInfoBuilder.addPath(enumLiteralName).message("Invalid enum literal.").removePath();
                }
                Object enumLiteralValue = entry.getValue();
                if (column.getType().isText() && !(enumLiteralValue instanceof CharSequence)) {
                    validationInfoBuilder.addPath(enumLiteralName).message("Invalid enum literal value. Expecting text.").removePath();
                }
                if (column.getType().isNumber() && !(enumLiteralValue instanceof Integer)) {
                    validationInfoBuilder.addPath(enumLiteralName).message("Invalid enum literal value. Expecting integer.").removePath();
                }
            }
        }

        validationInfoBuilder.removePath();
    }

    private void validateIndex(Table table, Index index, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(index.getName())) {
            validationInfoBuilder.message("Index has no name.");
        }

        validationInfoBuilder.addPath(index.getName());

        if (!isIdentifier(index.getName())) {
            validationInfoBuilder.message("Index has invalid name.");
        }
        if (index.getColumns().isEmpty()) {
            validationInfoBuilder.message("Index has no columns.");
        }

        for (String columnName : index.getColumns()) {
            if (!table.getColumns().containsKey(columnName)) {
                validationInfoBuilder.addPath(columnName).message("Index column does not exist.").removePath();
            }
        }

        validationInfoBuilder.removePath();
    }

    private void validateForeignKey(DDLSchema schema, Table table, ForeignKey foreignKey, ValidationInfo.Builder validationInfoBuilder) {
        if (StringUtils.isEmpty(foreignKey.getName())) {
            validationInfoBuilder.message("Foreign Key has no name.");
        }

        validationInfoBuilder.addPath(foreignKey.getName());

        if (!isIdentifier(foreignKey.getName())) {
            validationInfoBuilder.message("Foreign Key has invalid name.");
        }
        if (foreignKey.getColumns().isEmpty()) {
            validationInfoBuilder.message("Foreign Key has no columns.");
        }

        Table primaryTable = schema.getTables().get(foreignKey.getReferenceTable());
        if (primaryTable == null) {
            validationInfoBuilder.addPath(foreignKey.getReferenceTable()).message("Foreign Key reference table does not exist.").removePath();
        }

        for (Map.Entry<String, String> columns : foreignKey.getColumns().entrySet()) {
            if (!table.getColumns().containsKey(columns.getKey())) {
                validationInfoBuilder.addPath(columns.getKey()).message("Foreign Key column does not exist.").removePath();
            }
            if (!primaryTable.getColumns().containsKey(columns.getValue())) {
                validationInfoBuilder.addPath(columns.getValue()).message("Foreign Key column does not exist.").removePath();
            }
        }

        validationInfoBuilder.removePath();
    }

    private boolean isIdentifier(String identifier) {
        if (StringUtils.isEmpty(identifier)) {
            return false;
        }
        int index = 0;
        if (!Character.isJavaIdentifierStart(identifier.charAt(index++))) {
            return false;
        }
        for (; index < identifier.length(); ++index) {
            if (!Character.isJavaIdentifierPart(identifier.charAt(index))) {
                return false;
            }
        }
        return true;
    }

}
