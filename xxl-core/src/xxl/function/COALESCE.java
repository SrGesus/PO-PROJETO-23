package xxl.function;

import java.util.List;

import xxl.Spreadsheet;
import xxl.content.Content;
import xxl.content.literal.StringLiteral;
import xxl.exceptions.InvalidExpressionException;
import xxl.exceptions.FunctionArgException;
import xxl.exceptions.UnexpectedContentException;
import xxl.visitor.Visitable;
import xxl.visitor.Visitor;

public class COALESCE extends RangeFunction {

    /**
     * Constructor.
     * @param spreadsheet
     * @param args
     * @throws FunctionArgException
     */
    public COALESCE(Spreadsheet spreadsheet, String[] args) throws FunctionArgException {
        super(spreadsheet, args);
    }

    /**
     * Constructor from Content.
     * @param args
     */
    public COALESCE(List<Content> args) {
        super(args);
    }

    /**
     * @return the result of the function
     * @see RangeFunction#result()
     */
    @Override
    protected StringLiteral result() throws UnexpectedContentException, InvalidExpressionException{
        String result = "\'";
        for (int i = 0; i < size(); i++) {
            try {
                result += getArg(i).getString();
                return new StringLiteral(result);
            } 
            catch (UnexpectedContentException e) {
                /* Ignore non-Strings */
            }
        }
        return new StringLiteral(result);
    }

    /** @see Visitable#accept(Visitor) */
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitCOALESCE(this);
    }
}
