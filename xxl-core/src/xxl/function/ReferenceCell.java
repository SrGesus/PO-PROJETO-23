package xxl.function;

import xxl.cell.Cell;
import xxl.cell.CellStore;
import xxl.content.Content;
import xxl.exceptions.FunctionArgException;

/**
 * A Function that represents a reference to a Cell.
 * Return the contents of the referenced Cell.
 */
public class ReferenceCell extends FunctionStrategy {

    /**
     * Constructor.
     * @param store
     * @param output Cell calling this function.
     * @param args must be a single String representing the address of the Cell.
     * @throws FunctionArgException
     */
    public ReferenceCell(CellStore store, Cell output, String... args) throws FunctionArgException {
        super(c -> true, output);
        if (args.length != 1) throw new FunctionArgException();
        addCellArg(store, "reference", args[0]);
    }

    @Override
    protected Content result() {
        return getCellArg("reference").getContent();
    }
}
