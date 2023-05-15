package com.vasnatech.datation.entity.schema;

public enum FieldTypeGroup {

    BOOLEAN {
        @Override
        public boolean isBoolean() {
            return true;
        }
    },
    NUMBER {
        @Override
        public boolean isNumber() {
            return true;
        }
    },
    TEXT {
        @Override
        public boolean isText() {
            return true;
        }
    },
    DATE {
        @Override
        public boolean isDate() {
            return true;
        }
    },
    TIME {
        @Override
        public boolean isTime() {
            return true;
        }
    },
    ARRAY {
        @Override
        public boolean isArray() {
            return true;
        }

        @Override
        public boolean isPrimitive() {
            return false;
        }
    },
    COLLECTION {
        @Override
        public boolean isCollection() {
            return true;
        }

        @Override
        public boolean isPrimitive() {
            return false;
        }
    },
    OBJECT {
        @Override
        public boolean isObject() {
            return true;
        }

        @Override
        public boolean isPrimitive() {
            return false;
        }
    };

    public boolean isBoolean() {
        return false;
    }
    public boolean isNumber() {
        return false;
    }
    public boolean isText() {
        return false;
    }
    public boolean isDate() {
        return false;
    }
    public boolean isTime() {
        return false;
    }
    public boolean isDateTime() {
        return isDate() && isTime();
    }
    public boolean isArray() {
        return false;
    }
    public boolean isCollection() {
        return false;
    }
    public boolean isObject() {
        return false;
    }

    public boolean isPrimitive() {
        return true;
    }
}
