package com.vasnatech.datation.ddl.schema;

import com.vasnatech.datation.schema.Node;

import java.util.ArrayList;
import java.util.List;

public class Index extends Node {

    final boolean unique;
    final List<String> columns;
    final boolean real;

    public Index(String name, boolean unique, List<String> columns, boolean real) {
        super(name);
        this.unique = unique;
        this.columns = columns;
        this.real = real;
    }

    public boolean isUnique() {
        return unique;
    }

    public List<String> getColumns() {
        return columns;
    }

    public boolean isReal() {
        return real;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        boolean unique = false;
        List<String> columns = new ArrayList<>(2);
        boolean real = true;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder unique(boolean unique) {
            this.unique = unique;
            return this;
        }

        public Builder column(String column) {
            this.columns.add(column);
            return this;
        }

        public Builder real(boolean real) {
            this.real = real;
            return this;
        }

        public Index build() {
            return new Index(name, unique, columns, real);
        }
    }
}
