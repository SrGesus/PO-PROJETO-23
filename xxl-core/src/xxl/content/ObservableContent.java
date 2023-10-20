package xxl.content;

import java.util.ArrayList;
import java.util.List;

import xxl.observer.Observer;

/**
 * Implements the Observable interface for Content.
 */
public abstract class ObservableContent extends Content {
    
    /* Observers of this Content */
    private List<Observer> _observers = new ArrayList<Observer>();

    /**
     * @param observer to attach
     * @see Observable#attach(Observer)
     */
    @Override
    public final void attach(Observer observer) {
        _observers.add(observer);
    }

    /**
     * @param observer to detach
     * @see Observable#detach(Observer)
     */
    @Override
    public final void detach(Observer observer) {
        _observers.remove(observer);
    }

    /**
     * Notify all observers of the change in this Content.
     * @see Observable#notifyObservers()
     */
    @Override
    public final void notifyObservers() {
        for (Observer observer : _observers) {
            observer.update();
        }
    }

}
