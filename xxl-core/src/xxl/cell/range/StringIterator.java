package xxl.cell.range;

import xxl.cell.Cell;

/**
 * An Iterator that iterates over the string that represents each cell (with their address).
 */
public class StringIterator extends Range.RangeIterator<String> {

    /**
     * Constructor.
     * @param range
     */
    StringIterator(Range range) {
        range.super();
    }

    /** @see RangeIterator#getResult(Cell) */
    @Override
    public String getResult(Cell cell) {
        return getCurrentAddress() + "|" + cell;
    }
}
