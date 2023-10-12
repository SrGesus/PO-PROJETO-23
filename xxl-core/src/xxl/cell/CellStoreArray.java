package xxl.cell;

import xxl.exceptions.InvalidAddressException;

/**
 * Class that stores all Cells of a Spreadsheet in a primitive array.
 * Will only create lines and cells when needed.
 */
public class CellStoreArray extends CellStore {
    
    /** The CellStore itself */
    private Cell[][] _cells;

    /**
     * Constructor.
     * @param lines
     * @param columns
     */
    public CellStoreArray(int lines, int columns) {
        super(lines, columns);
        _cells = new Cell[getLines()][];
    }

    /**
     * If the Cell is not in the store, an empty Cell will be created in the store.
     * @param addr
     * @return the Cell at the given Address
     * @throws InvalidAddressException
     * @see CellStore#getCell(Address)
     */
    @Override
    public Cell getCell(Address address) throws InvalidAddressException {
        try {
            Cell[] line = _cells[address.getLine()];
            if (line == null) line = _cells[address.getLine()] = new Cell[getColumns()];
            Cell cell = line[address.getColumn()];
            if (cell == null) cell = line[address.getColumn()] = new Cell();
            return cell;
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidAddressException(address.toString());
        }
    };

    /**
     * If the Cell is not in the store, an empty Cell unnattached to the store is returned.
     * @param addr
     * @return the Cell at the given Address if it exists, an empty Cell otherwise.
     * @throws InvalidAddressException
     * @see CellStore#getCellReadOnly(Address)
     */
    @Override
    public Cell getCellReadOnly(Address address) throws InvalidAddressException {
        try {
            Cell[] line = _cells[address.getLine()];
            if (line == null) return new Cell();
            Cell cell = line[address.getColumn()];
            if (cell == null) return new Cell();
            return cell;
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidAddressException(address.toString());
        }
    };

    /**
     * Removes Cell from the store if it is empty,
     * and without observers.
     * @param addr
     * @see CellStore#deleteEmptyCell(Address)
     */
    @Override
    public void deleteEmptyCell(Address address) {
        try {
            Cell[] line = _cells[address.getLine()];
            if (line == null) return;
            Cell cell = line[address.getColumn()];
            if (cell == null) return;
            line[address.getColumn()] = null;
        } catch (IndexOutOfBoundsException e) {
            return;
        }
    };

    /** 
     * Will remove all empty lines from the store.
     * @see CellStore#cleanUp() 
     */
    @Override
    public void cleanUp() {
        for (int i = 0; i < getColumns(); i++) {
            Cell[] line = _cells[i];
            boolean empty = true;
            if (line == null) continue;
            for (Cell cell : line) {
                if (cell != null && !cell.isDeletable()) {
                    empty = false;
                    break;
                }
            }
            if (empty) _cells[i] = null;
        }
    }


}
