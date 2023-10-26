package xxl.function;

import java.util.List;

import xxl.Spreadsheet;
import xxl.content.Content;
import xxl.content.literal.IntLiteral;
import xxl.content.literal.Literal;
import xxl.exceptions.InvalidExpressionException;
import xxl.exceptions.FunctionArgException;
import xxl.exceptions.UnexpectedContentException;
import xxl.visitor.Visitable;
import xxl.visitor.Visitor;

public class PRODUCT extends RangeFunction {

    /**
     * Constructor.
     * @param spreadsheet
     * @param args
     * @throws FunctionArgException
     */
    public PRODUCT(Spreadsheet spreadsheet, String[] args) throws FunctionArgException {
        super(spreadsheet, args);
    }

    /**
     * Constructor from Content.
     * @param args
     */
    public PRODUCT(List<Content> args) {
        super(args);
    }

    /**
     * @return the result of the function
     * @see RangeFunction#result()
     */
    @Override
    protected Literal result() throws UnexpectedContentException, InvalidExpressionException{
        int result = 1;
        for (int i = 0; i < size(); i++) {
            result *= getArg(i).getInt();
        }
        return new IntLiteral(result); 
    }

    /** @see Visitable#accept(Visitor) */
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitPRODUCT(this);
    }
}
