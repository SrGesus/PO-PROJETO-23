package xxl.cell;

import java.io.Serializable;
import java.util.Iterator;

import xxl.cell.range.Range;
import xxl.exceptions.InvalidRangeException;

public class CutBuffer implements Serializable {
    
    private CellStore _buffer = new CellStoreArray(0, 0);

    /**
     * Copies the content of the target cells into the cut buffer.
     * @param gamaSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     */
    public void copy(Range origin) throws InvalidRangeException {
        _buffer = new CellStoreArray(origin.getLines(), origin.getColumns());
        Iterator<Cell> originIterator = origin.iterCellsReadOnly().iterator();
        Iterator<Cell> cutBufferIterator = getCutBufferRange().iterCells().iterator();
        while (cutBufferIterator.hasNext() && originIterator.hasNext())
            cutBufferIterator.next().paste(originIterator.next()).close();
    }

    /**
     * Pastes the cut buffer content into the target cells.
     * @param gamaSpecification ::= LINHA;COLUNA:LINHA;COLUNA | LINHA;COLUNA
     */
    public void paste(Range destinationRange, CellStore destinationStore) throws InvalidRangeException {
        // If provided range is single cell
        if (destinationRange.getColumns() == 1 && destinationRange.getLines() == 1) {
            Address cellAddr = destinationRange.getStartAddress();
            // If buffer is horizontal
            if (_buffer.getColumns() > 1)
                destinationRange = destinationStore.getRange(cellAddr, new Address(cellAddr.getLine(), destinationStore.getColumns()-1));
            // If buffer is vertical
            else
                destinationRange = destinationStore.getRange(cellAddr, new Address(destinationStore.getLines()-1, cellAddr.getColumn()));
        // otherwise if geometry does not match
        } else if (destinationRange.getColumns() != _buffer.getColumns() && destinationRange.getLines() != _buffer.getLines())
            throw new InvalidRangeException(destinationRange.toString());
        Iterator<Cell> destinationIterator = destinationRange.iterCells().iterator();
        Iterator<Cell> cutBufferIterator = getCutBufferRange().iterCellsReadOnly().iterator();
        // While there are cells and space to paste
        while (destinationIterator.hasNext() && cutBufferIterator.hasNext())
            destinationIterator.next().paste(cutBufferIterator.next());
    }


    public Range getCutBufferRange() {
        try {
            return _buffer.getRange(new Address(0, 0), new Address(_buffer.getLines()-1, _buffer.getColumns()-1));
        } catch (InvalidRangeException e) {
            /* Logically Unreachable */
            e.printStackTrace();
            return null;
        }
    }
}
