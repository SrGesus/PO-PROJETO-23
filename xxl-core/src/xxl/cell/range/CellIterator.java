package xxl.cell.range;

import xxl.cell.Cell;
import xxl.exceptions.InvalidAddressException;

/**
 * An Iterator that iterates over Cells in the store.
 * Use when the Cell is to be changed or added observers.
 */
class CellIterator extends CellReadOnlyIterator {

    /**
     * Constructor.
     * @param range
     */
    CellIterator(Range range) {
        super(range);
    }
    /** @see RangeIterator#getNextCell() */
    @Override
    public Cell getNextCell() throws InvalidAddressException {
        return getStore().getCell(getCurrentAddress());
    }
}