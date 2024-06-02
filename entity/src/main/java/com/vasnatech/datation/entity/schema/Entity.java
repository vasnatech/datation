package com.vasnatech.datation.entity.schema;

import com.vasnatech.commons.schema.schema.Append;
import com.vasnatech.commons.schema.schema.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record Entity(
        String name,
        DDL.Table ddl,
        Inherits inherits,
        LinkedHashMap<String, Field> fields,
        List<String> ids,
        LinkedHashMap<String, Append> appends
) implements Node {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String name;
        DDL.Table ddl;
        Inherits inherits;
        LinkedHashMap<String, Field> fields = new LinkedHashMap<>();
        List<String> ids = new ArrayList<>(1);
        LinkedHashMap<String, Append> appends = new LinkedHashMap<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder ddl(DDL.Table ddl) {
            this.ddl = ddl;
            return this;
        }

        public Builder inherits(Inherits inherits) {
            this.inherits = inherits;
            return this;
        }

        public Builder fields(Map<String, Field> fields) {
            this.fields.putAll(fields);
            return this;
        }

        public Builder field(Field field) {
            this.fields.put(field.name(), field);
            return this;
        }

        public Builder ids(List<String> ids) {
            this.ids.addAll(ids);
            return this;
        }

        public Builder id(String id) {
            this.ids.add(id);
            return this;
        }

        public Builder appends(LinkedHashMap<String, Append> appends) {
            this.appends = appends;
            return this;
        }

        public Builder append(Append append) {
            this.appends.put(append.name(), append);
            return this;
        }

        public Entity build() {
            return new Entity(name, ddl, inherits, fields, ids, appends);
        }
    }

    public record Inherits(String base, DDL.RelationColumn ddl) {

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            String base;
            DDL.RelationColumn ddl;

            public Builder base(String base) {
                this.base = base;
                return this;
            }

            public Builder ddl(DDL.RelationColumn ddl) {
                this.ddl = ddl;
                return this;
            }

            public Inherits build() {
                return new Inherits(base, ddl);
            }
        }
    }
}
