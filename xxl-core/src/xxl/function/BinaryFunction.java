package xxl.function;

import java.util.Arrays;

import xxl.Spreadsheet;
import xxl.content.Content;
import xxl.content.literal.ErrorLiteral;
import xxl.content.literal.IntLiteral;
import xxl.content.literal.Literal;
import xxl.exceptions.FunctionArgException;
import xxl.exceptions.FunctionNameException;
import xxl.exceptions.UnexpectedContentException;

public abstract class BinaryFunction extends Function {

    /**
     * Constructor from Strings.
     * @param spreadsheet
     * @param args
     * @throws FunctionArgException
     */
    protected BinaryFunction(Spreadsheet spreadsheet, String[] args) throws FunctionArgException {
        try {
            if (args.length != 2) 
                throw new FunctionArgException();
            for (String arg : args)
                addArg(spreadsheet.parseContent(arg, false));
        } catch (FunctionNameException e) {
            throw new FunctionArgException();
        }
    }

    /**
     * Constructor from Content.
     * @param ...args
     */
    protected BinaryFunction(Content... args) {
        super(Arrays.asList(args));
    }
    
    /**
     * @return the result of the function
     * @see Function#compute()
     */
    @Override
    protected final Literal compute() {
        try {
            return new IntLiteral(result(getArg(0).getInt(), getArg(1).getInt()));
        } catch (UnexpectedContentException | ArithmeticException e) {
            return new ErrorLiteral();
        }
    }

    /**
     * Template method.
     * @param x
     * @param y
     * @return result of the function
     */
    protected abstract int result(int x, int y) ;

}
