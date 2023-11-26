package com.vasnatech.datation.ui.component.schema;

import com.vasnatech.commons.schema.schema.AbstractSchema;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

public class UIComponentSchema extends AbstractSchema {

    final LinkedHashMap<String, Property> properties;
    final LinkedHashMap<String, Event> events;
    final LinkedHashMap<String, Control> controls;
    final LinkedHashMap<String, Container> containers;

    public UIComponentSchema(String name, LinkedHashMap<String, String> meta, LinkedHashMap<String, Property> properties, LinkedHashMap<String, Event> events, LinkedHashMap<String, Control> controls, LinkedHashMap<String, Container> containers) {
        super(name, "ui-control", meta);
        this.properties = properties;
        this.events = events;
        this.controls = controls;
        this.containers = containers;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UIComponentSchema.class.getSimpleName() + "{", "}")
                .add("type='" + type + "'")
                .add("name='" + name + "'")
                .add("meta=" + meta)
                .toString();
    }

    public LinkedHashMap<String, Property> getProperties() {
        return properties;
    }

    public LinkedHashMap<String, Event> getEvents() {
        return events;
    }

    public LinkedHashMap<String, Control> getControls() {
        return controls;
    }

    public Control getControl(String name) {
        return controls.get(name);
    }

    public LinkedHashMap<String, Container> getContainers() {
        return containers;
    }

    public Container getContainer(String name) {
        return containers.get(name);
    }

    public Component getComponent(String name) {
        return controls.containsKey(name) ? controls.get(name) : containers.get(name);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name = "controls";
        LinkedHashMap<String, String> meta = new LinkedHashMap<>();
        LinkedHashMap<String, Property> properties = new LinkedHashMap<>();
        LinkedHashMap<String, Event> events = new LinkedHashMap<>();
        LinkedHashMap<String, Control> controls = new LinkedHashMap<>();
        LinkedHashMap<String, Container> containers = new LinkedHashMap<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder meta(Map<String, String> meta) {
            this.meta.putAll(meta);
            return this;
        }

        public Builder meta(String key, String value) {
            this.meta.put(key, value);
            return this;
        }

        public Builder properties(LinkedHashMap<String, Property> properties) {
            this.properties.putAll(properties);
            return this;
        }

        public Builder property(Property property) {
            this.properties.put(property.name(), property);
            return this;
        }

        public Builder events(LinkedHashMap<String, Event> events) {
            this.events.putAll(events);
            return this;
        }

        public Builder event(Event event) {
            this.events.put(event.name(), event);
            return this;
        }

        public Builder controls(LinkedHashMap<String, Control> controls) {
            this.controls.putAll(controls);
            return this;
        }

        public Builder control(Control control) {
            this.controls.put(control.name(), control);
            return this;
        }

        public Builder containers(LinkedHashMap<String, Container> containers) {
            this.containers.putAll(containers);
            return this;
        }

        public Builder container(Container container) {
            this.containers.put(container.name(), container);
            return this;
        }

        public UIComponentSchema build() {
            return new UIComponentSchema(name, meta, properties, events, controls, containers);
        }
    }
}
