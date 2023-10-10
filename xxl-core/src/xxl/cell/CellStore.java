package xxl.cell;

import java.io.Serializable;

import xxl.exceptions.FunctionNameException;
import xxl.exceptions.InvalidAddressException;
import xxl.exceptions.InvalidExpressionException;
import xxl.exceptions.InvalidRangeException;

/**
 * Class that stores all Cells of a Spreadsheet.
 */
public abstract class CellStore implements Serializable {

    /** Whether there are unsaved changes */
    boolean _dirty = true;

    /** Number of lines in this store */
    int _lines;

    /** Number of columns in this store */
    int _columns;

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
     * @param address of the Cell
     * @return the Cell
     */
    public abstract Cell getCell(Address address) throws InvalidAddressException;

    /**
     * 
     * @param addressSpecification ::= LINHA;COLUNA
     * @return
     * @throws InvalidAddressException
     */
    public Cell getCell(String addressSpecification) throws InvalidAddressException {
        return getCell(new Address(addressSpecification));
    }

    /**
     * 
     * @param rangeSpecification
     * @return
     * @throws InvalidRangeException
     */
    public Range getRange(String rangeSpecification) throws InvalidRangeException {
        return new Range(this, rangeSpecification);
    }

    /**
     * @param gamaSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
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
     * @param rangeSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     * @param expression
     * @throws FunctionNameException
     * @throws InvalidExpressionException
     */
    public void insertExpression(String rangeSpecification, String expression) throws FunctionNameException, InvalidExpressionException {
        Range range = getGama(rangeSpecification);
        while (range.hasNext()) {
            range.next().updateExpression(this, expression, true);
        }
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
