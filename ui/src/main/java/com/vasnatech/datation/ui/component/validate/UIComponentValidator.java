package com.vasnatech.datation.ui.component.validate;

import com.vasnatech.datation.ui.component.schema.*;
import com.vasnatech.commons.schema.validate.SchemaValidator;
import com.vasnatech.commons.schema.validate.ValidationInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class UIComponentValidator implements SchemaValidator<UIComponentSchema> {

    @Override
    public List<ValidationInfo> validate(UIComponentSchema schema) {
        ValidationInfo.Builder resultBuilder = ValidationInfo.builder();
        if (!schema.meta().containsKey("@schema-type")) {
            resultBuilder.message("meta @schema-type is missing.");
        }
        if (!schema.meta().containsKey("@schema-version")) {
            resultBuilder.message("meta @schema-version is missing.");
        }
        if (StringUtils.isEmpty(schema.name())) {
            resultBuilder.message("schema name is missing");
        }
        if (schema.properties().isEmpty()) {
            resultBuilder.message("schema has no properties");
        }
        if (schema.controls().isEmpty()) {
            resultBuilder.message("schema has no controls");
        }
        if (schema.containers().isEmpty()) {
            resultBuilder.message("schema has no containers");
        }

        resultBuilder.addPath("properties");
        schema.properties().values().forEach(property -> validateProperty(property, resultBuilder));
        resultBuilder.removePath();

        resultBuilder.addPath("events");
        schema.events().values().forEach(event -> validateEvent(event, resultBuilder));
        resultBuilder.removePath();

        resultBuilder.addPath("controls");
        schema.controls().values().forEach(control -> validateControl(control, resultBuilder));
        resultBuilder.removePath();

        resultBuilder.addPath("container");
        schema.containers().values().forEach(container -> validateContainer(container, resultBuilder));
        resultBuilder.removePath();

        return resultBuilder.build();
    }

    void validateProperty(Property property, ValidationInfo.Builder resultBuilder) {
        resultBuilder.addPath(property.name());
        if (StringUtils.isEmpty(property.name())) {
            resultBuilder.message("property has no name");
        }
        if (property.type() == null) {
            resultBuilder.message("property has no type");
        } else if (property.type() instanceof EnumPropertyType enumPropertyType) {
            if (enumPropertyType.enumLiterals().isEmpty()) {
                resultBuilder.message("property has no enum literals");
            }
        }
        if (StringUtils.isEmpty(property.title())) {
            resultBuilder.message("property has no title");
        }
        resultBuilder.removePath();
    }

    void validateEvent(Event event, ValidationInfo.Builder resultBuilder) {
        resultBuilder.addPath(event.name());
        if (StringUtils.isEmpty(event.name())) {
            resultBuilder.message("event has no name");
        }
        resultBuilder.removePath();
    }

    void validateControl(Control control, ValidationInfo.Builder resultBuilder) {
        resultBuilder.addPath(control.name());
        if (StringUtils.isEmpty(control.name())) {
            resultBuilder.message("control has no name");
        }
        if (control.properties().isEmpty()) {
            resultBuilder.message("control has no properties");
        } else {
            resultBuilder.addPath("properties");
            control.properties().forEach((name, property) -> {
                if (StringUtils.isEmpty(name)) {
                    resultBuilder.message("control has an empty property");
                } else if (property == null) {
                    resultBuilder.addPath(name);
                    resultBuilder.message("property does not exists");
                    resultBuilder.removePath();
                }
            });
            resultBuilder.removePath();
        }

        resultBuilder.addPath("events");
        control.events().forEach((name, event) -> {
            if (StringUtils.isEmpty(name)) {
                resultBuilder.message("control has an empty event");
            } else if (event == null) {
                resultBuilder.addPath(name);
                resultBuilder.message("event does not exists");
                resultBuilder.removePath();
            }
        });
        resultBuilder.removePath();

        resultBuilder.removePath();
    }

    void validateContainer(Container container, ValidationInfo.Builder resultBuilder) {
        resultBuilder.addPath(container.name());
        if (StringUtils.isEmpty(container.name())) {
            resultBuilder.message("container has no name");
        }
        if (container.properties().isEmpty()) {
            resultBuilder.message("container has no properties");
        } else {
            resultBuilder.addPath("properties");
            container.properties().forEach((name, property) -> {
                if (StringUtils.isEmpty(name)) {
                    resultBuilder.message("container has an empty property");
                } else if (property == null) {
                    resultBuilder.addPath(name);
                    resultBuilder.message("property does not exists");
                    resultBuilder.removePath();
                }
            });
            resultBuilder.removePath();
        }

        if (container.childProperties().isEmpty()) {
            resultBuilder.message("container has no properties");
        } else {
            resultBuilder.addPath("child-properties");
            container.childProperties().forEach((name, property) -> {
                if (StringUtils.isEmpty(name)) {
                    resultBuilder.message("container has an empty child property");
                } else if (property == null) {
                    resultBuilder.addPath(name);
                    resultBuilder.message("property does not exists");
                    resultBuilder.removePath();
                }
            });
            resultBuilder.removePath();
        }
        resultBuilder.addPath("events");
        container.events().forEach((name, event) -> {
            if (StringUtils.isEmpty(name)) {
                resultBuilder.message("container has an empty event");
            } else if (event == null) {
                resultBuilder.addPath(name);
                resultBuilder.message("event does not exists");
                resultBuilder.removePath();
            }
        });
        resultBuilder.removePath();

        resultBuilder.removePath();
    }
}
