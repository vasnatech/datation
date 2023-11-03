package com.vasnatech.datation.ui.control.schema;

public abstract class UIControlFactory {

    static UIControlFactory INSTANCE = new DefaultUIControlFactory();

    public static UIControlFactory instance() {
        return INSTANCE;
    }

    public static void instance(UIControlFactory instance) {
        INSTANCE = instance;
    }

    public abstract Control create(String name);
}
