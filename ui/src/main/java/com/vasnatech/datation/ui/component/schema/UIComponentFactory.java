package com.vasnatech.datation.ui.component.schema;

public abstract class UIComponentFactory {

    static UIComponentFactory INSTANCE = new DefaultUIComponentFactory();

    public static UIComponentFactory instance() {
        return INSTANCE;
    }

    public static void instance(UIComponentFactory instance) {
        INSTANCE = instance;
    }

    public abstract Control control(String name);

    public abstract Container container(String name);
}
