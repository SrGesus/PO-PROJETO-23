package xxl.cell;

import java.io.Serializable;
import java.util.List;

import xxl.cell.range.Range;
import xxl.exceptions.InvalidAddressException;
import xxl.exceptions.InvalidRangeException;
import xxl.visitor.SearchVisitor;

/**
 * Class that stores all Cells of a Spreadsheet.
 */
public abstract class CellStore implements Serializable {

    /** Number of lines in this store */
    private int _lines;

    /** Number of columns in this store */
    private int _columns;

    /**
     * Constructor.
     * @param lines
     * @param columns
     */
    public CellStore(int lines, int columns) {
        _lines = lines;
        _columns = columns;
    }

    /**
     * @return the number of lines
     */
    public int getLines() {
        return _lines;
    }

    /**
     * @return the number of columns
     */
    public int getColumns() {
        return _columns;
    }

    /**
     * Getter for the cell that will create new emtpy cell 
     * if it doesn't exist at the address.
     * @param address of the Cell
     * @return the Cell
     * @throws InvalidAddressException
     */
    public abstract Cell getCell(Address address) throws InvalidAddressException;

    /**
     * If the Cell is not in the store, an empty Cell unnattached to the store is returned.
     * @param address
     * @throws InvalidAddressException
     */
    public abstract Cell getCellReadOnly(Address address) throws InvalidAddressException;

    /**
     * Delete a Cell's content and try to remove it from the store if it is not being observed.
     * @param address
     * @throws InvalidAddressException
     */
    public void deleteCell(Address address) throws InvalidAddressException {
        getCellReadOnly(address).setContent(null);
        deleteEmptyCell(address);
    }

    /**
     * Removes Cell from the store if it is already empty,
     * and without observers.
     * @param address
     */
    protected abstract void deleteEmptyCell(Address address);

    /**
     * Searches the store for matching Cells
     * @param v search visitor
     * @return list of results
     */
    public abstract List<String> searchStore(SearchVisitor v);

    /**
     * Cleans up memory before saving
     */
    public abstract void cleanUp();

    /**
     * @param rangeSpecification
     * @return the Range of the given specification.
     * @throws InvalidRangeException
     */
    public Range getRange(String rangeSpecification) throws InvalidRangeException {
        return new Range(this, rangeSpecification);
    }
}
