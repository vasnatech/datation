package com.vasnatech.datation.ui.design.schema;

import com.vasnatech.commons.schema.schema.AbstractSchema;

import java.util.LinkedHashMap;
import java.util.Map;

public class UIDesignSchema extends AbstractSchema {

    final Element root;

    public UIDesignSchema(String name, LinkedHashMap<String, String> meta, Element root) {
        super(name, "ui-design", meta);
        this.root = root;
    }

    public Element root() {
        return root;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        LinkedHashMap<String, String> meta = new LinkedHashMap<>();
        Element element;

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

        public Builder element(Element element) {
            this.element = element;
            return this;
        }

        public UIDesignSchema build() {
            return new UIDesignSchema(name, meta, element);
        }
    }
}
