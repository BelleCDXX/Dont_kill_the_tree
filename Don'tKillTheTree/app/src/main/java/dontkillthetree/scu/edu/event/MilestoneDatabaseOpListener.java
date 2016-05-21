package dontkillthetree.scu.edu.event;

import java.util.Calendar;

/**
 * Created by Joey Zheng on 5/20/16.
 */
public interface MilestoneDatabaseOpListener{
    long onInsert(String name, Calendar dueDate);
    void onUpdate(PropertyChangeEvent event);
    /**
     * Delete records from the database and dispose the listener
     */
    void onDelete(DisposeEvent event);
}
