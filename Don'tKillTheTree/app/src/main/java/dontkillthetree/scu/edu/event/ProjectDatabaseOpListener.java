package dontkillthetree.scu.edu.event;

import android.content.Context;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import dontkillthetree.scu.edu.model.Milestone;

public interface ProjectDatabaseOpListener{
    String[] onSelect(long id);
    long onInsert(String name, Calendar dueDate, String guardianName, String guardianPhone, List<Milestone> milestones);
    void getMilestones(long id, List<Milestone> milestones, MilestoneDatabaseOpListener milestoneDatabaseOpListener) throws ParseException;
    void onUpdate(PropertyChangeEvent event);
    /**
     * Delete records from the database and dispose the listener
     */
    void onDelete(DisposeEvent event);
}
