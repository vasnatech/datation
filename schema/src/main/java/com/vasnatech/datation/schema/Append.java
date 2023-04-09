package com.vasnatech.datation.schema;

public class Append {

    final String name;
    final Anchor columnAnchor;

    public Append(String name, Anchor columnAnchor) {
        this.name = name;
        this.columnAnchor = columnAnchor;
    }

    public String getName() {
        return name;
    }

    public Anchor getColumnAnchor() {
        return columnAnchor;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        Anchor columnAnchor;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder columnAnchor(Anchor columnAnchor) {
            this.columnAnchor = columnAnchor;
            return this;
        }

        public Append build() {
            return new Append(name, columnAnchor);
        }
    }
}
