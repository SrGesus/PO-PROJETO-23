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

public abstract class RangeFunction extends Function{

    protected RangeFunction(Spreadsheet spreadsheet, String[] args) throws FunctionArgException {
        if (args.length != 1) throw new FunctionArgException();
        addRange(spreadsheet.getRange(args[0]));
    }
    
    @Override
    protiected abstract Literal compute();

    @Override
    public String toString() {
        return getName() + "(" + getArg(0) + ":" + getArg(size()-1) + ")";
        
    }  
}

