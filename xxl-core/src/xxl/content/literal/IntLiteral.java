package xxl.content.literal;

import xxl.exceptions.InvalidExpressionException;
import xxl.visitor.Visitable;
import xxl.visitor.Visitor;

/**
 * Represents a value that is an integer.
 */
public class IntLiteral extends Literal {

    /** The content integer. */
    private int _value;

    /**
     * Constructor to create an integer content from int.
     * @param integer
     */
    public IntLiteral(int integer) {
        _value = integer;
    }

    /**
     * Constructor to evaluate an expression into an integer content.
     * @param expression to be evaluated.
     * @throws InvalidExpressionException when the expression doesn't evaluate to a String.
     */
    public IntLiteral(String expression) throws InvalidExpressionException {
        try {
            _value = Integer.parseInt(expression);
        } catch (NumberFormatException e) {
            throw new InvalidExpressionException(expression);
        }
    }

    /** @see xxl.content.Content#getString() */
    @Override
    public int getInt() {
        return _value;
    }

    /** @see Visitable#accept(Visitor) */
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitIntLiteral(this);
    }
}
