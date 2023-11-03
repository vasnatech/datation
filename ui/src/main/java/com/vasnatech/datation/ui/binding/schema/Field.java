package com.vasnatech.datation.ui.binding.schema;

import com.vasnatech.datation.ui.binding.parse.ExpressionParser;
import com.vasnatech.datation.ui.control.schema.Control;
import com.vasnatech.datation.ui.control.schema.UIControlFactory;
import org.springframework.expression.Expression;

import java.util.StringJoiner;

public class Field {
    final String name;
    final Control control;
    final Expression getExpression;
    final Expression setExpression;
    final Expression dataSourceExpression;

    public Field(String name, Control control, Expression getExpression, Expression setExpression, Expression dataSourceExpression) {
        this.name = name;
        this.control = control;
        this.getExpression = getExpression;
        this.setExpression = setExpression;
        this.dataSourceExpression = dataSourceExpression;
    }

    public String getName() {
        return name;
    }

    public Control getControl() {
        return control;
    }

    public Expression getGetExpression() {
        return getExpression;
    }

    public Expression getSetExpression() {
        return setExpression;
    }

    public Expression getDataSourceExpression() {
        return dataSourceExpression;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Field.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("control=" + control)
                .add("getExpression=" + getExpression.getExpressionString())
                .add("setExpression=" + (setExpression == null ? null : setExpression.getExpressionString()))
                .add("dataSourceExpression=" + (dataSourceExpression == null ? null : dataSourceExpression.getExpressionString()))
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        String name;
        Control control;
        Expression getExpression;
        Expression setExpression;
        Expression dataSourceExpression;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder control(Control control) {
            this.control = control;
            return this;
        }

        public Builder binding(Expression expression) {
            this.getExpression = expression;
            this.setExpression = expression;
            return this;
        }

        public Builder getter(Expression expression) {
            this.getExpression = expression;
            return this;
        }

        public Builder setter(Expression expression) {
            this.setExpression = expression;
            return this;
        }

        public Builder dataSource(Expression dataSourceExpression) {
            this.dataSourceExpression = dataSourceExpression;
            return this;
        }

        public Field build() {
            return new Field(name, control, getExpression, setExpression, dataSourceExpression);
        }
    }
}
