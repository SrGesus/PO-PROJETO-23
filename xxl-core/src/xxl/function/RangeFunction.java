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
        try {addRange(spreadsheet.getGama(args[0]));}
        catch (InvalidRangeException e) { throw new FunctionArgException(); }
    }
    
    @Override
    protected final Literal compute() {
        try {
            return new IntLiteral(result());
        } catch (UnexpectedContentException | ArithmeticException e) {
            return new ErrorLiteral();
        }
    }

    /**
     * Template method.
     * @return result of the function
     */
    protected abstract int result() throws UnexpectedContentException;
    

    /** @see Content#toString() */
    @Override
    public String toString() {
        return getName() + "(" + getArg(0) + ":" + getArg(size()-1) + ")";
        
    }  
}

