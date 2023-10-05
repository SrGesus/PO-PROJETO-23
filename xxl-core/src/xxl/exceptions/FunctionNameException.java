package xxl.exceptions;

/**
 * Thrown when a function with this name cannot be found.
 */
public class FunctionNameException extends Exception {
    /** Name of function that was not found. */
    String _functionName;

    /**
     * @param functionName
     */
    public FunctionNameException(String functionName) {
        super("Function Name not found:" + functionName);
        _functionName = functionName;
    }

    /**
     * @return name of function that was not found.
     */
    public String getFunctionName() {
        return _functionName;
    }
}
