package com.vasnatech.datation.ui.component.schema;

import com.vasnatech.commons.schema.schema.Node;

import java.util.LinkedHashMap;
import java.util.Map;

public record Event(String name, Map<String, ?> properties) implements Node {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        String name;
        Map<String, Object> properties = new LinkedHashMap<>();

        public Builder name(String name) {
            this.name = name;
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

        public Event build() {
            return new Event(name, properties);
        }
    }
}
