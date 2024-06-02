package com.vasnatech.datation.ui.binding.schema;

import com.vasnatech.commons.schema.schema.Node;
import com.vasnatech.datation.ui.component.schema.Control;
import org.springframework.expression.Expression;

import java.util.StringJoiner;

public record Field(
        String name,
        Control control,
        Expression getExpression,
        Expression setExpression,
        Expression dataSourceExpression
) implements Node {

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
