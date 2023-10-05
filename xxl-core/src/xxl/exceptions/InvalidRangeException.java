package xxl.exceptions;

public class InvalidRangeException extends InvalidExpressionException {
    public InvalidRangeException(String expression) {
        super(expression);
    }
}
