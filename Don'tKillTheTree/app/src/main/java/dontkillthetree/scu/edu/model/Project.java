package dontkillthetree.scu.edu.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.database.DatabaseHelper;
import dontkillthetree.scu.edu.event.ChangeListener;
import dontkillthetree.scu.edu.event.DisposeEvent;
import dontkillthetree.scu.edu.event.PropertyChangeEvent;

public class Project {
    private final long id;
    private String name;
    private Calendar dueDate;
    private List<Milestone> milestones;
    private Milestone currentMilestone;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private ChangeListener changeListener;

    /**
     * Use this constructor when user creates a new project, i.e. no record in the database
     * @param name
     * @param dueDate
     * @param numberOfMilestones
     * @param context
     */
    public Project(String name, Calendar dueDate, int numberOfMilestones, Context context) {
        Calendar currentDate = Calendar.getInstance();
        if (name == null || context == null || dueDate.before(currentDate) || numberOfMilestones <= 0) {
            throw new IllegalArgumentException();
        }

        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        milestones = new ArrayList<>();

        this.name = name;
        this.dueDate = (Calendar) dueDate.clone();
        Util.toNearestDueDate(this.dueDate);
        int increment = (int) (this.dueDate.getTimeInMillis() - currentDate.getTimeInMillis()) / (24 * 60 * 60 * 1000 * numberOfMilestones);

        // create milestones
        int i;
        for (i = 1; i <= numberOfMilestones; i++) {
            milestones.add(new Milestone("Milestone " + i, currentDate, context));
            currentDate.add(Calendar.DATE, increment);
        }
        this.currentMilestone = milestones.get(0);

        // get project id
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ProjectEntry.COLUMN_NAME_NAME, name);
        values.put(DatabaseContract.ProjectEntry.COLUMN_NAME_DUE_DATE, Util.calendarToString(this.dueDate));
        values.put(DatabaseContract.ProjectEntry.COLUMN_NAME_CURRENT_MILESTONE_ID, currentMilestone.getId());
        this.id = db.insert(DatabaseContract.ProjectEntry.TABLE_NAME, "null", values);

        // connect project id with milestone ids in the database
        for (i = 1; i <= numberOfMilestones; i++) {
            values = new ContentValues();
            values.put(DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_PROJECT_ID, id);
            values.put(DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_MILESTONE_ID, milestones.get(i - 1).getId());
            db.insert(DatabaseContract.ProjectMilestoneEntry.TABLE_NAME, "null", values);
        }
    }

    /**
     * Use this constructor when it is recovered from the database
     * @param id
     * @param name
     * @param dueDate
     * @param context
     * @throws ParseException
     */
    public Project(long id, String name, String dueDate, Context context) throws ParseException{
        if (name == null || dueDate == null || context == null) {
            throw new IllegalArgumentException();
        }

        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        Calendar dueDateCalendar = Util.stringToCalendar(dueDate);

        this.id = id;
        this.name = name;
        this.dueDate = dueDateCalendar;
        this.milestones = new ArrayList<>();

        getMilestones(this.id, this.milestones, context);
        sortMilestones();
    }

    /**
     * Sort the milestones base on due date ascending
     */
    public void sortMilestones() {
        Collections.sort(milestones);
    }

    /**
     * Get milestones from the database and store it in the milestones
     * @param id Project ID
     * @param milestones A list where you want to store the milestones
     * @param context
     * @throws ParseException
     */
    private void getMilestones(long id, List<Milestone> milestones, Context context) throws ParseException{
        List<Long> milestoneIds = new ArrayList<>();
        milestones.clear();

        String[] projection = {DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_MILESTONE_ID};
        String selection = DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_PROJECT_ID + " = " + this.id;
        Cursor cursor = db.query(DatabaseContract.ProjectMilestoneEntry.TABLE_NAME, projection, selection, null, null, null, null);
        if (!cursor.moveToFirst()) {
            return;
        }

        do {
            milestoneIds.add(cursor.getLong(cursor.getColumnIndex(DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_MILESTONE_ID)));
        } while (cursor.moveToNext());

        String[] milestoneProject = {
                DatabaseContract.MilestoneEntry._ID,
                DatabaseContract.MilestoneEntry.COLUMN_NAME_NAME,
                DatabaseContract.MilestoneEntry.COLUMN_NAME_DUE_DATE,
                DatabaseContract.MilestoneEntry.COLUMN_NAME_COMPLETED};

        StringBuilder selectionStringBuilder = new StringBuilder(DatabaseContract.MilestoneEntry._ID + " in (");
        for (int i = 0; i < milestoneIds.size(); i++) {
            if (i == 0) {
                selectionStringBuilder.append(milestoneIds.get(i));
            }
            else {
                selectionStringBuilder.append(", " + milestoneIds.get(i));
            }
        }
        selectionStringBuilder.append(")");

        cursor = db.query(DatabaseContract.MilestoneEntry.TABLE_NAME, milestoneProject, selectionStringBuilder.toString(), null, null, null, null);
        cursor.moveToFirst();

        do {
            milestones.add(new Milestone(
                    cursor.getLong(cursor.getColumnIndex(DatabaseContract.MilestoneEntry._ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.MilestoneEntry.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndex(DatabaseContract.MilestoneEntry.COLUMN_NAME_DUE_DATE)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseContract.MilestoneEntry.COLUMN_NAME_COMPLETED)) == 1,
                    context));
        } while (cursor.moveToNext());
    }

    public void dispose() {
        if (changeListener != null) {
            changeListener.onDispose(new DisposeEvent(id));
        }
    }

    public void addPropertyChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
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
        if (changeListener != null) {
            changeListener.onPropertyChange(new PropertyChangeEvent(
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
        if (changeListener != null) {
            changeListener.onPropertyChange(new PropertyChangeEvent(
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
    }

    public long addMilestone(String name, Calendar dueDate, Context context) {
        Milestone newMilestone = new Milestone(name, dueDate, context);
        milestones.add(newMilestone);
        sortMilestones();
        return newMilestone.getId();
    }

    public Milestone getCurrentMilestone() {
        return currentMilestone;
    }

    public void updateCurrentMilestone() {

    }
}
