package com.vasnatech.datation.entity.schema;

import java.util.EnumSet;
import java.util.function.Predicate;

public enum FieldType {

    CHARACTER(FieldTypeGroup.TEXT),
    STRING(FieldTypeGroup.TEXT),
    BOOLEAN(FieldTypeGroup.BOOLEAN),
    BYTE(FieldTypeGroup.NUMBER),
    INT16(FieldTypeGroup.NUMBER),
    INT32(FieldTypeGroup.NUMBER),
    INT64(FieldTypeGroup.NUMBER),
    INTEGER(FieldTypeGroup.NUMBER),
    FLOAT(FieldTypeGroup.NUMBER),
    DOUBLE(FieldTypeGroup.NUMBER),
    DECIMAL(FieldTypeGroup.NUMBER),
    DATE(FieldTypeGroup.DATE),
    TIME(FieldTypeGroup.TIME),
    DATETIME(FieldTypeGroup.DATE, FieldTypeGroup.TIME),
    INSTANT(FieldTypeGroup.DATE, FieldTypeGroup.TIME),
    BYTE_ARRAY(FieldTypeGroup.ARRAY),
    CHAR_ARRAY(FieldTypeGroup.ARRAY),
    LIST(FieldTypeGroup.COLLECTION),
    SET(FieldTypeGroup.COLLECTION),
    COLLECTION(FieldTypeGroup.COLLECTION),
    ENTITY(FieldTypeGroup.OBJECT)
    ;

    final EnumSet<FieldTypeGroup> groups;

    FieldType(FieldTypeGroup first, FieldTypeGroup... rest) {
        this.groups = EnumSet.of(first, rest);
    }

    public EnumSet<FieldTypeGroup> getGroups() {
        return groups;
    }

    public boolean isBoolean() {
        return isInGroup(FieldTypeGroup::isBoolean);
    }
    public boolean isNumber() {
        return isInGroup(FieldTypeGroup::isNumber);
    }
    public boolean isText() {
        return isInGroup(FieldTypeGroup::isText);
    }
    public boolean isDate() {
        return isInGroup(FieldTypeGroup::isDate);
    }
    public boolean isTime() {
        return isInGroup(FieldTypeGroup::isTime);
    }
    public boolean isDateTime() {
        return isInGroup(FieldTypeGroup::isDateTime);
    }
    public boolean isArray() {
        return isInGroup(FieldTypeGroup::isArray);
    }
    public boolean isCollection() {
        return isInGroup(FieldTypeGroup::isCollection);
    }
    public boolean isObject() {
        return isInGroup(FieldTypeGroup::isObject);
    }
    private boolean isInGroup(Predicate<? super FieldTypeGroup> predicate) {
        return groups.stream().anyMatch(predicate);
    }

    public boolean isPrimitive() {
        return groups.stream().allMatch(FieldTypeGroup::isPrimitive);
    }
}
