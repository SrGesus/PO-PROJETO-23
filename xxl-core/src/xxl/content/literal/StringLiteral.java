package xxl.content.literal;

import xxl.exceptions.InvalidExpressionException;
import xxl.visitor.Visitable;
import xxl.visitor.Visitor;

/**
 * Represents a value that is a string.
 */
public class StringLiteral extends Literal {

    /** The content string. */
    private String _value;

    /**
     * Constructor to evaluate an expression into an integer content.
     * @param expression to be evaluated.
     * @throws InvalidExpressionException when the expression doesn't evaluate to a String.
     */
    public StringLiteral(String expression) throws InvalidExpressionException {
        /** Verifies if first character is ' */
        if (expression.length() == 0 | expression.charAt(0) != '\'') {
            throw new InvalidExpressionException(expression);
        }
        _value = expression.substring(1);
    }

    /** @see xxl.content.Content#getString() */
    @Override
    public String getString() {
        return _value;
    }

    @Override
    public String toString() {
        return "'" + _value;
    }

    /** @see Visitable#accept(Visitor) */
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitStringLiteral(this);
    }
}
