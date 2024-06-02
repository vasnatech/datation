package com.vasnatech.datation.ui.component.schema;

import com.vasnatech.commons.schema.schema.Node;

import java.util.Map;

public interface Component extends Node {

    boolean isContainer();

    Map<String, Property> properties();

    Map<String, Event> events();
}
