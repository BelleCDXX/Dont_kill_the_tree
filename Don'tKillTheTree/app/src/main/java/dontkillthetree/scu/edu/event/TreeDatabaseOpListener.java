package dontkillthetree.scu.edu.event;

import java.util.Calendar;

public interface TreeDatabaseOpListener {
    int[] onSelect();
    void onUpdate(PropertyChangeEvent event);
}
