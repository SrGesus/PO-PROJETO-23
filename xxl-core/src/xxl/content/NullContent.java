package xxl.content;

import xxl.exceptions.UnexpectedContentException;

/**
 * Represents the content of an empty cell or an empty content.
 */
public class NullContent extends Content {

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
        return "";
    }

}
