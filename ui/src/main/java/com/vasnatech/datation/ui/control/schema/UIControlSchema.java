package com.vasnatech.datation.ui.control.schema;

import com.vasnatech.commons.schema.schema.AbstractSchema;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

public class UIControlSchema extends AbstractSchema {

    final LinkedHashMap<String, Control> controls;

    public UIControlSchema(String name, LinkedHashMap<String, String> meta, LinkedHashMap<String, Control> controls) {
        super(name, "ui-control", meta);
        this.controls = controls;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UIControlSchema.class.getSimpleName() + "{", "}")
                .add("type='" + type + "'")
                .add("name='" + name + "'")
                .add("meta=" + meta)
                .add("controls=" + controls)
                .toString();
    }

    public LinkedHashMap<String, Control> getControls() {
        return controls;
    }

    public Control getControl(String name) {
        return controls.get(name);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name = "controls";
        LinkedHashMap<String, String> meta = new LinkedHashMap<>();
        LinkedHashMap<String, Control> controls = new LinkedHashMap<>();

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

        public Builder controls(LinkedHashMap<String, Control> controls) {
            this.controls.putAll(controls);
            return this;
        }

        public Builder control(Control control) {
            this.controls.put(control.name(), control);
            return this;
        }

        public Builder control(String name, String info) {
            control(new Control(name, info));
            return this;
        }

        public UIControlSchema build() {
            return new UIControlSchema(name, meta, controls);
        }
    }
}
