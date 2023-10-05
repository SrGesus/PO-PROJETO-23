package xxl.cell;

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
        _cells = new Cell[_lines][];
    }

    /**
     * Getter for the cell that will create new cell 
     * if it doesn't exist at the address.
     * @param line
     * @param column
     * @return the Cell
     */
    public Cell getCell(Address addr) {
        Cell[] line = _cells[addr.getLine()];
        if (line == null) line = _cells[addr.getLine()] = new Cell[_columns];
        Cell cell = line[addr.getColumn()];
        if (cell == null) cell = line[addr.getColumn()] = new Cell();
        return cell;
    };

}
