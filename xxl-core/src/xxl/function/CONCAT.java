package xxl.function;
import xxl.Spreadsheet;
import xxl.content.literal.StringLiteral;
import xxl.exceptions.InvalidExpressionException;
import xxl.exceptions.FunctionArgException;
import xxl.exceptions.UnexpectedContentException;
import xxl.visitor.Visitable;
import xxl.visitor.Visitor;

public class CONCAT extends RangeFunction {

    /**
     * Constructor.
     * @param spreadsheet
     * @param args
     * @throws FunctionArgException
     */
    public CONCAT(Spreadsheet spreadsheet, String[] args) throws FunctionArgException {
        super(spreadsheet, args);
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
            } 
            catch (UnexpectedContentException e) {
                // Do nothing
            }
        }
        return new StringLiteral(result);
    }


    /**
     * @return the name of the function
     * @see Function#getName()
     */
    @Override
    public String getName() {
        return "CONCAT";
    }

    /** @see Visitable#accept(Visitor) */
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitCONCAT(this);
    }
}
