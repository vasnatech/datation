package com.vasnatech.datation.ui.binding.schema;

import com.vasnatech.datation.schema.AbstractSchema;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

public class UIBindingSchema extends AbstractSchema {

    final LinkedHashMap<String, Field> fields;

    public UIBindingSchema(String name, LinkedHashMap<String, String> meta, LinkedHashMap<String, Field> fields) {
        super(name, "ui-binding", meta);
        this.fields = fields;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UIBindingSchema.class.getSimpleName() + "{", "}")
                .add("type='" + type + "'")
                .add("name='" + name + "'")
                .add("meta=" + meta)
                .add("fields=" + fields)
                .toString();
    }

    public LinkedHashMap<String, Field> getFields() {
        return fields;
    }

    public Field getField(String name) {
        return fields.get(name);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name = "controls";
        LinkedHashMap<String, String> meta = new LinkedHashMap<>();
        LinkedHashMap<String, Field> fields = new LinkedHashMap<>();

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

        public Builder fields(Map<String, Field> fields) {
            this.fields.putAll(fields);
            return this;
        }

        public Builder field(Field field) {
            this.fields.put(field.name, field);
            return this;
        }

        public UIBindingSchema build() {
            return new UIBindingSchema(name, meta, fields);
        }
    }
}
