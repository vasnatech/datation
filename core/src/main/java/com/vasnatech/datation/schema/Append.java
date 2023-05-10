package com.vasnatech.datation.schema;

public class Append {

    final String name;
    final Anchor anchor;

    public Append(String name, Anchor anchor) {
        this.name = name;
        this.anchor = anchor;
    }

    public String getName() {
        return name;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        Anchor anchor;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder anchor(Anchor anchor) {
            this.anchor = anchor;
            return this;
        }

        public Append build() {
            return new Append(name, anchor);
        }
    }
}
