package com.vasnatech.datation.ui.component.schema;

public record Property(String name, PropertyType type, Object defaultValue, String title) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        PropertyType propertyType;
        Object defaultValue;
        String title;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder propertyType(PropertyType propertyType) {
            this.propertyType = propertyType;
            return this;
        }

        public Builder propertyType(Object propertyType) {
            this.propertyType = PropertyType.of(propertyType);
            return this;
        }

        public Builder defaultValue(Object defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Property build() {
            return new Property(name, propertyType, defaultValue, title);
        }
    }
}
