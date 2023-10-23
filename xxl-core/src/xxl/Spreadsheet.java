package xxl;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import xxl.cell.Cell;
import xxl.cell.CellStore;
import xxl.cell.CellStoreArray;
import xxl.cell.range.Range;
import xxl.content.CellReference;
import xxl.content.Content;
import xxl.content.literal.ErrorLiteral;
import xxl.content.literal.IntLiteral;
import xxl.content.literal.StringLiteral;
import xxl.exceptions.*;
import xxl.function.*;
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

    private CellStore _cutBuffer=null;

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
     * Parses an expression String into a Content.
     * @param expression
     * @param checkEqual whether to check for an equals sign at the beginning of the expression
     * @return
     * @throws FunctionNameException
     */
    public Content parseContent(String expression, boolean checkEqual) throws FunctionNameException {
        if (expression.isBlank()) return null;
        /* Try String */
        try {
            return new StringLiteral(expression);
        } catch (InvalidExpressionException e) {}
        /* Try Int */
        try {
            return new IntLiteral(expression);
        } catch (InvalidExpressionException e) {}
        /* Consume equals */
        if (checkEqual) {
            if (expression.charAt(0) != '=') return new ErrorLiteral();
            expression = expression.substring(1);
        }
        /* Try reference to Cell */
        try {
            return new CellReference(_cellStore, expression);
        } catch (InvalidExpressionException e) {}
        /* Try function */
        try {
            /** The name of the function is everything until '(' */
            String functionName = expression.substring(0, expression.indexOf('('));
            /** Array of what is inbetween commas and inside the outermost parentheses */
            String[] functionArgs = expression.substring(expression.indexOf('(')+1, expression.lastIndexOf(')')).split(",+(?![^\\(]*\\))");
            return switch(functionName) {
                case "ADD" -> new ADD(this, functionArgs);
                case "DIV" -> new DIV(this, functionArgs);
                case "MUL" -> new MUL(this, functionArgs);
                case "SUB" -> new SUB(this, functionArgs);
                case "AVERAGE" -> new AVERAGE(this, functionArgs);
                case "CONCAT" -> new CONCAT(this, functionArgs);
                case "COALESCE" -> new COALESCE(this, functionArgs);
                default -> throw new FunctionNameException(functionName);
            };
        } catch (IndexOutOfBoundsException| FunctionArgException e) {}
        return new ErrorLiteral();
    }

    
    /**
     * getRange but works for single cells.
     * @param gamaSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     * @return the Range of the given specification.
     * @throws InvalidRangeException
     */
    public Range getGama(String gamaSpecification, CellStore store) throws InvalidRangeException {
        try {
            return store.getRange(gamaSpecification);
        } catch (InvalidRangeException e) {
            try {
                return store.getRange(gamaSpecification + ":" + gamaSpecification);
            } catch (InvalidRangeException e2) {
                throw e;
            }
        }
    }

    /**
     * @param rangeSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     * @return 
     * @throws InvalidRangeException
     */
    public Iterable<String> showGama(String rangeSpecification) throws InvalidRangeException {
        return getGama(rangeSpecification, _cellStore).iterStrings();
    }

    public Iterable<String> showCutBuffer(String rangeSpecification) throws InvalidRangeException {
        return getGama(rangeSpecification, _cutBuffer).iterStrings();
    }

    /**
     * Deletes the contents of the given gama.
     * @param rangeSpecification
     * @throws InvalidRangeException
     */
    public void deleteGama(String rangeSpecification) throws InvalidRangeException {
        getGama(rangeSpecification, _cellStore).iterAddresses().forEach(addr -> {
            try {_cellStore.deleteCell(addr);
            } catch (InvalidAddressException e) { /* Logically Unreachable */}
        } );
        dirty();
    }

    /**
     * Inserts content into gama.
     * @param gamaSpecification
     */
    public void insertGama(String gamaSpecification, String contentSpecification) throws InvalidExpressionException, FunctionNameException {
        Content content = parseContent(contentSpecification, true);
        getGama(gamaSpecification, _cellStore).iterAddresses().forEach(addr -> {
            try {_cellStore.getCell(addr).setContent(content);
            } catch (InvalidAddressException e) { /* Logically Unreachable */}
        } );
        dirty();
    }

    /**
     * Insert specified content in specified range.
     * @param gamaSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     * @param contentSpecification an expression
     */
    public void insertContents(String gamaSpecification, String contentSpecification) throws UnrecognizedEntryException {
        try {
            if (contentSpecification.isBlank())
                deleteGama(gamaSpecification);
            else 
                insertGama(gamaSpecification, contentSpecification);
            dirty();
        } catch (InvalidExpressionException e) {
            throw new UnrecognizedEntryException(e.getExpression());
        } catch (FunctionNameException e) {
            /* Guaranteed to be Unreachable */
            throw new UnrecognizedEntryException(contentSpecification);
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

    public void cut(String gamaSpecification) throws InvalidRangeException {
        copy(gamaSpecification);
        deleteGama(gamaSpecification);
    }

    public void copy(String gamaSpecification) throws InvalidRangeException {
        Range gama = getGama(gamaSpecification, _cellStore);
        _cutBuffer = new CellStoreArray(gama.getLines(), gama.getColumns());
        gama.iterAddresses().forEach(addr -> {
            try {
                _cutBuffer.getCell(addr).paste(this, _cellStore.getCell(addr).copy(this));
            } catch (InvalidAddressException e) { /* Logically Unreachable */}
        });
    }

    public void paste(String gamaSpecification) throws InvalidRangeException {
        Range gama = getGama(gamaSpecification, _cutBuffer);
        if (_cutBuffer == null) return;
        gama.iterAddresses().forEach(addr -> {
            try {
                _cellStore.getCell(addr).paste(this, _cutBuffer.getCell(addr).copy(this));
            } catch (InvalidAddressException e) { /* Logically Unreachable */}
        });
        dirty();
    }
}
