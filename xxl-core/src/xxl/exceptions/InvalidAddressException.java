package xxl.exceptions;

public class InvalidAddressException extends InvalidExpressionException {
    public InvalidAddressException(String address) {
        super(address);
    }
}
