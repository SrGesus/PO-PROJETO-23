package xxl.cell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import xxl.content.Content;
import xxl.content.literal.ErrorLiteral;
import xxl.content.literal.Literal;
import xxl.observer.Observable;
import xxl.observer.Observer;
import xxl.visitor.CopyContent;
import xxl.visitor.Visitable;
import xxl.visitor.Visitor;

/**
 * Class that represents a Cell of a Spreadsheet.
 * All functions that alter the state of a Cell are package-private.
 */
public class Cell implements Serializable, Observable, Observer, Visitable {

    /** The content of this Cell */
    private Content _content = null;

    /* Observers of this Content */
    private List<Observer> _observers = new ArrayList<Observer>();
    
    /**
     * Constructor for empty Cell.
     */
    public Cell() {
    }

    /**
     * Setter for content.
     * @param content
     */
    public void setContent(Content content) {
        close();
        _content = content;
        if (content != null)
            content.attach(this);
        notifyObservers();
    }

    public Content getContent() {
        return _content;
    }

    /**
     * @return the value of the content of this Cell.
     */
    public Literal value() {
        if (_content == null) return new ErrorLiteral();
        return _content.value();
    }

    /**
     * Creates a copy of this Cell.
     * @return the Cell's copy
     */
    public Cell copy() {
        return new Cell().paste(this);
    }

    /**
     * @param spreadsheet
     * @param origin Cell to copy from
     * @return this Cell (destination)
     */
    public Cell paste(Cell origin) {
        setContent(origin.accept(new CopyContent()));
        return this;
    }

    /**
     * @param observer to attach
     * @see Observable#attach(Observer)
     */
    @Override
    public final void attach(Observer observer) {
        _observers.add(observer);
    }

    /**
     * @param observer to detach
     * @see Observable#detach(Observer)
     */
    @Override
    public void detach(Observer observer) {
        _observers.remove(observer);
    }

    /**
     * @see Observable#notifyObservers()
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : _observers) {
            observer.update();
        }
    }

    /**
     * @see Observer#update()
     */
    @Override
    public void update() {
        notifyObservers();
    }

    /**
     * @return true if this Cell is empty and has no Observers.
     */
    public boolean isDeletable() {
        return isEmpty() && _observers.isEmpty();
    }

    /**
     * @return true if this Cell has a null content
     */
    public boolean isEmpty() {
        return _content == null;
    }

    /**
     * @return true if this Cell has a Literal content
     */
    public boolean isLiteral() {
        return _content == value();
    }

    /** @see Observer#close() */
    public void close() {
        if (_content != null) _content.close();
    }

    /** @see Visitable#accept(Visitor) */
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitCell(this);
    }
}
