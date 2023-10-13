package xxl;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import xxl.cell.Cell;
import xxl.cell.CellStore;
import xxl.cell.CellStoreArray;
import xxl.cell.range.Range;
import xxl.exceptions.*;
import xxl.user.DataStore;
import xxl.user.User;

/**
 * Class representing a spreadsheet.
 */
public class Spreadsheet implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    /** Flexible storage for Cells */
    private CellStore _cellStore;

    /** Set of user names */
    private Set<String> _users = null;

    /** */
    private String _filename = null;

    /** Whether there are unsaved changes */
    private boolean _dirty = true;

    /**
     * Constructor.
     * @param lines
     * @param columns
     */
    Spreadsheet(int lines, int columns) {
        _cellStore = new CellStoreArray(lines, columns);
    }
    
    /**
     * THere are unsaved changes.
     */
    void dirty() {
        _dirty = true;
    }

    /**
     * There are no unsaved changes.
     */
    void clean() {
        _dirty = false;
        _cellStore.cleanUp();
    }

    /**
     * @return whether there are unsaved changes.
     */
    public boolean isDirty() {
        return _dirty;
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
    public Iterable<String> showGama(String rangeSpecification) throws InvalidRangeException {
        return getGama(rangeSpecification).iterStrings();
    }

    /**
     * Insert specified content in specified range.
     * @param rangeSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     * @param contentSpecification an expression
     */
    public void insertContents(String rangeSpecification, String contentSpecification) throws UnrecognizedEntryException, FunctionNameException {
        try {
            if (contentSpecification.isBlank())
                _cellStore.deleteGama(rangeSpecification);
            else
                _cellStore.insertExpression(rangeSpecification, contentSpecification);
            dirty();
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
        user.addSpreadsheet(_filename);
        dirty();
    }

    /**
     * Changes the filename of this spreadsheet.
     * @param filename
     */
    public void rename(DataStore store, String filename) {
        store.renameSpreadsheet(_filename, filename);
        _filename = filename;
    }
}
