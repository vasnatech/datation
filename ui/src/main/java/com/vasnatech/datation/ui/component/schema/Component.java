package com.vasnatech.datation.ui.component.schema;

import java.util.Map;

public interface Component {

    boolean isContainer();

    String name();

    Map<String, Property> properties();

    Map<String, Event> events();
}
