package xxl.function;

import xxl.Spreadsheet;
import xxl.exceptions.FunctionArgException;
import xxl.visitor.Visitable;
import xxl.visitor.Visitor;

public class ADD extends BinaryFunction {

    /**
     * Constructor.
     * @param spreadsheet
     * @param args
     * @throws FunctionArgException
     */
    public ADD(Spreadsheet spreadsheet, String[] args) throws FunctionArgException {
        super(spreadsheet, args);
    }

    /**
     * @return the result of the function
     * @see BinaryFunction#result(int, int)
     */
    @Override
    protected int result(int x, int y) {
        return x + y;
    }

    /**
     * @return the name of the function
     * @see Function#getName()
     */
    @Override
    public String getName() {
        return "ADD";
    }

    /** @see Visitable#accept(Visitor) */
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitADD(this);
    }
}
