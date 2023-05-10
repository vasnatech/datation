package com.vasnatech.datation.validate;

import com.vasnatech.datation.DatationException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ValidationException extends DatationException {

    final List<ValidationInfo> validationInfoList;

    public ValidationException() {
        this(List.of());
    }

    public ValidationException(List<ValidationInfo> validationInfoList) {
        super(validationInfoList.stream().map(it -> it.message() + " @ " + it.path()).collect(Collectors.joining(", ")));
        this.validationInfoList = validationInfoList;
    }

    public List<ValidationInfo> getValidationInfoList() {
        return Collections.unmodifiableList(validationInfoList);
    }
}
