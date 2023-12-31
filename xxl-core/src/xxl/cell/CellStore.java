package xxl.cell;

import java.io.Serializable;
import java.util.function.Consumer;

import xxl.cell.range.Range;
import xxl.exceptions.InvalidAddressException;
import xxl.exceptions.InvalidRangeException;
import xxl.search.SearchResult;

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
     * Places cell on given address.
     * @param address
     * @param cell
     * @throws InvalidAddressException
     */
    public void putCell(Address address, Cell cell) throws InvalidAddressException {
        getCell(address).paste(cell);
    }

    /**
     * Efficiently iterate over taking advantage of the store type.
     * <p> Note: Only guaranteed to be in-order for CellStoreArray.
     * @param consumer consumer of Address and Cell
     */
    public abstract void forEach(Consumer<SearchResult> consumer);

    /**
     * Cleans up memory before saving
     */
    public abstract void cleanUp();

    /**
     * @param rangeSpecification
     * @return the Range of the given specification.
     * @throws InvalidRangeException
     */
    public Range getRange(Address startAddress, Address endAddress) throws InvalidRangeException {
        return new Range(this, startAddress, endAddress);
    }
}
