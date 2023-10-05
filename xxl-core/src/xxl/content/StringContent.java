package xxl.content;

import xxl.exceptions.InvalidExpressionException;
import xxl.exceptions.UnexpectedContentException;

/**
 * Represents a value that is a string.
 */
public class StringContent extends Content {

    /** The content string. */
    private String _content;

    /**
     * Constructor to evaluate an expression into an integer content.
     * @param expression to be evaluated.
     * @throws InvalidExpressionException when the expression doesn't evaluate to a String.
     */
    public StringContent(String expression) throws InvalidExpressionException {
        /** Verifies if first character is ' */
        if (expression.length() == 0 | expression.charAt(0) != '\'') {
            throw new InvalidExpressionException(expression);
        }
        _content = expression.substring(1);
    }

    /** @see xxl.content.Content#getString() */
    @Override
    public String getString() {
        return _content;
    }

    /** @see xxl.content.Content#getInt() */
    @Override
    public int getInt() throws UnexpectedContentException {
        throw new UnexpectedContentException();
    }

    @Override
    public String toString() {
        return _content;
    }
}
