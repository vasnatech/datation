package com.vasnatech.datation.ui.component.schema;

import java.util.LinkedHashMap;
import java.util.Map;

public record Control(String name, Map<String, Property> properties, Map<String, Event> events) implements Component {

    @Override
    public boolean isContainer() {
        return false;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        Map<String, Property> properties = new LinkedHashMap<>();
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

        public Control build() {
            return new Control(name, properties, events);
        }
    }
}
