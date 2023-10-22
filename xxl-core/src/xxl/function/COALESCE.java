package xxl.function;

import xxl.Spreadsheet;
import xxl.cell.range.Range;
import xxl.content.Content;
import xxl.content.literal.ErrorLiteral;
import xxl.content.literal.StringLiteral;
import xxl.content.literal.Literal;
import xxl.exceptions.InvalidExpressionException;
import xxl.exceptions.FunctionArgException;
import xxl.exceptions.FunctionNameException;
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
     * @see BinaryFunction#result(int, int)
     */
    @Override
    protected StringLiteral result() throws UnexpectedContentException, InvalidExpressionException{
        String result = "";
        for (int i = 0, j = 0; i < size() && j != 1; i++) {
            try {
                result += getArg(i).getString();
                j = 1;
            } 
            catch (UnexpectedContentException e) {
                j = 0;
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
        return "COALESCE";
    }
}
