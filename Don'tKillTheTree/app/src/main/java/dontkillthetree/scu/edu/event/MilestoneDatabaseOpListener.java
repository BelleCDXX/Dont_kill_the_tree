package dontkillthetree.scu.edu.event;

import java.util.Calendar;

public interface MilestoneDatabaseOpListener{
    long onInsert(String name, Calendar dueDate);
    void onUpdate(PropertyChangeEvent event);
    /**
     * Delete records from the database and dispose the listener
     */
    void onDelete(DisposeEvent event);
}
