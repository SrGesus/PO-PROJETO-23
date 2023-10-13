package xxl.cell;


/**
 * Class representing the cut content buffer
 */
public class CutBuffer{

    CellStore _buffer;

    /**
     * Constructor
     * @param lines
     * @param columns
     */
    CutBuffer(int lines, int columns){
        _buffer = new CellStoreArray(lines, columns);
    }


}