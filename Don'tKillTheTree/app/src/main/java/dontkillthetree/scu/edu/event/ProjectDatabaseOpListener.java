package dontkillthetree.scu.edu.event;

import android.content.Context;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import dontkillthetree.scu.edu.model.Milestone;

/**
 * Created by Joey Zheng on 5/20/16.
 */
public interface ProjectDatabaseOpListener{
    long onInsert(String name, Calendar dueDate, List<Milestone> milestones);
    void getMilestones(long id, List<Milestone> milestones, MilestoneDatabaseOpListener milestoneDatabaseOpListener) throws ParseException;
    void onUpdate(PropertyChangeEvent event);
    /**
     * Delete records from the database and dispose the listener
     */
    void onDelete(DisposeEvent event);
}
