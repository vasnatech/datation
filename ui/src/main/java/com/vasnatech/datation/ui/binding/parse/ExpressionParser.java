package com.vasnatech.datation.ui.binding.parse;

import com.vasnatech.commons.schema.parse.ParserException;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public final class ExpressionParser {

    final static ExpressionParser INSTANCE = new ExpressionParser();

    public static ExpressionParser instance() {
        return INSTANCE;
    }

    final org.springframework.expression.ExpressionParser expressionParser;

    private ExpressionParser() {
        expressionParser = new SpelExpressionParser();
    }

    public Expression create(String expressionAsText) {
        try {
            return expressionParser.parseExpression(expressionAsText);
        } catch (org.springframework.expression.ParseException e) {
            throw new ParserException("Unable to parse expression: " + expressionAsText, e);
        }
    }
}
