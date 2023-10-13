package xxl.cell.range;

import xxl.cell.Cell;

/**
 * An Iterator that iterates over Cells.
 * Use when they're not going to be changed.
 */
class CellReadOnlyIterator extends Range.RangeIterator<Cell> {

    /**
     * Constructor.
     * @param range
     */
    CellReadOnlyIterator(Range range) {
        range.super();
    }
    
    /** @see RangeIterator#getResult(Cell) */
    @Override
    public Cell getResult(Cell c) {
        return c;
    }
}
