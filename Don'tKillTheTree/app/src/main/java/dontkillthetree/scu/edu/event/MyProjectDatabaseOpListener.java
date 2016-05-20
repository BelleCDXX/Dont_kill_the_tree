package dontkillthetree.scu.edu.event;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.database.DatabaseHelper;
import dontkillthetree.scu.edu.model.Milestone;
import dontkillthetree.scu.edu.model.Project;

public class MyProjectDatabaseOpListener implements ProjectDatabaseOpListener {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Context context;

    public MyProjectDatabaseOpListener(Context context) {
        this.context = context;
    }

    public long onInsert(String name, Calendar dueDate, List<Milestone> milestones) {
        long id;
        databaseHelper = new DatabaseHelper(this.context);
        db = databaseHelper.getWritableDatabase();

        // create Project entry
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ProjectEntry.COLUMN_NAME_NAME, name);
        values.put(DatabaseContract.ProjectEntry.COLUMN_NAME_DUE_DATE, Util.calendarToString(dueDate));
        values.put(DatabaseContract.ProjectEntry.COLUMN_NAME_CURRENT_MILESTONE_ID, milestones.get(0).getId());
        id = db.insert(DatabaseContract.ProjectEntry.TABLE_NAME, "null", values);

        // create ProjectMilestone entry
        int i;
        for (i = 1; i <= milestones.size(); i++) {
            values = new ContentValues();
            values.put(DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_PROJECT_ID, id);
            values.put(DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_MILESTONE_ID, milestones.get(i - 1).getId());
            db.insert(DatabaseContract.ProjectMilestoneEntry.TABLE_NAME, "null", values);
        }

        db.close();
        databaseHelper.close();

        return id;
    }

    /**
     * Get milestones from the database and store it in the milestones
     * @param id Project ID
     * @param milestones A list where you want to store the milestones
     * @throws ParseException
     */
    public void getMilestones(long id, List<Milestone> milestones, MilestoneDatabaseOpListener milestoneDatabaseOpListener) throws ParseException{
        List<Long> milestoneIds = new ArrayList<>();
        milestones.clear();
        databaseHelper = new DatabaseHelper(this.context);
        db = databaseHelper.getWritableDatabase();

        String[] projection = {DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_MILESTONE_ID};
        String selection = DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_PROJECT_ID + " = " + id;
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
                    milestoneDatabaseOpListener,
                    context));
        } while (cursor.moveToNext());

        db.close();
        databaseHelper.close();
    }

    public void onUpdate(PropertyChangeEvent event) {
        databaseHelper = new DatabaseHelper(this.context);
        db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(event.getPropertyName(), event.getValue());
        String selection = DatabaseContract.ProjectEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(event.getId())};
        db.update(DatabaseContract.ProjectEntry.TABLE_NAME, values, selection, selectionArgs);

        db.close();
        databaseHelper.close();
    }

    public void onDelete(DisposeEvent event) {
        List<Long> milestoneIds = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this.context);
        db = databaseHelper.getWritableDatabase();

        String[] projection = {DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_MILESTONE_ID};
        String selection = DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_PROJECT_ID + " = " + event.getId();
        Cursor cursor = db.query(DatabaseContract.ProjectMilestoneEntry.TABLE_NAME, projection, selection, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                milestoneIds.add(cursor.getLong(cursor.getColumnIndex(DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_MILESTONE_ID)));
            } while (cursor.moveToNext());
        }

        StringBuilder selectionStringBuilder = new StringBuilder("(");
        for (int i = 0; i < milestoneIds.size(); i++) {
            if (i == 0) {
                selectionStringBuilder.append(milestoneIds.get(i));
            }
            else {
                selectionStringBuilder.append(", " + milestoneIds.get(i));
            }
        }
        selectionStringBuilder.append(")");
        String deleteCondition = selectionStringBuilder.toString();

        String projectToDelete = DatabaseContract.ProjectEntry._ID + " = " + event.getId();
        String milestoneToDelete = DatabaseContract.MilestoneEntry._ID + " IN " + deleteCondition;
        String projectMilestoneToDelete = DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_MILESTONE_ID + " IN " + deleteCondition;
        db.delete(DatabaseContract.ProjectMilestoneEntry.TABLE_NAME, projectMilestoneToDelete, null);
        db.delete(DatabaseContract.MilestoneEntry.TABLE_NAME, milestoneToDelete, null);
        db.delete(DatabaseContract.ProjectEntry.TABLE_NAME, projectToDelete, null);

        db.close();
        databaseHelper.close();
    }
}