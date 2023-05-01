package com.vasnatech.datation.schema.ddl;

public enum ColumnType {

    CHAR(true, false, true, false),
    VARCHAR(true, false, true, false),
    BOOL(false, false, false, false),
    BYTE(false, true, false, false),
    INT16(false, true, false, false),
    INT32(false, true, false, false),
    INT64(false, true, false, false),
    INTEGER(false, true, false, false),
    FLOAT(false, true, false, false),
    DOUBLE(false, true, false, false),
    DECIMAL(false, true, true, false),
    DATE(false, false, false, false),
    TIME(false, false, false, false),
    DATETIME(false, false, false, false),
    TIMESTAMP(false, false, false, false),
    BLOB(false, false, false, false),
    CLOB(true, false, false, false);

    final boolean lengthRequired;
    final boolean length2Required;
    final boolean text;
    final boolean number;

    ColumnType(
            boolean text,
            boolean number,
            boolean lengthRequired,
            boolean length2Required
    ) {
        this.text = text;
        this.number = number;
        this.lengthRequired = lengthRequired;
        this.length2Required = length2Required;
    }

    public boolean isText() {
        return text;
    }

    public boolean isNumber() {
        return number;
    }

    public boolean isLengthRequired() {
        return lengthRequired;
    }

    public boolean isLength2Required() {
        return length2Required;
    }
}
