package dontkillthetree.scu.edu.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import dontkillthetree.scu.edu.UI.AddProjectDueDate;
import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.event.DisposeEvent;
import dontkillthetree.scu.edu.event.MilestoneDatabaseOpListener;
import dontkillthetree.scu.edu.event.MyMilestoneDatabaseOpListener;
import dontkillthetree.scu.edu.event.ProjectDatabaseOpListener;
import dontkillthetree.scu.edu.event.PropertyChangeEvent;

public class Project {
    private final long id;
    private String name;
    private Calendar dueDate;
    private List<Milestone> milestones;
    private Milestone currentMilestone;
    private ProjectDatabaseOpListener projectDatabaseOpListener;
    private MilestoneDatabaseOpListener milestoneDatabaseOpListener;

    /**
     * Use this constructor when user creates a new project, i.e. no record in the database
     * @param name
     * @param dueDate
     * @param numberOfMilestones
     * @param projectDatabaseOpListener
     * @param milestoneDatabaseOpListener
     * @param context
     */
    public Project(String name, Calendar dueDate, int numberOfMilestones, ProjectDatabaseOpListener projectDatabaseOpListener, MilestoneDatabaseOpListener milestoneDatabaseOpListener, Context context) {
        Calendar currentDate = Calendar.getInstance();
        if (name == null || context == null || dueDate.before(currentDate) || numberOfMilestones <= 0) {
            throw new IllegalArgumentException();
        }

        this.milestones = new ArrayList<>();
        this.name = name;
        this.dueDate = (Calendar) dueDate.clone();
        Util.toNearestDueDate(this.dueDate);
        int increment = (int) (this.dueDate.getTimeInMillis() - currentDate.getTimeInMillis()) / (24 * 60 * 60 * 1000 * numberOfMilestones);

        // create milestones
        int i;
        for (i = 1; i <= numberOfMilestones; i++) {
            milestones.add(new Milestone("Milestone " + i, currentDate, milestoneDatabaseOpListener, context));
            currentDate.add(Calendar.DATE, increment);
        }
        this.currentMilestone = milestones.get(0);
        this.projectDatabaseOpListener = projectDatabaseOpListener;
        this.milestoneDatabaseOpListener = milestoneDatabaseOpListener;
        this.id = this.projectDatabaseOpListener.onInsert(name, this.dueDate, milestones);
    }

    /**
     * Use this constructor when it is recovered from the database
     * @param id
     * @param name
     * @param dueDate
     * @throws ParseException
     */
    public Project(long id, String name, String dueDate, ProjectDatabaseOpListener projectDatabaseOpListener, MilestoneDatabaseOpListener milestoneDatabaseOpListener) throws ParseException{
        if (name == null || dueDate == null) {
            throw new IllegalArgumentException();
        }

        this.projectDatabaseOpListener = projectDatabaseOpListener;
        Calendar dueDateCalendar = Util.stringToCalendar(dueDate);

        this.id = id;
        this.name = name;
        this.dueDate = dueDateCalendar;
        this.milestones = new ArrayList<>();
        this.milestoneDatabaseOpListener = milestoneDatabaseOpListener;

        this.projectDatabaseOpListener.getMilestones(this.id, this.milestones, this.milestoneDatabaseOpListener);
        sortMilestones();
        updateCurrentMilestone();
    }

    /**
     * Sort the milestones base on due date ascending
     */
    public void sortMilestones() {
        Collections.sort(milestones);
    }

    public void dispose() {
        projectDatabaseOpListener.onDelete(new DisposeEvent(id));
    }

    // getter and setter
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        // update database
        if (projectDatabaseOpListener != null) {
            projectDatabaseOpListener.onUpdate(new PropertyChangeEvent(
                    id,
                    DatabaseContract.ProjectEntry.COLUMN_NAME_NAME,
                    name));
        }

        // update instance
        this.name = name;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        // update instance
        this.dueDate = (Calendar) dueDate.clone();
        Util.toNearestDueDate(this.dueDate);

        // update database
        if (projectDatabaseOpListener != null) {
            projectDatabaseOpListener.onUpdate(new PropertyChangeEvent(
                    id,
                    DatabaseContract.ProjectEntry.COLUMN_NAME_DUE_DATE,
                    Util.calendarToString(this.dueDate)));
        }
    }

    public List<Milestone> getMilestones() {
        return milestones;
    }

    public void removeMilestone(long id) {
        Milestone milestone = Util.getMilestoneById(milestones, id);

        if (milestone != null) {
            milestone.dispose();
            milestones.remove(milestone);
        }

        updateCurrentMilestone();
    }

    public long addMilestone(String name, Calendar dueDate, Context context) {
        Milestone newMilestone = new Milestone(name, dueDate, milestoneDatabaseOpListener, context);
        milestones.add(newMilestone);
        sortMilestones();
        updateCurrentMilestone();

        return newMilestone.getId();
    }

    public Milestone getCurrentMilestone() {
        return currentMilestone;
    }

    public void updateCurrentMilestone() {
        sortMilestones();
        for (Milestone milestone : milestones) {
            if (!milestone.isCompleted()) {
                currentMilestone = milestone;
                return;
            }
        }

        currentMilestone = null;
    }
}
