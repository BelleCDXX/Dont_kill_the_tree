package dontkillthetree.scu.edu.event;

import java.util.Calendar;

/**
 * Created by Joey Zheng on 5/24/16.
 */
public interface TreeDatabaseOpListener {
    int[] onSelect();
    void onUpdate(PropertyChangeEvent event);
}
