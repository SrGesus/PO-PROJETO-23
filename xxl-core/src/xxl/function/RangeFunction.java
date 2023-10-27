package xxl.function;

import java.util.List;

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
     * Constructor from String.
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

    /**
     * Constructor from Content.
     * @param args
     */
    protected RangeFunction(List<Content> args) {
        super(args);
    }

    /**
     * @return the result of the function
     * @see Function#compute()
     */
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

}

