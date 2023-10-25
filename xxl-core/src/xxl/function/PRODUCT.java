package xxl.function;

import xxl.Spreadsheet;
import xxl.content.literal.IntLiteral;
import xxl.content.literal.Literal;
import xxl.exceptions.InvalidExpressionException;
import xxl.exceptions.FunctionArgException;
import xxl.exceptions.UnexpectedContentException;

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
     * @return the result of the function
     * @see RangeFunction#result(int, int)
     */
    @Override
    protected Literal result() throws UnexpectedContentException, InvalidExpressionException{
        int result = 1;
        for (int i = 0; i < size(); i++) {
            result *= getArg(i).getInt();
        }
        return new IntLiteral(result); 
    }


    /**
     * @return the name of the function
     * @see Function#getName()
     */
    @Override
    public String getName() {
        return "PRODUCT";
    }
}
