package xxl.content.literal;

import xxl.visitor.Visitor;

/**
 * Represents a value that wasn't able to be calculated.
 */
public class ErrorLiteral extends Literal {

    /** @see java.lang.Object#toString() */
    @Override
    public String toString() {
        return "#VALUE";
    }

    /** @see Visitable#accept(Visitor) */
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitErrorLiteral(this);
    }
    
}
