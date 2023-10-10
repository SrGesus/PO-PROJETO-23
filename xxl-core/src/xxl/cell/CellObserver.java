package xxl.cell;

import java.io.Serializable;

/** 
 * Observer design pattern for Cells. 
 * To notify a Cell (subject Cell) when a Cell's Content that
 * they depend on (observed Cell) is changed.
 */
public class CellObserver implements Serializable {

    /** The Cell that is to be notified */
    Cell _subject;

    /**
     * Constructor.
     * @param subject Cell that needs to be notified.
     */
    public CellObserver(Cell subject) {
        _subject = subject;
    }

    /**
     * Notify the subject Cell that recalculation is needed.
     */
    public void update() {
        _subject.dirty();
    }

    /**
     * Detach this observer from the given Cell.
     * @param cell
     */
    public void detach(Cell cell) {
        cell.detach(this);
    }
}
