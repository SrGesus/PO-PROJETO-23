package xxl.search;

import java.util.Comparator;

import xxl.cell.Address;
import xxl.cell.Cell;
import xxl.visitor.ExpressionGenerator;

public class SearchResult {
    
    private Address _address;
    
    private Cell _cell;

    public SearchResult(Address address, Cell cell) {
        _address = address;
        _cell = cell;
    }

    public Address getAddress() {
        return _address;
    }

    public Cell getCell() {
        return _cell;
    }

    public String toString() {
        return _address + "|" + _cell.accept(new ExpressionGenerator());
    }
}
