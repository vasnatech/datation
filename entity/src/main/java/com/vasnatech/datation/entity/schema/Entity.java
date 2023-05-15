package com.vasnatech.datation.entity.schema;

import com.vasnatech.datation.schema.Append;
import com.vasnatech.datation.schema.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Entity extends Node {

    final DDL.Table ddl;
    final Inherits inherits;
    final LinkedHashMap<String, Field> fields;
    final List<String> ids;
    final LinkedHashMap<String, Append> appends;

    public Entity(
            String name,
            DDL.Table ddl,
            Inherits inherits,
            LinkedHashMap<String, Field> fields,
            List<String> ids,
            LinkedHashMap<String, Append> appends
    ) {
        super(name);
        this.ddl = ddl;
        this.inherits = inherits;
        this.fields = fields;
        this.ids = ids;
        this.appends = appends;
    }

    public DDL.Table getDDL() {
        return ddl;
    }

    public Inherits getInherits() {
        return inherits;
    }

    public Map<String, Field> getFields() {
        return fields;
    }

    public List<String> getIds() {
        return ids;
    }

    public LinkedHashMap<String, Append> getAppends() {
        return appends;
    }

    public Builder toBuilder() {
        return new Builder()
                .name(name)
                .ddl(ddl)
                .inherits(inherits)
                .fields(fields)
                .ids(ids);
    }

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
            this.fields.put(field.getName(), field);
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
            this.appends.put(append.getName(), append);
            return this;
        }

        public Entity build() {
            return new Entity(name, ddl, inherits, fields, ids, appends);
        }
    }

    public static class Inherits {
        final String base;
        final DDL.RelationColumn ddl;

        public Inherits(String base, DDL.RelationColumn ddl) {
            this.base = base;
            this.ddl = ddl;
        }

        public String getBase() {
            return base;
        }

        public DDL.RelationColumn getDDL() {
            return ddl;
        }

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
