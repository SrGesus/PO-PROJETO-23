package xxl.function;

import xxl.Spreadsheet;
import xxl.content.Content;
import xxl.content.literal.ErrorLiteral;
import xxl.content.literal.Literal;
import xxl.exceptions.InvalidExpressionException;
import xxl.exceptions.FunctionArgException;
import xxl.exceptions.UnexpectedContentException;
import xxl.exceptions.InvalidRangeException;

public abstract class RangeFunction extends Function{

    /**
     * Constructor.
     * @param spreadsheet
     * @param args
     * @throws FunctionArgException
     */
    protected RangeFunction(Spreadsheet spreadsheet, String[] args) throws FunctionArgException {
        if (args.length != 1) throw new FunctionArgException();
        try {
            addRange(spreadsheet.getGama(args[0]));
        } catch (InvalidRangeException e) {
            throw new FunctionArgException(); 
        }
    }

    @Override
    protected final Literal compute() {
        try {
            return result();
        } catch (UnexpectedContentException | InvalidExpressionException | ArithmeticException e) {
            return new ErrorLiteral();
        }
    }

    /**
     * Template method.
     * @return result of the function
     */
    protected abstract Literal result() throws UnexpectedContentException, InvalidExpressionException;
    

    /** @see Content#toString() */
    @Override
    public String toString() {
        return getName() + "(" + getArg(0) + ":" + getArg(size()-1) + ")";
    }  
}

