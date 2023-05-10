package com.vasnatech.datation.entity.schema;

import com.vasnatech.datation.schema.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Entity extends Node {

    final String ddlTable;
    final String inheritedEntity;
    final Map<String, Field> fields;
    final List<String> ids;

    public Entity(String name, String ddlTable, String inheritedEntity, Map<String, Field> fields, List<String> ids) {
        super(name);
        this.ddlTable = ddlTable;
        this.inheritedEntity = inheritedEntity;
        this.fields = fields;
        this.ids = ids;
    }

    public String getDdlTable() {
        return ddlTable;
    }

    public String getInheritedEntity() {
        return inheritedEntity;
    }

    public Map<String, Field> getFields() {
        return fields;
    }

    public List<String> getIds() {
        return ids;
    }

    public static class Builder {
        String name;
        String ddlTable;
        String inheritedEntity;
        Map<String, Field> fields = new LinkedHashMap<>();
        List<String> ids = new ArrayList<>(1);

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder ddlTable(String ddlTable) {
            this.ddlTable = ddlTable;
            return this;
        }

        public Builder inheritedEntity(String inheritedEntity) {
            this.inheritedEntity = inheritedEntity;
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

        public Entity build() {
            return new Entity(name, ddlTable, inheritedEntity, fields, ids);
        }
    }
}
