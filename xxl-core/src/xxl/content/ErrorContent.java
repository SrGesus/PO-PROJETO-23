package xxl.content;

import xxl.exceptions.UnexpectedContentException;

/**
 * Represents a value that wasn't able to be calculated.
 */
public class ErrorContent extends Content {

    /** @see xxl.content.Content#getInt() */
    @Override
    public String getString() throws UnexpectedContentException {
        throw new UnexpectedContentException();
    }

    /** @see xxl.content.Content#getString() */
    @Override
    public int getInt() throws UnexpectedContentException {
        throw new UnexpectedContentException();
    }

    @Override
    public String toString() {
        return "#VALUE";
    }

}
