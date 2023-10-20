package xxl.content.literal;

import xxl.content.Content;
import xxl.exceptions.UnexpectedContentException;
import xxl.observer.Observable;
import xxl.observer.Observer;

/**
 * A Content of immutable value (observation is futile as there is no change)
 */
public abstract class Literal extends Content {

    /** @see Content#value() */
    @Override
    public final Literal value() {
        return this;
    }

    /**
     * @return The String value of this content.
     * @throws UnexpectedContentException if the content has no String.
     * @see Content#getString()
    */
    @Override
    public String getString() throws UnexpectedContentException {
        throw new UnexpectedContentException();
    }

    /** 
     * @return The Int value of this content.
     * @throws UnexpectedContentException if the content has no Int.
     * @see Content#getInt()
     */
    @Override
    public int getInt() throws UnexpectedContentException {
        throw new UnexpectedContentException();
    }

    /** @see Observable#attach(Observer) */
    @Override
    public final void attach(Observer observer) {
        /** Literal is immutable */
    }

    /** @see Observable#attach(Observer) */
    @Override
    public final void detach(Observer observer) {
        /** Literal is immutable */
    }

    /** @see Observable#notifyObservers() */
    @Override
    public final void notifyObservers() {
        /** Literal is immutable */
    }

    /** @see Content#close() */
    public void close() {
        /** Literal does not Observer */
    }
}
