package xxl;

// FIXME import classes

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import xxl.cell.Cell;
import xxl.cell.CellStore;
import xxl.cell.CellStoreArray;
import xxl.cell.Range;
import xxl.exceptions.*;

/**
 * Class representing a spreadsheet.
 */
public class Spreadsheet implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    /** Flexible storage for Cells */
    private CellStore _cellStore;

    /**  */
    private Set<String> _users = null;

    /**
     * Constructor.
     * @param lines
     * @param columns
     */
    Spreadsheet(int lines, int columns) {
        _cellStore = new CellStoreArray(lines, columns);
    }
    
    /**
     * @param addressExpression ::= LINHA;COLUNA
     * @return the Cell
     * @throws InvalidAddressException when not able to parse address.
     */
    public Cell getCell(String addressExpression) throws InvalidAddressException {
        return _cellStore.getCell(addressExpression);
    }

    /**
     * @param rangeSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     * @return an iterator over the cells in the specified range.
     * @throws InvalidRangeException
     */
    public Range getGama(String rangeSpecification) throws InvalidRangeException {
        return _cellStore.getGama(rangeSpecification);
    }

    /**
     * @param rangeSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     * @return 
     * @throws InvalidRangeException
     */
    public Collection<String> showGama(String rangeSpecification) throws InvalidRangeException {
        return getGama(rangeSpecification).toStrings();
    }

    /**
     * Insert specified content in specified range.
     * @param rangeSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     * @param contentSpecification an expression
     */
    public void insertContents(String rangeSpecification, String contentSpecification) throws UnrecognizedEntryException, FunctionNameException {
        try {
            _cellStore.insertExpression(rangeSpecification, contentSpecification);
        } catch (InvalidExpressionException e) {
            throw new UnrecognizedEntryException(e.getExpression());
        }
    }

    /**
     * Add given user to the spreadsheet.
     */
    public void addUser(User user) {
        if (_users == null) _users = new HashSet<String>();
        _users.add(user.getName());
    }
}
