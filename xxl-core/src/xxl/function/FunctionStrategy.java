package xxl.function;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Predicate;

import xxl.cell.Cell;
import xxl.cell.CellStore;
import xxl.cell.CellObserver;
import xxl.content.Content;
import xxl.content.ErrorContent;
import xxl.exceptions.FunctionArgException;
import xxl.exceptions.FunctionNameException;
import xxl.exceptions.InvalidExpressionException;
import xxl.exceptions.InvalidRangeException;

/**
 * Abstract class representing a function
 * IMPORTANT A function must have the following PUBLIC constructor:
 * FUNCTION(CellStore, Cell, String... args)
 * and must use methods addCellArg and addRangeArg to 
 * evaluate their value.
 */
public abstract class FunctionStrategy implements Serializable {

    /** The Arguments this Function depends on */
    private Map<String, Cell> _argCells = new HashMap<>();
    private Map<String, Collection<Cell>> _argRanges = new HashMap<>(1);

    /** Predicate that checks whether a Content is a valid input */
    private Predicate<Cell> _isValidInput;

    /** 
     * The Observer of the Cell that stores the output of this function.
     * It needs to be stored for cleanup.
     * @see xxl.function.FunctionStrategy#cleanFunction()
     */
    private CellObserver _output;

    /**
     * Constructor.
     * @param isValidInput Predicate that returns whether given cell is a valid input.
     * @param output Cell calling this function.
     */
    FunctionStrategy(Predicate<Cell> isValidInput, Cell output) {
        _isValidInput = isValidInput;
        _output = new CellObserver(output);
    }

    /**
     * Adds an argument to the function.
     * @param store
     * @param key
     * @param expression
     * @throws FunctionArgException when expression can't be evaluated.
     */
    protected void addCellArg(CellStore store, String key, String expression) throws FunctionArgException {
        try {
            Cell cell = store.evaluateExpression(expression);
            _argCells.put(key, cell);
            cell.attach(_output);
        } catch (FunctionNameException | InvalidExpressionException e) {
            throw new FunctionArgException();
        }
    }

    /**
     * @param key
     * @return Cell argument of given key.
     */
    protected Cell getCellArg(String key) {
        return _argCells.get(key);                        
    }

    /**
     * Adds a range argument to the function.
     * @param store
     * @param key
     * @param expression
     * @throws FunctionArgException when expression is not a valid range.
     */
    protected void addRangeArg(CellStore store, String key, String expression) throws FunctionArgException {
        try {
            Collection<Cell> range = store.getRange(expression).collect();
            _argRanges.put(key, range);
            range.forEach(v -> v.attach(_output));
        } catch (InvalidRangeException e) {
            throw new FunctionArgException();
        }
    }

    /**
     * @param key
     * @return range argument of given key.
     */
    protected Collection<Cell> getRangeArg(String key) {
        return _argRanges.get(key);                     
    }

    /**
     * Template method that computes the arguments into a value.
     * @return the value as Content.
     */
    final public Content getOutput() {
        /** If any of the values does not pass the given isValidInput predicate, return Error content */
        for (Cell c : _argCells.values())
            if (!_isValidInput.test(c)) return new ErrorContent();
        for (Collection<Cell> r : _argRanges.values())
            for (Cell c : r) if (!_isValidInput.test(c)) return new ErrorContent();
        try {
            return result();
        } catch (FunctionArgException e) {
            return new ErrorContent();
        }
    }

    /**
     * @return the Content that is the result of the function.
     * @throws FunctionArgException if there's something wrong with the arguments.
     */
    protected abstract Content result() throws FunctionArgException;

    /**
     * This command should be executed whenever the function is not to be used anymore.
     * Stops the arguments from being observed.
     */
    public void cleanFunction() {
        /* Stop arguments from notifying their changes */
        _argCells.values().forEach(c -> _output.detach(c));
        _argRanges.values().forEach(r -> r.forEach(c -> _output.detach(c)));
    }
}
