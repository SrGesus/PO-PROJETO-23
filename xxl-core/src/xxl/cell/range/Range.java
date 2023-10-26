package xxl.cell.range;

import java.util.Iterator;
import java.util.NoSuchElementException;

import xxl.cell.Address;
import xxl.cell.Cell;
import xxl.cell.CellStore;
import xxl.exceptions.InvalidAddressException;
import xxl.exceptions.InvalidRangeException;


/**
 * Class that represents a Range of Cells.
 * Has many iterators that iterate over the Cells.
 */
public class Range {
    /** The first Address of this Range */
    private Address _startAddress;

    /** The last Address of this Range */
    private Address _endAddress;

    /** The grid of cells */
    private CellStore _store;

    /**
     * Constructor for Cell Iterator.
     * @param rangeSpecification ::= LINHA;COLUNA:LINHA;COLUNA
     * @throws InvalidRangeException
     */
    public Range(CellStore store, String rangeSpecification) throws InvalidRangeException {
        try {
            /** Parsing of rangeSpecification */
            String[] range = rangeSpecification.split(":");

            _startAddress = new Address(range[0]);
            _endAddress = new Address(range[1]);
            _store = store;

            
            /** Check if Range is either a line or a column */
            if (_startAddress.getLine() != _endAddress.getLine() &&
                _startAddress.getColumn() != _endAddress.getColumn()) {
                throw new InvalidRangeException(rangeSpecification);
            }

            /** If Range start is before the end switch order */
            if (_startAddress.getLine() > _endAddress.getLine() || 
                _startAddress.getColumn() > _endAddress.getColumn()) {
                Address temp = _startAddress;
                _startAddress = _endAddress;
                _endAddress = temp;
            }
            
            /** Attempt to get the last cell to make sure range is in store bounds */
            store.getCellReadOnly(_endAddress);
        } catch (InvalidAddressException | IndexOutOfBoundsException e) {
            throw new InvalidRangeException(rangeSpecification);
        }
    }

    public Address getStartAddress() {
        return _startAddress;
    }

    public Address getEndAddress() {
        return _endAddress;
    }

    /**
     * @return the number of lines in this Range.
     */
    public int getLines() {
        return _endAddress.getLine() - _startAddress.getLine() + 1;
    }

    /**
     * @return the number of columns in this Range.
     */
    public int getColumns() {
        return _endAddress.getColumn() - _startAddress.getColumn() + 1;
    }
 
    /**
     * @return an iterable over the string representation of each cell.
     */
    public Iterable<String> iterStrings() {
        return new Iterable<String>() {
            /** @see java.util.Iterable#iterator() */
            @Override
            public RangeIterator<String> iterator() {
                return new StringIterator(Range.this);
            }
        };
    }

    /**
     * Use when the Cell is to be changed or added observers.
     * @return an iterable that iterates over Cells in the store.
     */
    public Iterable<Cell> iterCells() {
        return new Iterable<Cell>() {
            /** @see java.util.Iterable#iterator() */
            @Override
            public RangeIterator<Cell> iterator() {
                return new CellIterator(Range.this);
            }
        };
    }

    /**
     * Use when they're not going to be changed.
     * @return an Iterator that iterates over Cells.
     */
    public Iterable<Cell> iterCellsReadOnly() {
        return new Iterable<Cell>() {
            /** @see java.util.Iterable#iterator() */
            @Override
            public RangeIterator<Cell> iterator() {
                return new CellReadOnlyIterator(Range.this);
            }
        };
    }

    /**
     * @return an Iterator that iterates over Addresses.
     */
    public Iterable<Address> iterAddresses() {
        return new Iterable<Address>() {
            /** @see java.util.Iterable#iterator() */
            @Override
            public RangeIterator<Address> iterator() {
                return new AddressIterator(Range.this);
            }
        };
    }

    /**
     * An abstract iterator that iterates over a range of cells.
     * @param T the type of what it is iterating over.
     */
    public abstract class RangeIterator<T> implements Iterator<T> {

        /** The current Address of this Range */
        private Address _currentAddress = _startAddress;

        /**
         * @return the next Address to be iterated.
         */
        public Address getCurrentAddress() {
            return _currentAddress;
        }

        /**
         * @return the store of this Range.
         */
        public CellStore getStore() {
            return _store;
        }

        /**
         * @return the next Cell in the range.
         * @throws NoSuchElementException
         */
        protected Cell getNextCell() throws InvalidAddressException {
            return _store.getCellReadOnly(getCurrentAddress());
        }

        /**
         * Template method for subclasses.
         * @param cell
         * @return the result of a transformation of the cell.
         */
        protected abstract T getResult(Cell cell);

        /** 
         * @return the next result in the range.
         * @throws NoSuchElementException should never happen if hasNext() returned true.
         * @see java.util.Iterator#next()
         */
        @Override
        public T next() throws NoSuchElementException {
            try {
                T result = getResult(getNextCell());
                if (_currentAddress.getColumn() < _endAddress.getColumn())
                    _currentAddress = _currentAddress.nextColumn();
                else {
                    _currentAddress = _currentAddress.nextLine();
                    /* This is not needed for single column/line ranges */
                    // _currentAddress.setColumn(_startAddress.getColumn());
                }
                return result;
            } catch (InvalidAddressException e) {
                throw new NoSuchElementException();
            }
        }

        /** @see java.util.Iterator#hasNext() */
        @Override
        public boolean hasNext() {
            return _currentAddress.getLine() <= _endAddress.getLine() &&
                   _currentAddress.getColumn() <= _endAddress.getColumn();
        }
    }

    @Override
    public String toString() {
        return _startAddress + ":" + _endAddress;
    }

}
