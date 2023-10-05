package xxl.content;

import xxl.exceptions.InvalidExpressionException;
import xxl.exceptions.UnexpectedContentException;

/**
 * Represents a value that is an integer.
 */
public class IntContent extends Content {

    /** The content integer. */
    private int _content;

    /**
     * Constructor to create an integer content from int.
     * @param integer
     */
    public IntContent(int integer) {
        _content = integer;
    }

    /**
     * Constructor to evaluate an expression into an integer content.
     * @param expression to be evaluated.
     * @throws InvalidExpressionException when the expression doesn't evaluate to a String.
     */
    public IntContent(String expression) throws InvalidExpressionException {
        try {
            _content = Integer.parseInt(expression);
        } catch (NumberFormatException e) {
            throw new InvalidExpressionException(expression);
        }
    }

    /** @see xxl.content.Content#getInt() */
    @Override
    public String getString() throws UnexpectedContentException {
        throw new UnexpectedContentException();
    }

    /** @see xxl.content.Content#getString() */
    @Override
    public int getInt() {
        return _content;
    }

    @Override
    public String toString() {
        return Integer.toString(_content);
    }
}
