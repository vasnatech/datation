package com.vasnatech.datation.ddl.schema;

import com.vasnatech.commons.schema.schema.Node;

import java.util.LinkedHashMap;

public record ForeignKey(
        String name,
        String referenceTable,
        LinkedHashMap<String, String> columns,
        boolean real
) implements Node {

    public static Builder builder() {
        return new Builder();
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
