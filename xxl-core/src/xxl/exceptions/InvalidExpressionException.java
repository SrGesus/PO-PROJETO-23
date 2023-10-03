package xxl.exceptions;

/**
 * Represents an attempt to evaluate an expression
 */
public class InvalidExpressionException extends Exception {

    /** The expression that caused exception. */
    private String _expression;

    /**
     * Constructor.
     * @param expression
     */
    public InvalidExpressionException(String expression) {
        _expression = expression;
    }

    /**
     * @return Expression that caused exception.
     */
    public String getExpression() {
        return _expression;
    }
}
