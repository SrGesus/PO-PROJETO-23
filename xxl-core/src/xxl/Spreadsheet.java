package xxl;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.lang.Iterable;
import java.util.Iterator;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xxl.cell.Address;
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
import xxl.visitor.SearchVisitor;

/**
 * Class representing a spreadsheet.
 */
public class Spreadsheet implements Serializable {

    @Serial
    private static final long serialVersionUID = 202308312359L;

    /** Flexible storage for Cells */
    private CellStore _cellStore;

    private CellStore _cutBuffer = new CellStoreArray(0, 0);

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
     * Searches the store for matching Cells
     * @param v search visitor
     * @return list of results, with functions alphabetically sorted
     */
    public List<String> searchStore(SearchVisitor v) {
        List<String> result = _cellStore.searchStore(v);
        
        Comparator<String> functionSorter = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                // Find the substrings after the '=' character
                String pattern = "=(.*?)(\\()";
                Pattern r = Pattern.compile(pattern);

                Matcher m1 = r.matcher(s1);
                Matcher m2 = r.matcher(s2);

                if (m1.find() && m2.find()) {
                    String substring1 = m1.group(1);
                    String substring2 = m2.group(1);

                    return substring1.compareTo(substring2);
                } else {
                    // If no match is found, behave as if no comparison is needed
                    return s1.compareTo(s2);
                }
            }
        };

        Collections.sort(result, functionSorter); // Sort the results based on function name alphabetical order
        return result;
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
        try {
            return _cellStore.getRange(gamaSpecification);
        } catch (InvalidRangeException e) {
            try {
                return _cellStore.getRange(gamaSpecification + ":" + gamaSpecification);
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
        return getGama(rangeSpecification).iterStrings();
    }

    /**
     * @return 
     * @throws InvalidRangeException
     */
    public Iterable<String> showCutBuffer() throws InvalidRangeException {
        return getCutBufferRange().iterStrings();
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

    private Range getCutBufferRange() {
        try {
            return _cutBuffer.getRange("1;1:" + _cutBuffer.getLines() + ";" + _cutBuffer.getColumns());
        } catch (InvalidRangeException e) {
            /* Logically Unreachable */
            e.printStackTrace();
            return null;
        }
    }

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
        Range gamaOrigem = getGama(gamaSpecification);
        _cutBuffer = new CellStoreArray(gamaOrigem.getLines(), gamaOrigem.getColumns());
        Iterator<Cell> originIterator = gamaOrigem.iterCellsReadOnly().iterator();
        Iterator<Cell> cutBufferIterator = getCutBufferRange().iterCells().iterator();
        while (cutBufferIterator.hasNext() && originIterator.hasNext()) {
            Cell bufferCell = cutBufferIterator.next();
            bufferCell.paste(this, originIterator.next()).close();
            bufferCell.value(); // Update value
        }
    }

    /**
     * Pastes the cut buffer content into the target cells.
     * @param gamaSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     */
    public void paste(String gamaSpecification) throws InvalidRangeException {
        Range gamaDestino = getGama(gamaSpecification);
        // Get all of the cut buffer
        Range gamaCutBuffer = _cutBuffer.getRange("1;1:" + _cutBuffer.getLines() + ";" + _cutBuffer.getColumns());
        // If provided range is single cell
        if (gamaDestino.getColumns() == 1 && gamaDestino.getLines() == 1) {
            Address cellAddr = gamaDestino.getStartAddress();
            // If buffer is horizontal
            if (gamaCutBuffer.getColumns() > 1)
                gamaDestino = getGama(cellAddr + ":" + (cellAddr.getLine() + 1) + ";" + _cellStore.getColumns());
            // If buffer is vertical
            else
                gamaDestino = getGama(cellAddr + ":" + _cellStore.getLines() + ";" + (cellAddr.getColumn() + 1));
        // otherwise if geometry does not match
        } else if (gamaDestino.getColumns() != gamaCutBuffer.getColumns() && gamaDestino.getLines() != gamaCutBuffer.getLines())
            throw new InvalidRangeException(gamaSpecification);
        Iterator<Cell> destinationIterator = gamaDestino.iterCells().iterator();
        Iterator<Cell> cutBufferIterator = gamaCutBuffer.iterCellsReadOnly().iterator();
        // While there are cells and space to paste
        while (destinationIterator.hasNext() && cutBufferIterator.hasNext())
            destinationIterator.next().paste(this, cutBufferIterator.next());
        dirty();
    }
}
