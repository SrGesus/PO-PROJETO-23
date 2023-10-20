package xxl.content;

import xxl.cell.Address;
import xxl.cell.Cell;
import xxl.cell.CellStore;
import xxl.content.literal.Literal;
import xxl.exceptions.InvalidAddressException;
import xxl.observer.Observer;

/**
 * A Function that represents a reference to a Cell.
 * Return the contents of the referenced Cell.
 */
public class CellReference extends ObservableContent implements Observer {

    /** The address of the referenced Cell. */
    Address _address;

    /** Referenced Cell. */
    Cell _cell;

    /**
     * Constructor for CellReference.
     * @param store
     * @param address ::= LINHA;COLUNA
     * @throws InvalidAddressException
     */
    public CellReference(CellStore store, String address) throws InvalidAddressException {
        this(new Address(address), store.getCell(new Address(address)));
    }

    /**
     * Constructor for CellReference.
     * @param store
     * @param address
     * @throws InvalidAddressException
     */
    public CellReference(Address address, Cell cell) throws InvalidAddressException {
        _address = address;
        _cell = cell;
    }

    /** @see Observer#update() */
    @Override
    public void update() {
        notifyObservers();
    }

    /** @see Content#value() */
    @Override
    public Literal value() {
        return _cell.value();
    }

    /**
     * @return the address of the referenced Cell.
     */
    @Override
    public String toString() {
        return _address.toString();
    }

    /**
     * This CellReference is no longer an Observer of the referenced Cell.
     * @see Content#close()
     */
    @Override
    public void close() {
        _cell.detach(this);
    }
}