package xxl;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.lang.Iterable;
import java.util.List;

import xxl.cell.Address;
import xxl.cell.CellStore;
import xxl.cell.CellStoreArray;
import xxl.cell.CutBuffer;
import xxl.cell.range.Range;
import xxl.content.CellReference;
import xxl.content.Content;
import xxl.content.literal.*;
import xxl.exceptions.*;
import xxl.function.*;
import xxl.search.SearchResult;
import xxl.search.SearchMethod;
import xxl.search.Searcher;
import xxl.user.User;

/**
 * Class representing a spreadsheet.
 */
public class Spreadsheet implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    /** Flexible storage for Cells */
    private CellStore _cellStore;

    /** CutBuffer */
    private CutBuffer _cutBuffer = new CutBuffer();

    /** Name of the Spreadsheet */
    private String _filename = "";

    /** Set of user names */
    private Set<User> _users = null;

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

//  Files

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
     * @return the name of the Spreadsheet file
     */
    public String getFilename() {
        return _filename;
    }

    /**
     * Changes the Spreadsheet file name.
     * <p> Note: Does not handle users.
     * @param filename
     * @see DataStore#renameSpreadsheet(String,String)
     */
    public void setFilename(String filename) {
        _filename = filename;
    }

//  Search

    /**
     * Searches the store for matching Cells
     * @param v search visitor
     * @return list of results, with functions alphabetically sorted
     */
    public List<SearchResult> searchStore(SearchMethod v) {
        return new Searcher(v).search(_cellStore);
    }

//  Edit

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
                case "PRODUCT" -> new PRODUCT(this, functionArgs);
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
    public Range getGama(String gamaSpecification) throws InvalidRangeException {
        /** Parsing of rangeSpecification */
        String[] range = gamaSpecification.split(":");
        try {
            return switch (range.length) {
                case 1  -> _cellStore.getRange(new Address(range[0]), new Address(range[0]));
                case 2  -> _cellStore.getRange(new Address(range[0]), new Address(range[1]));
                default -> throw new InvalidRangeException(gamaSpecification);
            };
        } catch (InvalidAddressException e) {
            throw new InvalidRangeException(gamaSpecification);
        }
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
     * Deletes the contents of the given gama.
     * @param rangeSpecification
     * @throws InvalidRangeException
     */
    public void deleteGama(String rangeSpecification) throws InvalidRangeException {
        getGama(rangeSpecification).iterAddresses().forEach(addr -> {
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
        getGama(gamaSpecification).iterAddresses().forEach(addr -> {
            try {
                _cellStore.getCell(addr).setContent(content);
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

//  Users

    /**
     * Package-Private iterable for users.
     * @return users of the Spreadsheet
     */
    Iterable<User> users() {
        return _users;
    }

    /**
     * Add given user to the spreadsheet.
     * Overwrites if user already exists.
     * @param user to be added
     */
    public void addUser(User user) {
        if (_users == null) _users = new HashSet<User>();
        if (_users.add(user))
            dirty();
        else
            /** Update User */
            _users.remove(user); _users.add(user);
    }

    /**
     * Remove given user from the spreadsheet.
     * @param user to be removed
     */
    void removeUser(User user) {
        if (_users == null) return;
        _users.remove(user);
    }

//  Cut Buffer Apparatus

    /**
     * Cuts the content of the target cells into the cut buffer.
     * @param gamaSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     */
    public void cut(String gamaSpecification) throws InvalidRangeException {
        copy(gamaSpecification);
        deleteGama(gamaSpecification);
    }

    /**
     * Copies the content of the target cells into the cut buffer.
     * @param gamaSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     */
    public void copy(String gamaSpecification) throws InvalidRangeException {
        Range origin = getGama(gamaSpecification);
        _cutBuffer.copy(origin);
    }

    /**
     * Pastes the cut buffer content into the target cells.
     * @param gamaSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     */
    public void paste(String gamaSpecification) throws InvalidRangeException {
        Range gamaDestino = getGama(gamaSpecification);
        _cutBuffer.paste(gamaDestino, _cellStore);
        dirty();
    }

    /**
     * @return 
     * @throws InvalidRangeException
     */
    public Iterable<String> showCutBuffer() throws InvalidRangeException {
        return _cutBuffer.getCutBufferRange().iterStrings();
    }
}
