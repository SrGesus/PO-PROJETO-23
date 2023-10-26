package xxl.content;

import xxl.cell.Address;
import xxl.cell.Cell;
import xxl.cell.CellStore;
import xxl.content.literal.Literal;
import xxl.exceptions.InvalidAddressException;
import xxl.observer.Observer;
import xxl.visitor.Visitable;
import xxl.visitor.Visitor;

/**
 * A Function that represents a reference to a Cell.
 * Return the contents of the referenced Cell.
 */
public class CellReference extends ObservableContent implements Observer {

    /** The address of the referenced Cell. */
    private Address _address;

    /** Referenced Cell. */
    private Cell _cell;

    /** Saved value */
    private Literal _value;

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
    public CellReference(Address address, Cell cell) {
        _address = address;
        _cell = cell;
        _cell.attach(this);
        _value = _cell.value();
    }

    /** @see Observer#update() */
    @Override
    public void update() {
        _value = _cell.value();
        notifyObservers();
    }

    /** @see Content#value() */
    @Override
    public Literal value() {
        return _value;
    }

    public Address getAddress() {
        return _address;
    }

    public Cell getCell() {
        return _cell;
    }

    /**
     * This CellReference is no longer an Observer of the referenced Cell.
     * @see Content#close()
     */
    @Override
    public void close() {
        _cell.detach(this);
    }

    /** @see Visitable#accept(Visitor) */
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitCellReference(this);
    }
}