package xxl.observer;

/**
 * 
 */
public interface Observable {
    /**
     * Adds observer to the list of observers to be notified.
     * @param observer to attach
     */
    public void attach(Observer observer);

    /**
     * Stop given observer from observing this Observable.
     * @param observer to detach
     */
    public void detach(Observer observer);

    /**
     * Notifies all observers that there was a change in this Observable.
     */
    public void notifyObservers();
}
