package xxl.cell;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import xxl.function.*;
import xxl.content.*;
import xxl.exceptions.FunctionArgException;
import xxl.exceptions.FunctionNameException;
import xxl.exceptions.InvalidAddressException;
import xxl.exceptions.InvalidExpressionException;

/**
 * Class that represents a Cell of a Spreadsheet.
 * All functions that alter the state of a Cell are package-private.
 */
public class Cell implements Serializable {

    /** 
     * The expression inside this Cell 
     * @see xxl.cell.Cell#setExpression(String)
     */
    private String _expression = "";

    /** The content of this Cell */
    private Content _content = new NullContent();

    /** Function that the expression of the Cell represents */
    private FunctionStrategy _function = null;

    /** True if content might have to be recalculated. */
    private boolean dirty = false;

    /** A Collection of the observers of this Cell that need to be notified. */
    private Set<CellObserver> _observers = null;

    /**
     * Package-private constructor.
     */
    Cell() {}

    /** Mark Cell's content as outdated. */
    void dirty() {
        dirty = true;
    }

    /**
     * Retrieves the content, and recalculates it if dirty.
     * @return The content of this cell
     */
    public Content getContent() {
        if (dirty) updateContent();
        return _content;
    }

    /**
     * @return The expression of this cell
     */
    public String getExpression() {
        return _expression;
    }

    /**
     * @return The name of the function in this cell.
     */
    public String getFunctionName() {
        if (_function == null) return "";
        return _function.getClass().getSimpleName();
    }

    /**
     * Recalculates the content of this cell.
    */
    void updateContent() {
        if (_function != null) {
            _content = _function.getOutput();
        }
        notifyObservers();
        dirty = false;
    }

    /**
     * Changes the expression of this Cell.
     * Run this method instead of setting the expression directly.
     * Will remove current function.
     * @param expression to be changed
     */
    private void setExpression(String expression) {
        if (_function != null) {
            _function.cleanFunction();
            _function = null;
        }
        notifyObservers();
        _expression = expression;
    }

    /**
     * Sets and parses the expression of this Cell.
     * @param store the CellStore this Cell is in.
     * @param expression to be evaluated.
     */
    void updateExpression(CellStore store, String expression) throws FunctionNameException, InvalidExpressionException {
        updateExpression(store, expression, false);
    }

    /**
     * Sets and parses the expression of this Cell.
     * @param store the CellStore this Cell is in.
     * @param expression to be evaluated.
     * @param checkEqual if true, will check if there is an equal sign if the expression is not a literal.
     */
    void updateExpression(CellStore store, String expression, boolean checkEqual) throws FunctionNameException, InvalidExpressionException {
        /* Try empty expression */
        if (expression.isBlank()) {
            _content = new NullContent();
            setExpression(expression);
            return;
        }
        /* Try String literal */
        try {
            _content = new StringContent(expression);
            setExpression(expression);
            return;
        } catch (InvalidExpressionException e) {
            /* Not a String */
        }
        /* Try Integer literal */
        try {
            _content = new IntContent(expression);
            setExpression(expression);
            return;
        } catch (InvalidExpressionException e) {
            /* Not an integer. */
        }
        /*  */
        if (checkEqual) {
            if (expression.charAt(0) != '=') throw new InvalidExpressionException(expression);
            expression = expression.substring(1);
        }
        /* Try to evaluate a Reference */
        try {
            store.getCell(expression);
            setExpression(expression);
            _function = new ReferenceCell(store, this, expression);
            dirty();
            return;
        } catch (InvalidAddressException e) {
            /* Not a reference. */
        } catch (FunctionArgException e) {
            /* Unreachable */
        }
        /* Try to evaluate a Function */
        try {
            /** The name of the function is everything until '(' */
            String functionName = expression.substring(0, expression.indexOf('('));
            /** Array of what is inbetween commas and inside the outermost parentheses */
            String[] functionArgs = expression.substring(expression.indexOf('(')+1, expression.lastIndexOf(')')).split(",+(?![^\\(]*\\))");
            FunctionStrategy newFunction = switch (functionName) {
                case "ADD" -> new ADD(store, this, functionArgs);
                case "SUB" -> new SUB(store, this, functionArgs);
                case "MUL" -> new MUL(store, this, functionArgs);
                case "DIV" -> new DIV(store, this, functionArgs);
                default -> throw new FunctionNameException(functionName);
            };
            setExpression(expression);
            _function = newFunction;
            dirty();
            return;
        } catch (IndexOutOfBoundsException | FunctionArgException e) {}
        /* Not properly formulated expression */
        throw new InvalidExpressionException(expression);
    }
    
    /**
     * Starts notifying given observer.
     * @param cellObserver to be attached.
     */
    public void attach(CellObserver cellObserver) {
        if (_observers == null) _observers = new HashSet<CellObserver>();
        _observers.add(cellObserver);
    }

    /**
     * Stops notifying given observer.
     * @param cellObserver to be removed.
     */
    void detach(CellObserver cellObserver) {
        _observers.remove(cellObserver);
    }

    /**
     * Notify observers that value has been changed.
     */
    private void notifyObservers() {
        if (_observers == null) return;
        for (CellObserver cellObserver : _observers) {
            cellObserver.update();
        }
    }

    /**
     * Copy the expression of given Cell to this Cell.
     * @param store the CellStore this Cell is in.
     * @param cell the Cell to be pasted into this one.
     */
    public void paste(CellStore store, Cell cell) {
        try {
            updateExpression(store, cell._expression, true);
        } catch (InvalidExpressionException | FunctionNameException e) {
            /** Unreachable */
        }
    }

    /**
     * Copy the expression of this Cell to another Cell.
     * @param store the CellStore this Cell is in
     * @return a copy of this Cell
     */
    public Cell copy(CellStore store) {
        Cell cell = new Cell();
        cell.setExpression(cell._expression);
        return cell;
    }

    /** @see Object#toString() */
    @Override
    public String toString() {
        if (getContent() instanceof NullContent) return "";
        return getContent() + (_function != null ? "=" + _expression : "");
    }
}
