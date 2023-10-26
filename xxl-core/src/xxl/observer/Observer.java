package xxl.observer;

/** 
 * Observer design pattern for Cells and Content. 
 * To do an action when an Observable that
 * is being observed is changed.
 */
public interface Observer {

    /** To do when an Observable is changed  */
    public void update();

    /** 
     * To do when an Observable is to be no longer used.
     * Remove this Observer from its Observables. 
     */
    public void close();
}
