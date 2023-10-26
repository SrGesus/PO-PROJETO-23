package xxl.function;

import xxl.Spreadsheet;
import xxl.exceptions.FunctionArgException;
import xxl.visitor.Visitable;
import xxl.visitor.Visitor;

public class MUL extends BinaryFunction {

    /**
     * Constructor.
     * @param spreadsheet
     * @param args
     * @throws FunctionArgException
     */
    public MUL(Spreadsheet spreadsheet, String[] args) throws FunctionArgException {
        super(spreadsheet, args);
    }

    /**
     * @return the result of the function
     * @see BinaryFunction#result(int, int)
     */
    @Override
    protected int result(int x, int y) {
        return x * y;
    }

    /**
     * @return the name of the function
     * @see Function#getName()
     */
    @Override
    public String getName() {
        return "MUL";
    }

    /** @see Visitable#accept(Visitor) */
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitMUL(this);
    }
}
