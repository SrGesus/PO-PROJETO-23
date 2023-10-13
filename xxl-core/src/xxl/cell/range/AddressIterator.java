package xxl.cell.range;

import xxl.cell.Address;
import xxl.cell.Cell;

/**
 * An Iterator that iterates over the addresses of the cells.
 */
class AddressIterator extends Range.RangeIterator<Address> {

    /**
     * Constructor.
     * @param range
     */
    AddressIterator(Range range) {
        range.super();
    }

    /** @see RangeIterator#getResult(Cell) */
    @Override
    protected Address getResult(Cell cell) {
        return getCurrentAddress();
    }

    /** @see RangeIterator#getNextCell() */
    @Override
    protected Cell getNextCell() {
        /** For efficiency don't lookup cell */
        return null;
    }
}
