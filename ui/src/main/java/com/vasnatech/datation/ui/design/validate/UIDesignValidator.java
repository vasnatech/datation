package com.vasnatech.datation.ui.design.validate;

import com.vasnatech.commons.schema.validate.SchemaValidator;
import com.vasnatech.commons.schema.validate.ValidationInfo;
import com.vasnatech.datation.ui.component.schema.BasicPropertyType;
import com.vasnatech.datation.ui.component.schema.Container;
import com.vasnatech.datation.ui.component.schema.EnumPropertyType;
import com.vasnatech.datation.ui.component.schema.Property;
import com.vasnatech.datation.ui.design.schema.*;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.List;

public class UIDesignValidator implements SchemaValidator<UIDesignSchema> {

    @Override
    public List<ValidationInfo> validate(UIDesignSchema schema) {
        ValidationInfo.Builder resultBuilder = ValidationInfo.builder();
        if (!schema.meta().containsKey("@schema-type")) {
            resultBuilder.message("meta @schema-type is missing.");
        }
        if (!schema.meta().containsKey("@schema-version")) {
            resultBuilder.message("meta @schema-version is missing.");
        }

        validateElement(null, schema.root(), resultBuilder);

        return resultBuilder.build();
    }

    void validateElement(Element parent, Element child, ValidationInfo.Builder resultBuilder) {
        resultBuilder.addPath(child.name());

        if (StringUtils.isEmpty(child.name())) {
            resultBuilder.message("element has no name");
        }
        if (child.type() == null) {
            resultBuilder.message("element has no type");
        } else {
            if (!child.type().isContainer() && !child.children().isEmpty()) {
                resultBuilder.message("element is not container but has children");
            }

            child.properties().forEach((key, value) -> {
                resultBuilder.addPath(key);
                Property property = child.type().properties().get(key);
                if (property == null) {
                    resultBuilder.message("unable to find property definition");
                } else if (!validatePropertyValue(property, value)) {
                    resultBuilder.message("unsupported property value");
                }
                resultBuilder.removePath();
            });

            child.containerProperties().forEach((key, value) -> {
                resultBuilder.addPath(key);
                Property property;
                if (parent != null && (parent.type() instanceof Container container)) {
                    property = container.childProperties().get(key);
                    if (property == null) {
                        resultBuilder.message("unable to find property definition");
                    } else if (!validatePropertyValue(property, value)) {
                        resultBuilder.message("unsupported property value");
                    }
                }
                resultBuilder.removePath();
            });
        }

        child.children().values().forEach(element -> validateElement(child, element, resultBuilder));

        resultBuilder.removePath();
    }

    boolean validatePropertyValue(Property property, Object value) {
        if (value == null) {
            return false;
        }
        if (property.type() instanceof EnumPropertyType enumPropertyType) {
            if (value instanceof String stringValue) {
                return enumPropertyType.enumLiterals().contains(stringValue);
            }
            return false;
        } else if (property.type() instanceof BasicPropertyType basicPropertyType) {
            return switch (basicPropertyType) {
                case BOOLEAN -> value instanceof Boolean;
                case INTEGER -> value instanceof Integer;
                case FLOAT -> value instanceof Float;
                case STRING -> value instanceof String;
                case STRING_ARRAY -> value instanceof String[];
                case INSETS -> value instanceof Insets;
                case RECTANGLE -> value instanceof Rectangle;
                case DIMENSION -> value instanceof Dimension;
                case URI -> value instanceof URI;
            };
        }
        return false;
    }
}
