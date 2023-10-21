package xxl.function;

import xxl.Spreadsheet;
import xxl.cell.range.Range;
import xxl.content.Content;
import xxl.content.literal.ErrorLiteral;
import xxl.content.literal.IntLiteral;
import xxl.content.literal.Literal;
import xxl.exceptions.FunctionArgException;
import xxl.exceptions.FunctionNameException;
import xxl.exceptions.UnexpectedContentException;

public class AVERAGE extends RangeFunction {

    /**
     * Constructor.
     * @param spreadsheet
     * @param args
     * @throws FunctionArgException
     */
    public AVERAGE(Spreadsheet spreadsheet, String[] args) throws FunctionArgException {
        super(spreadsheet, args);
    }


    /**
     * @return the result of the function
     * @see BinaryFunction#result(int, int)
     */
    @Override
    protected int result() throws UnexpectedContentException{
        int result = 0;
        for (int i = 0; i < size(); i++) {
            try {
                result += getArg(i).getInt();
            } catch (UnexpectedContentException e) { // FIXME: might want to throw more appropriate exception, not catch it
                return 0;
            }
        }
        return result; 
    }


    /**
     * @return the name of the function
     * @see Function#getName()
     */
    @Override
    public String getName() {
        return "AVERAGE";
    }
}
