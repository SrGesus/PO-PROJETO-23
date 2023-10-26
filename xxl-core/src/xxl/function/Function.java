package xxl.function;

import java.util.ArrayList;
import java.util.List;

import xxl.cell.Cell;
import xxl.cell.range.Range;
import xxl.content.CellReference;
import xxl.content.Content;
import xxl.content.ObservableContent;
import xxl.content.literal.Literal;
import xxl.exceptions.FunctionArgException;
import xxl.exceptions.InvalidAddressException;
import xxl.exceptions.UnexpectedContentException;
import xxl.observer.Observer;

public abstract class Function extends ObservableContent implements Observer {

    /* Whether the Literal value of this Function is up to date */
    private boolean dirty = true;

    /* The output saved, so it doesn't have to be recalculated */
    private Literal _value;

    private List<Content> _arguments = new ArrayList<>();

    /**
     * Adds a single Content as an argument of this Function.
     * @param argument
     */
    protected void addArg(Content argument) throws FunctionArgException {
        if (argument == null) throw new FunctionArgException();
        _arguments.add(argument);
        argument.attach(this);
    }

    /**
     * Adds the CellReferences of a Range as arguments of this Function.
     * @param store
     * @param range
     */
    protected void addRange(Range range) throws FunctionArgException {
        try {
            Range.RangeIterator<Cell> iterator = (Range.RangeIterator<Cell>) range.iterCells().iterator();
            while (iterator.hasNext()) {
                addArg(new CellReference(iterator.getCurrentAddress(), iterator.next()));
            }
        } catch (InvalidAddressException | ClassCastException e) {
            /* Logically Unreachable */
            e.printStackTrace();
        }
    }

    /**
     * @param index
     * @return the nth argument of this Function.
     */
    public Content getArg(int index) {
        return _arguments.get(index);
    }

    /**
     * @return the number of arguments of this Function.
     */
    public int size() {
        return _arguments.size();
    }

    
    /**
     * @return The String value of this function.
     * @throws UnexpectedContentException if the content has no String.
     * @see Content#getString()
    */
    public final String getString() throws UnexpectedContentException {
        return value().getString();
    }

    /** 
     * @return The Int value of this function.
     * @throws UnexpectedContentException if the content has no Int.
     * @see Content#getInt()
     */
    public final int getInt() throws UnexpectedContentException {
        return value().getInt();
    }

    /** @see Content#value() */
    @Override
    public final Literal value() {
        if (dirty) {
            _value = compute();
            dirty = false;
        }
        return _value;
    }

    /** @see Observer#update() */
    @Override
    public void update() {
        dirty = true;
        notifyObservers();
    }

    /**
     * Template method.
     * @return the Literal value of this Function as computed by a subclass.
     */
    protected abstract Literal compute();

    /**
     * @return the name of this Function.
     */
    public abstract String getName();

    /** @see Observer#close() */
    @Override
    public void close() {
        for (Content argument : _arguments) {
            argument.detach(this);
        }
    }
}
