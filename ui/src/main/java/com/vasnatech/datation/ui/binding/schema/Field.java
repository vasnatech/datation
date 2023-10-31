package com.vasnatech.datation.ui.binding.schema;

import com.vasnatech.commons.expression.Expression;
import com.vasnatech.datation.ui.control.schema.Control;

public class Field {
    final String name;
    final String controlName;
    final String getBinding;
    final String setBinding;
    final String dataSourceBinding;

    Control control;
    Expression getExpression;
    Expression setExpression;
    Expression dataSourceExpression;

    public Field(String name, String controlName, String getBinding, String setBinding, String dataSourceBinding) {
        this.name = name;
        this.controlName = controlName;
        this.getBinding = getBinding;
        this.setBinding = setBinding;
        this.dataSourceBinding = dataSourceBinding;
    }

    public String getName() {
        return name;
    }

    public String getControlName() {
        return controlName;
    }

    public String getGetBinding() {
        return getBinding;
    }

    public String getSetBinding() {
        return setBinding;
    }

    public String getDataSourceBinding() {
        return dataSourceBinding;
    }

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public Expression getGetExpression() {
        return getExpression;
    }

    public void setGetExpression(Expression getExpression) {
        this.getExpression = getExpression;
    }

    public Expression getSetExpression() {
        return setExpression;
    }

    public void setSetExpression(Expression setExpression) {
        this.setExpression = setExpression;
    }

    public Expression getDataSourceExpression() {
        return dataSourceExpression;
    }

    public void setDataSourceExpression(Expression dataSourceExpression) {
        this.dataSourceExpression = dataSourceExpression;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        String name;
        String controlName;
        String getBinding;
        String setBinding;
        String dataSourceBinding;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder control(String controlName) {
            this.controlName = controlName;
            return this;
        }

        public Builder binding(String binding) {
            this.getBinding = binding;
            this.setBinding = binding;
            return this;
        }

        public Builder getter(String getBinding) {
            this.getBinding = getBinding;
            return this;
        }

        public Builder setter(String setBinding) {
            this.setBinding = setBinding;
            return this;
        }

        public Builder dataSource(String dataSourceBinding) {
            this.dataSourceBinding = dataSourceBinding;
            return this;
        }

        public Field build() {
            return new Field(name, controlName, getBinding, setBinding, dataSourceBinding);
        }
    }
}
