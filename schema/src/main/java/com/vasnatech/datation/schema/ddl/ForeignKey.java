package com.vasnatech.datation.schema.ddl;

import java.util.LinkedHashMap;

public class ForeignKey extends Node {

    final String referenceTable;
    final LinkedHashMap<String, String> columns;
    final boolean real;

    public static Builder builder() {
        return new Builder();
    }

    public ForeignKey(String name, String referenceTable, LinkedHashMap<String, String> columns, boolean real) {
        super(name);
        this.referenceTable = referenceTable;
        this.columns = columns;
        this.real = real;
    }

    public String getReferenceTable() {
        return referenceTable;
    }

    public LinkedHashMap<String, String> getColumns() {
        return columns;
    }

    public boolean isReal() {
        return real;
    }

    public static class Builder {
        String name;
        String referenceTable;
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        boolean real = true;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder referenceTable(String referenceTable) {
            this.referenceTable = referenceTable;
            return this;
        }

        public Builder column(String column, String referenceColumn) {
            this.columns.put(column, referenceColumn);
            return this;
        }

        public Builder real(boolean real) {
            this.real = real;
            return this;
        }

        public ForeignKey build() {
            return new ForeignKey(name, referenceTable, columns, real);
        }
    }
}
