package com.vasnatech.datation.ui.component.schema;

import java.util.LinkedHashMap;
import java.util.Map;

public record Container(String name, Map<String, Property> properties, Map<String, Property> childProperties, Map<String, Event> events) implements Component {

    @Override
    public boolean isContainer() {
        return true;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        Map<String, Property> properties = new LinkedHashMap<>();
        Map<String, Property> childProperties = new LinkedHashMap<>();
        Map<String, Event> events = new LinkedHashMap<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder properties(Map<String, Property> properties) {
            this.properties.putAll(properties);
            return this;
        }

        public Builder property(Property property) {
            this.properties.put(property.name(), property);
            return this;
        }

        public Builder property(String name) {
            this.properties.put(name, null);
            return this;
        }

        public Builder childProperties(Map<String, Property> properties) {
            this.childProperties.putAll(properties);
            return this;
        }

        public Builder childProperty(Property property) {
            this.childProperties.put(property.name(), property);
            return this;
        }

        public Builder childProperty(String name) {
            this.childProperties.put(name, null);
            return this;
        }

        public Builder events(Map<String, Event> events) {
            this.events.putAll(events);
            return this;
        }

        public Builder event(Event event) {
            this.events.put(event.name(), event);
            return this;
        }

        public Builder event(String name) {
            this.events.put(name, null);
            return this;
        }

        public Container build() {
            return new Container(name, properties, childProperties, events);
        }
    }
}
