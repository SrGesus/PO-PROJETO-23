package xxl.function;

import xxl.cell.Cell;
import xxl.cell.CellStore;
import xxl.exceptions.FunctionArgException;

/**
 * Returns the division of two integers.
 */
public class DIV extends BinaryFunction {
    /**
     * Constructor.
     * @param store
     * @param output
     * @param args must be two Strings representing the arguments.
     * @throws FunctionArgException
     */
    public DIV(CellStore store, Cell output, String... args) throws FunctionArgException {
        super(store, output, args);
    }

    @Override
    protected int compute(int arg1, int arg2) throws FunctionArgException {
        try {
            return arg1 / arg2;
        } catch (ArithmeticException e) {
            throw new FunctionArgException();
        }
    }
}
