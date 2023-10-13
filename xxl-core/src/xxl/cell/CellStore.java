package xxl.cell;

import java.io.Serializable;

import xxl.cell.range.Range;
import xxl.exceptions.FunctionNameException;
import xxl.exceptions.InvalidAddressException;
import xxl.exceptions.InvalidExpressionException;
import xxl.exceptions.InvalidRangeException;

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
     * Removes Cell from the store if it is not being observed.
     * @param address
     * @throws InvalidAddressException
     */
    public void deleteCell(Address address) throws InvalidAddressException {
        try {
            getCellReadOnly(address).updateExpression(this, "");
        } catch (FunctionNameException | InvalidExpressionException e) {
            /* Unreachable */
        }
        deleteEmptyCell(address);
    }

    /**
     * Removes Cell from the store if it is already empty,
     * and without observers.
     * @param address
     */
    protected abstract void deleteEmptyCell(Address address);

    /**
     * Cleans up memory before saving
     */
    public abstract void cleanUp();

    /**
     * @param addressSpecification ::= LINHA;COLUNA
     * @return the Cell at the given address specification.
     * @throws InvalidAddressException
     */
    public Cell getCell(String addressSpecification) throws InvalidAddressException {
        return getCell(new Address(addressSpecification));
    }

    /**
     * @param rangeSpecification
     * @return the Range of the given specification.
     * @throws InvalidRangeException
     */
    public Range getRange(String rangeSpecification) throws InvalidRangeException {
        return new Range(this, rangeSpecification);
    }

    /**
     * getRange but works for single cells.
     * @param gamaSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     * @return the Range of the given specification.
     * @throws InvalidRangeException
     */
    public Range getGama(String gamaSpecification) throws InvalidRangeException {
        try {
            return new Range(this, gamaSpecification);
        } catch (InvalidRangeException e) {
            try {
                return new Range(this, gamaSpecification + ":" + gamaSpecification);
            } catch (InvalidRangeException e2) {
                throw e;
            }
        }
    }

    /**
     * Inserts and evaluates a given expression into a Cell of the store.
     * @param gamaSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     * @param expression
     * @throws FunctionNameException
     * @throws InvalidExpressionException
     */
    public void insertExpression(String gamaSpecification, String expression) throws FunctionNameException, InvalidExpressionException {
        Range range = getGama(gamaSpecification);
        for (Cell cell : range.iterCells()) {
            cell.updateExpression(this, expression, true);
        }
    }

    /**
     * Deletes a given range of Cells from the store.
     * @param gamaSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     * @throws InvalidRangeException
     */
    public void deleteGama(String gamaSpecification) throws InvalidRangeException {
        Range range = getGama(gamaSpecification);
        range.iterAddresses().forEach(address -> deleteEmptyCell(address));
    }

    /**
     * Evaluates the expression into a Cell.
     * @param expression
     * @return the evaluated Cell. Returns referenced Cell if it's an address.
     * @throws FunctionNameException if there is no function with the given name.
     * @throws InvalidExpressionException otherwise failed to parse expression.
     */
    public Cell evaluateExpression(String expression) throws FunctionNameException, InvalidExpressionException {
        Cell cell = null;
        try {
            cell = getCell(expression);
        } catch (InvalidAddressException e) {
            cell = new Cell();
            cell.updateExpression(this, expression);
        }
        return cell;
    }
}
