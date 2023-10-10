package xxl.function;

import xxl.cell.Cell;
import xxl.cell.CellStore;
import xxl.exceptions.FunctionArgException;

/**
 * Returns the product of two integers.
 */
public class SUB extends BinaryFunction {
    /**
     * Constructor.
     * @param store
     * @param output
     * @param args must be two Strings representing the arguments.
     * @throws FunctionArgException
     */
    public SUB(CellStore store, Cell output, String... args) throws FunctionArgException {
        super(store, output, args);
    }

    /** @see xxl.function.BinaryFunction#compute(int, int) */
    @Override
    protected int compute(int arg1, int arg2) {
        return arg1 - arg2;
    }
}
