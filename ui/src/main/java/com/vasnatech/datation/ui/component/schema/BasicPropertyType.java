package com.vasnatech.datation.ui.component.schema;

import com.vasnatech.datation.ui.design.schema.Dimension;
import com.vasnatech.datation.ui.design.schema.Insets;
import com.vasnatech.datation.ui.design.schema.Rectangle;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum BasicPropertyType implements PropertyType {
    BOOLEAN("boolean") {
        public Boolean parseValue(String s) {
            return s == null ? null : Boolean.valueOf(s);
        }
    },
    INTEGER("integer") {
        public Integer parseValue(String s) {
            return s == null ? null : Integer.valueOf(s);
        }
    },
    FLOAT("float") {
        public Float parseValue(String s) {
            return s == null ? null : Float.valueOf(s);
        }
    },
    STRING("string") {
        public String parseValue(String s) {
            return s;
        }
    },
    STRING_ARRAY("string[]") {
        public String[] parseValue(String s) {
            return s == null ? null : s.split(",");
        }
    },
    INSETS("insets") {
        public Insets parseValue(String s) {
            return s == null ? null : Insets.valueOf(s);
        }
    },
    RECTANGLE("rectangle") {
        public Rectangle parseValue(String s) {
            return s == null ? null : Rectangle.valueOf(s);
        }
    },
    DIMENSION("dimension") {
        public Dimension parseValue(String s) {
            return s == null ? null : Dimension.valueOf(s);
        }
    },
    URI("uri") {
        public java.net.URI parseValue(String s) {
            return s == null ? null : java.net.URI.create(s);
        }
    }
    ;

    final String key;

    BasicPropertyType(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }

    static final Map<String, BasicPropertyType> MAP = Stream.of(values()).collect(Collectors.toMap(BasicPropertyType::key, it -> it));

    public static BasicPropertyType findByKey(String key) {
        return MAP.get(key);
    }
}
