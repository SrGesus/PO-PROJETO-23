package xxl.cell;

import xxl.exceptions.InvalidAddressException;

/**
 * Represents an address in the spreadsheet that corresponds to a single cell.
 */
class Address {
    /** The line of this address */
    int _line;

    /** The column of this address */
    int _column;

    /**
     * Constructor for int parameters.
     * @param line assumed to be >= 0.
     * @param column assumed to be >= 0.
     */
    public Address(int line, int column) {
        _line = line;
        _column = column;
    }

    /**
     * Constructor that evaluates expression into address.
     * @param expression to be evaluated.
     */
    public Address(String expression) throws InvalidAddressException {
        try {
            String[] address = expression.split(";");
            if (address.length != 2) throw new InvalidAddressException(expression);
            _line = Integer.parseInt(address[0]) - 1;
            _column = Integer.parseInt(address[1]) - 1;
            if (_line < 0 || _column < 0) throw new InvalidAddressException(expression);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new InvalidAddressException(expression);
        }
    }

    /**
     * @return the line of this address.
     */
    public int getLine() {
        return _line;
    }

    /**
     * @return the column of this address.
     */
    public int getColumn() {
        return _column;
    }

    /**
     * @return the address of the cell below this one.
     */
    public Address nextLine() {
        return new Address(_line+1, _column);
    }

    /**
     * @return the address of the cell to the right of this one.
     */
    public Address nextColumn() {
        return new Address(_line, _column+1);
    }
    
    /**
     * Package-private setter for column.
     * @param column
     */
    void setColumn(int column) {
        _column = column;
    }

    /** @see Object#toString() */
    @Override
    public String toString() {
        return (_line + 1) + ";" + (_column + 1);
    }

    /** @see Object#equals(Object) */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Address a) {
            return _line == a.getLine() && _column == a.getColumn();
        }
        return false;
    }
}
