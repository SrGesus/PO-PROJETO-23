package xxl.cell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import xxl.exceptions.InvalidAddressException;
import xxl.exceptions.InvalidRangeException;

/**
 * 
 */
public class Range implements Iterator<Cell> {
        /** The first Address of this Range */
        private Address _startAddress;

        /** The current Address of this Range */
        private Address _currentAddress;

        /** The last Address of this Range */
        private Address _endAddress;

        /** The grid of cells */
        CellStore _store;

        /**
         * Constructor for Cell Iterator.
         * @param rangeSpecification
         * @throws InvalidRangeException
         */
        Range(CellStore store, String rangeSpecification) throws InvalidRangeException {
            try {
                /** Parsing of rangeSpecification */
                String[] range = rangeSpecification.split(":");

                _startAddress = new Address(range[0]);
                _currentAddress = _startAddress;
                _endAddress = new Address(range[1]);
                _store = store;


                /** Attempt to get the last cell to make sure range is in bounds */
                store.getCell(_endAddress);

                /** Check if Range start is before the end */
                if (_startAddress.getLine() > _endAddress.getLine() || 
                    _startAddress.getColumn() > _endAddress.getColumn()) {
                    throw new InvalidRangeException(rangeSpecification);
                }

                /** Check if Range is either a line or a column */
                if (_startAddress.getLine() != _endAddress.getLine() &&
                    _startAddress.getColumn() != _endAddress.getColumn()) {
                    throw new InvalidRangeException(rangeSpecification);
                }
            } catch (InvalidAddressException | IndexOutOfBoundsException e) {
                throw new InvalidRangeException(rangeSpecification);
            }
        }

        /**
         * Turns the Range into a collection of Cells.
         * Consumes the Range (it may not be used again).
         * @return the collection of Cells.
         */
        public Collection<Cell> collect() {
            Collection<Cell> cells = new ArrayList<Cell>();
            while (hasNext()) {
                cells.add(next());
            }
            return cells;
        }

        /**
         * Turns the Range into a collection of Strings.
         * @return ::= LINHA;COLUNA|content=expression
         */
        public Collection<String> toStrings() {
            Collection<String> strings = new ArrayList<String>();
            while (hasNext()) {
                strings.add(getCurrentAddress() + "|" + next());
            }
            return strings;
        }

        /**
         * @return the next Address to be iterated.
         */
        public Address getCurrentAddress() {
            return _currentAddress;
        }

        /** @see java.util.Iterator#hasNext() */
        @Override
        public boolean hasNext() {
            return _currentAddress.getLine() <= _endAddress.getLine() &&
                   _currentAddress.getColumn() <= _endAddress.getColumn();
        }

        /** 
         * @return the next Cell in the range.
         * @throws NoSuchElementException should never happen if hasNext() returned true.
         * @see java.util.Iterator#next()
         */
        @Override
        public Cell next() throws NoSuchElementException {
            try {
                Cell cell = _store.getCell(_currentAddress);
                if (_currentAddress.getColumn() < _endAddress.getColumn())
                    _currentAddress = _currentAddress.nextColumn();
                else {
                    _currentAddress = _currentAddress.nextLine();
                    /* This is not needed for single column/line ranges */
                    // _currentAddress.setColumn(_startAddress.getColumn());
                }
                return cell;
            } catch (InvalidAddressException e) {
                throw new NoSuchElementException();
            }
        }
    }
