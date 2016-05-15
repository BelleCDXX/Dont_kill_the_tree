package dontkillthetree.scu.edu.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.database.DatabaseHelper;

/**
 * Created by Joey Zheng on 5/14/16.
 */
public class Project {
    private final long id;
    private String name;
    private Calendar dueDate;
    private List<Milestone> milestones;
    private Milestone currentMilestone;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

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
        int increment = (int) (dueDate.getTimeInMillis() - currentDate.getTimeInMillis()) / (24 * 60 * 60 * 1000 * numberOfMilestones);

        this.name = name;
        this.dueDate = (Calendar) dueDate.clone();

        // create milestones
        int i;
        for (i = 1; i <= numberOfMilestones; i++) {
            milestones.add(new Milestone("Milestone " + i, currentDate, context));
            currentDate.add(Calendar.DAY_OF_MONTH, increment);
        }
        this.currentMilestone = milestones.get(0);

        // get project id
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ProjectEntry.COLUMN_NAME_NAME, name);
        values.put(DatabaseContract.ProjectEntry.COLUMN_NAME_DUE_DATE, Util.calendarToString(dueDate));
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

    }
}
