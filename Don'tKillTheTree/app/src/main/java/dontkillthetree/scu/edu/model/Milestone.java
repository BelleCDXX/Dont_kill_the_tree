package dontkillthetree.scu.edu.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.Calendar;

import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.database.DatabaseHelper;
import dontkillthetree.scu.edu.event.DisposeEvent;
import dontkillthetree.scu.edu.event.PropertyChangeEvent;
import dontkillthetree.scu.edu.event.ChangeListener;

/**
 * Created by Joey Zheng on 5/14/16.
 */
public class Milestone implements Comparable{
    private final long id;
    private String name;
    private Calendar dueDate;
    private boolean onTime;
    private boolean completed;
    private boolean shown;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private ChangeListener changeListener;

    /**
     * Use this constructor when user creates a new milestone, i.e. no record in the database
     * @param name
     * @param dueDate
     */
    public Milestone(String name, Calendar dueDate, Context context) {
        Calendar currentDate = Calendar.getInstance();
        if (name == null || context == null || dueDate.before(currentDate)) {
            throw new IllegalArgumentException();
        }

        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        this.name = name;
        this.dueDate = (Calendar) dueDate.clone();
        Util.toNearestDueDate(this.dueDate);
        this.onTime = Util.isOnTime(dueDate, currentDate);
        this.completed = false;
        this.shown = false;

        // insert data into the database and retrieve id
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MilestoneEntry.COLUMN_NAME_NAME, name);
        values.put(DatabaseContract.MilestoneEntry.COLUMN_NAME_DUE_DATE, Util.calendarToString(this.dueDate));
        values.put(DatabaseContract.MilestoneEntry.COLUMN_NAME_COMPLETED, completed);
        this.id = db.insert(DatabaseContract.MilestoneEntry.TABLE_NAME, "null", values);
    }

    /**
     * Use this constructor when it is recovered from the database
     * @param id
     * @param name
     * @param dueDate
     */
    public Milestone(long id, String name, String dueDate, boolean completed, Context context) throws ParseException{
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        Calendar dueDateCalendar = Util.stringToCalendar(dueDate);

        this.id = id;
        this.name = name;
        this.dueDate = dueDateCalendar;
        this.completed = completed;
        this.onTime = Util.isOnTime(dueDateCalendar, null) || this.completed;
        this.shown = false;
    }

    public void dispose() {
        if (changeListener != null) {
            changeListener.onDispose(new DisposeEvent(id));
        }
    }

    public int compareTo(Object milestone) {
        if (! (milestone instanceof Milestone)) {
            throw new IllegalArgumentException();
        }

        return dueDate.compareTo(((Milestone) milestone).getDueDate());
    }

    public void addPropertyChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    // getters and setters
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
                    DatabaseContract.MilestoneEntry.COLUMN_NAME_NAME,
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
        this.onTime = Util.isOnTime(this.dueDate, null) || this.completed;

        // update database
        if (changeListener != null) {
            changeListener.onPropertyChange(new PropertyChangeEvent(
                    id,
                    DatabaseContract.MilestoneEntry.COLUMN_NAME_DUE_DATE,
                    Util.calendarToString(this.dueDate)));
        }
    }

    public boolean isOnTime() {
        return onTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        // update database
        if (changeListener != null) {
            changeListener.onPropertyChange(new PropertyChangeEvent(
                    id,
                    DatabaseContract.MilestoneEntry.COLUMN_NAME_COMPLETED,
                    String.valueOf(completed)));
        }

        // update instance
        this.completed = completed;
        this.onTime = Util.isOnTime(this.dueDate, null) || this.completed;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }
}
