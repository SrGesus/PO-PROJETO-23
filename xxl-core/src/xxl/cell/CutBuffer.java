package xxl.cell;


/**
 * Class representing the cut content buffer
 */
public class CutBuffer{

    int _lines;

    int _columns;

    CellStore _buffer = new CellStoreArray(_lines, _columns);

    /**
     * Constructor
     * @param lines
     * @param columns
     */
    CutBuffer(int lines, int columns){
        _lines = lines;
        _columns = columns;
    }


}