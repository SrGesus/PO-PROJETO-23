package xxl.content;

import java.io.Serializable;

import xxl.content.literal.Literal;
import xxl.exceptions.UnexpectedContentException;
import xxl.observer.Observable;
import xxl.visitor.Visitable;

/**
 * Class representing the contents of a cell.
 */
public abstract class Content implements Serializable, Observable, Visitable {

    /**
     * @return The String value of this content.
     * @throws UnexpectedContentException if the content has no String.
    */
    public String getString() throws UnexpectedContentException {
        return value().getString();
    }

    /** 
     * @return The Int value of this content.
     * @throws UnexpectedContentException if the content has no Int.
     */
    public int getInt() throws UnexpectedContentException {
        return value().getInt();
    }

    /**
     * @return the Literal value of this content.
     */
    public abstract Literal value();

    /** Force the re-implementation of toString */
    @Override
    public abstract String toString();

    /** 
     * @see Observer#close() 
     */
    public abstract void close();
}
