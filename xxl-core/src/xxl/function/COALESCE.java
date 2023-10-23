package xxl.function;

import xxl.Spreadsheet;
import xxl.content.literal.StringLiteral;
import xxl.exceptions.InvalidExpressionException;
import xxl.exceptions.FunctionArgException;
import xxl.exceptions.UnexpectedContentException;

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
     * @return the result of the function
     * @see RangeFunction#result()
     */
    @Override
    protected StringLiteral result() throws UnexpectedContentException, InvalidExpressionException{
        for (int i = 0; i < size(); i++) {
            try {
                return new StringLiteral(getArg(i).getString()); 
            } 
            catch (UnexpectedContentException e) {
            }
        }
        return new StringLiteral("\'"); 
    }

 
    /**
     * @return the name of the function
     * @see Function#getName()
     */
    @Override
    public String getName() {
        return "COALESCE";
    }
}
