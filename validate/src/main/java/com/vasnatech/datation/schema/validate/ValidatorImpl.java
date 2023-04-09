package com.vasnatech.datation.schema.validate;

import com.vasnatech.datation.schema.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ValidatorImpl implements Validator {

    @Override
    public List<Result> validate(Schemas schemas) {
        ResultBuilder resultBuilder = new ResultBuilder();
        if (!schemas.getMeta().containsKey("@schema-model")) {
            resultBuilder.message("meta @schema-model is missing.");
        }
        if (!schemas.getMeta().containsKey("@schema-version")) {
            resultBuilder.message("meta @schema-version is missing.");
        }
        for (Schema schema : schemas.getSchemas().values()) {
            validateSchema(schema, resultBuilder);
        }
        return resultBuilder.build();
    }

    private void validateSchema(Schema schema, ResultBuilder resultBuilder) {
        if (StringUtils.isEmpty(schema.getName())) {
            resultBuilder.message("Schema has no name.");
        }
        resultBuilder.addPath(schema.getName());
        if (!isIdentifier(schema.getName())) {
            resultBuilder.message("Schema has invalid name.");
        }
        if (schema.getTables().isEmpty()) {
            resultBuilder.message("Schema has no tables.");
        }

        for (Table table : schema.getDefinitions().values()) {
            validateTable(schema, table, resultBuilder);
        }

        for (Table table : schema.getTables().values()) {
            validateTable(schema, table, resultBuilder);
        }

        resultBuilder.removePath();
    }

    private void validateTable(Schema schema, Table table, ResultBuilder resultBuilder) {
        if (StringUtils.isEmpty(table.getName())) {
            resultBuilder.message("Table has no name.");
        }

        resultBuilder.addPath(table.getName());

        if (!isIdentifier(table.getName())) {
            resultBuilder.message("Table has invalid name.");
        }
        if (table.getColumns().isEmpty()) {
            resultBuilder.message("Table has no columns.");
        }
        if (table.getPrimaryKey().isEmpty()) {
            resultBuilder.message("Table has no primary key.");
        }

        for (Column column : table.getColumns().values()) {
            validateColumn(schema, table, column, resultBuilder);
        }

        for (Index index : table.getIndexes().values()) {
            validateIndex(schema, table, index, resultBuilder);
        }

        for (ForeignKey foreignKey : table.getForeignKeys().values()) {
            validateForeignKey(schema, table, foreignKey, resultBuilder);
        }

        resultBuilder.removePath();
    }

    private void validateColumn(Schema schema, Table table, Column column, ResultBuilder resultBuilder) {
        if (StringUtils.isEmpty(column.getName())) {
            resultBuilder.message("Column has no name.");
        }

        resultBuilder.addPath(column.getName());

        if (!isIdentifier(column.getName())) {
            resultBuilder.message("Column has invalid name.");
        }
        if (column.getType() == null) {
            resultBuilder.message("Column has no type.");
        } else {
            if (column.getType().isLengthRequired() && column.getLength() <= 0) {
                resultBuilder.message("Column has no length.");
            }
            if (column.getType().isLength2Required() && column.getLength2() <= 0) {
                resultBuilder.message("Column has no length2.");
            }
        }
        if (column.isEnum()) {
            if (!column.getType().isText() && !column.getType().isNumber()) {
                resultBuilder.message("Column has wrong enum type. Type should be text or number type.");
            }
            for (Map.Entry<String, ?> entry : column.getEnumValues().entrySet()) {
                String enumLiteralName = entry.getKey();
                if (!isIdentifier(enumLiteralName)) {
                    resultBuilder.addPath(enumLiteralName).message("Invalid enum literal.").removePath();
                }
                Object enumLiteralValue = entry.getValue();
                if (column.getType().isText() && !(enumLiteralValue instanceof CharSequence)) {
                    resultBuilder.addPath(enumLiteralName).message("Invalid enum literal value. Expecting text.").removePath();
                }
                if (column.getType().isNumber() && !(enumLiteralValue instanceof Integer)) {
                    resultBuilder.addPath(enumLiteralName).message("Invalid enum literal value. Expecting integer.").removePath();
                }
            }
        }

        resultBuilder.removePath();
    }

    private void validateIndex(Schema schema, Table table, Index index, ResultBuilder resultBuilder) {
        if (StringUtils.isEmpty(index.getName())) {
            resultBuilder.message("Index has no name.");
        }

        resultBuilder.addPath(index.getName());

        if (!isIdentifier(index.getName())) {
            resultBuilder.message("Index has invalid name.");
        }
        if (index.getColumns().isEmpty()) {
            resultBuilder.message("Index has no columns.");
        }

        for (String columnName : index.getColumns()) {
            if (!table.getColumns().containsKey(columnName)) {
                resultBuilder.addPath(columnName).message("Index column does not exist.").removePath();
            }
        }

        resultBuilder.removePath();
    }

    private void validateForeignKey(Schema schema, Table table, ForeignKey foreignKey, ResultBuilder resultBuilder) {
        if (StringUtils.isEmpty(foreignKey.getName())) {
            resultBuilder.message("Foreign Key has no name.");
        }

        resultBuilder.addPath(foreignKey.getName());

        if (!isIdentifier(foreignKey.getName())) {
            resultBuilder.message("Foreign Key has invalid name.");
        }
        if (foreignKey.getColumns().isEmpty()) {
            resultBuilder.message("Foreign Key has no columns.");
        }

        Table primaryTable = schema.getTables().get(foreignKey.getReferenceTable());
        if (primaryTable == null) {
            resultBuilder.addPath(foreignKey.getReferenceTable()).message("Foreign Key reference table does not exist.").removePath();
        }

        for (Map.Entry<String, String> columns : foreignKey.getColumns().entrySet()) {
            if (!table.getColumns().containsKey(columns.getKey())) {
                resultBuilder.addPath(columns.getKey()).message("Foreign Key column does not exist.").removePath();
            }
            if (!primaryTable.getColumns().containsKey(columns.getValue())) {
                resultBuilder.addPath(columns.getValue()).message("Foreign Key column does not exist.").removePath();
            }
        }

        resultBuilder.removePath();
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

    record ResultImpl(String path, String message) implements Result {
    }

    public static class ResultBuilder {
        final LinkedList<String> path = new LinkedList<>();
        final List<Result> results = new ArrayList<>();

        public ResultBuilder() {
        }

        public ResultBuilder addPath(String path) {
            this.path.addLast(path);
            return this;
        }

        public ResultBuilder removePath() {
            this.path.removeLast();
            return this;
        }

        public ResultBuilder message(String message) {
            results.add(new ResultImpl(String.join(".", this.path), message));
            return this;
        }

        public List<Result> build() {
            return results;
        }
    }
}
