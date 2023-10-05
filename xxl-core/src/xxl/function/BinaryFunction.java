package xxl.function;

import xxl.cell.Cell;
import xxl.cell.CellStore;
import xxl.content.Content;
import xxl.content.IntContent;
import xxl.exceptions.FunctionArgException;
import xxl.exceptions.UnexpectedContentException;

/**
 * A Function that receives two integer arguments.
 * Returns an integer.
 */
public abstract class BinaryFunction extends FunctionStrategy{

    /**
     * Constructor.
     * @param store
     * @param output
     * @param args must be two Strings representing the two arguments.
     * @throws FunctionArgException
     */
    public BinaryFunction(CellStore store, Cell output, String... args) throws FunctionArgException {
        super(c -> c.getContent() instanceof IntContent, output);
        if (args.length != 2) throw new FunctionArgException();
        addCellArg(store, "arg1", args[0]);
        addCellArg(store, "arg2", args[1]);
    }

    /** @see xxl.function.FunctionStrategy#result() */
    @Override
    protected Content result() throws FunctionArgException {
        try {
            int arg1 = getCellArg("arg1").getContent().getInt();
            int arg2 = getCellArg("arg2").getContent().getInt();
            return new IntContent(compute(arg1, arg2));
        } catch (UnexpectedContentException e) {
            throw new FunctionArgException();
        }
    }

    protected abstract int compute(int arg1, int arg2) throws FunctionArgException;
}
