package xxl.content.literal;

/**
 * Represents a value that wasn't able to be calculated.
 */
public class ErrorLiteral extends Literal {

    /** @see java.lang.Object#toString() */
    @Override
    public String toString() {
        return "#VALUE";
    }
    
}
