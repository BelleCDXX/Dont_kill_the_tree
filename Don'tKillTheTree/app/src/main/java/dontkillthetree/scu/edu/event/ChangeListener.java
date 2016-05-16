package dontkillthetree.scu.edu.event;

/**
 * Created by Joey Zheng on 5/15/16.
 */
public interface ChangeListener {
    public void onPropertyChange(PropertyChangeEvent event);

    /**
     * Delete records from the database and dispose the listener
     */
    public void onDispose(DisposeEvent event);
}
