package xxl.function;

import xxl.cell.Cell;
import xxl.cell.CellStore;
import xxl.content.Content;
import xxl.content.ErrorContent;
import xxl.content.NullContent;
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
        super(output);
        if (args.length != 1) throw new FunctionArgException();
        addCellArg(store, "reference", args[0]);
    }

    /** @see xxl.function.FunctionStrategy#isValidInput */
    @Override
    protected boolean isValidInput(Cell c) {
        return !(c.getContent() instanceof NullContent) && !(c.getContent() instanceof ErrorContent);
    }

    /** @see xxl.function.FunctionStrategy#result() */
    @Override
    protected Content result() {
        return getCellArg("reference").getContent();
    }
}
