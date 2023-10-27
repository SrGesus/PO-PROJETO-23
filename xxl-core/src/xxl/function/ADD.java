package xxl.function;

import xxl.Spreadsheet;
import xxl.content.Content;
import xxl.exceptions.FunctionArgException;
import xxl.visitor.Visitable;
import xxl.visitor.Visitor;

public class ADD extends BinaryFunction {

    /**
     * Constructor from Strings.
     * @param spreadsheet
     * @param args
     * @throws FunctionArgException
     */
    public ADD(Spreadsheet spreadsheet, String[] args) throws FunctionArgException {
        super(spreadsheet, args);
    }

    /**
     * Constructor from Content.
     * @param c1
     * @param c2
     */
    public ADD(Content c1, Content c2) {
        super(c1, c2);
    }

    /**
     * @return the result of the function
     * @see BinaryFunction#result(int, int)
     */
    @Override
    protected int result(int x, int y) {
        return x + y;
    }

    /** @see Visitable#accept(Visitor) */
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitADD(this);
    }
}
