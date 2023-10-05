package xxl.function;

import xxl.cell.Cell;
import xxl.cell.CellStore;
import xxl.exceptions.FunctionArgException;

/**
 * Returns the product of two integers.
 */
public class MUL extends BinaryFunction {
    /**
     * Constructor.
     * @param store
     * @param output
     * @param args must be two Strings representing the arguments.
     * @throws FunctionArgException
     */
    public MUL(CellStore store, Cell output, String... args) throws FunctionArgException {
        super(store, output, args);
    }

    @Override
    protected int compute(int arg1, int arg2) {
        return arg1 * arg2;
    }
}
