package com.vasnatech.datation.ui.design.schema;

import com.vasnatech.datation.ui.component.schema.Component;

import java.util.LinkedHashMap;
import java.util.Map;

public record Element(
        String name,
        Component type,
        Map<String, Object> properties,
        Map<String, Object> containerProperties,
        Map<String, Element> children
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        Component type;
        Map<String, Object> properties = new LinkedHashMap<>();
        Map<String, Object> containerProperties = new LinkedHashMap<>();
        Map<String, Element> children = new LinkedHashMap<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(Component type) {
            this.type = type;
            return this;
        }

        public Builder properties(Map<String, ?> properties) {
            this.properties.putAll(properties);
            return this;
        }

        public Builder property(String key, Object value) {
            this.properties.put(key, value);
            return this;
        }

        public Builder containerProperties(Map<String, ?> containerProperties) {
            this.containerProperties.putAll(containerProperties);
            return this;
        }

        public Builder containerProperty(String key, Object value) {
            this.containerProperties.put(key, value);
            return this;
        }

        public Builder children(Map<String, Element> children) {
            this.children.putAll(children);
            return this;
        }

        public Builder child(Element element) {
            this.children.put(element.name(), element);
            return this;
        }

        public Element build() {
            return new Element(name, type, properties, containerProperties, children);
        }
    }
}
