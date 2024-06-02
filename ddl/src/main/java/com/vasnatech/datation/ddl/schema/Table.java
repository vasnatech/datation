package com.vasnatech.datation.ddl.schema;

import com.vasnatech.commons.schema.schema.Append;
import com.vasnatech.commons.schema.schema.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record Table(
        String name,
        LinkedHashMap<String, Column> columns,
        List<String> primaryKey,
        LinkedHashMap<String, Index> indexes,
        LinkedHashMap<String, ForeignKey> foreignKeys,
        LinkedHashMap<String, Append> appends
) implements Node {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        LinkedHashMap<String, Column> columns = new LinkedHashMap<>();
        List<String> primaryKey = new ArrayList<>(1);
        LinkedHashMap<String, Index> indexes = new LinkedHashMap<>();
        LinkedHashMap<String, ForeignKey> foreignKeys = new LinkedHashMap<>();
        LinkedHashMap<String, Append> appends = new LinkedHashMap<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder column(Column column) {
            this.columns.put(column.name(), column);
            return this;
        }

        public Builder columns(Map<String, Column> columns) {
            this.columns.putAll(columns);
            return this;
        }

        public Builder primaryKey(String columnName) {
            this.primaryKey.add(columnName);
            return this;
        }

        public Builder primaryKey(List<String> primaryKey) {
            this.primaryKey.addAll(primaryKey);
            return this;
        }

        public Builder index(Index index) {
            this.indexes.put(index.name(), index);
            return this;
        }

        public Builder indexes(Map<String, Index> indexes) {
            this.indexes.putAll(indexes);
            return this;
        }

        public Builder foreignKey(ForeignKey foreignKey) {
            this.foreignKeys.put(foreignKey.name(), foreignKey);
            return this;
        }

        public Builder foreignKeys(LinkedHashMap<String, ForeignKey> foreignKeys) {
            this.foreignKeys.putAll(foreignKeys);
            return this;
        }

        public Builder append(Append append) {
            this.appends.put(append.name(), append);
            return this;
        }

        public Table build() {
            return new Table(name, columns, primaryKey, indexes, foreignKeys, appends);
        }
    }
}
