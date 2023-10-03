package xxl.content;

import xxl.exceptions.UnexpectedContentException;

/**
 * Class representing a value.
 */
public abstract class Content {

    /**
     * @return the string content if it is a string
     * @throws UnexpectedContentException if content is not a string.
     */
    public abstract String getString() throws UnexpectedContentException;

    /**
     * @return the integer content if it is an integer.
     * @throws UnexpectedContentException if content is not an integer.
     */
    public abstract int getInt() throws UnexpectedContentException;

    /** Force the re-implementation of toString */
    @Override
    public abstract String toString();
}
