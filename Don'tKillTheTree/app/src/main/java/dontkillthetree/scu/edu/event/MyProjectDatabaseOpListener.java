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

    public String[] onSelect(long id) {
        String[] result = new String[4];
        databaseHelper = new DatabaseHelper(this.context);
        db = databaseHelper.getWritableDatabase();

        String[] projection = {
                DatabaseContract.ProjectEntry.COLUMN_NAME_NAME,
                DatabaseContract.ProjectEntry.COLUMN_NAME_DUE_DATE,
                DatabaseContract.ProjectEntry.COLUMN_NAME_GUARDIAN_NAME,
                DatabaseContract.ProjectEntry.COLUMN_NAME_GUARDIAN_PHONE
        };
        String selection = DatabaseContract.ProjectEntry._ID + " = " + id;
        Cursor cursor = db.query(DatabaseContract.ProjectEntry.TABLE_NAME, projection, selection, null, null, null, null);
        cursor.moveToFirst();
        result[0] = cursor.getString(cursor.getColumnIndex(DatabaseContract.ProjectEntry.COLUMN_NAME_NAME));
        result[1] = cursor.getString(cursor.getColumnIndex(DatabaseContract.ProjectEntry.COLUMN_NAME_DUE_DATE));
        result[2] = cursor.isNull(cursor.getColumnIndex(DatabaseContract.ProjectEntry.COLUMN_NAME_GUARDIAN_NAME)) ?
                null : cursor.getString(cursor.getColumnIndex(DatabaseContract.ProjectEntry.COLUMN_NAME_GUARDIAN_NAME));
        result[3] = cursor.isNull(cursor.getColumnIndex(DatabaseContract.ProjectEntry.COLUMN_NAME_GUARDIAN_PHONE)) ?
                null : cursor.getString(cursor.getColumnIndex(DatabaseContract.ProjectEntry.COLUMN_NAME_GUARDIAN_PHONE));

        cursor.close();
        db.close();
        databaseHelper.close();

        return result;
    }

    public long onInsert(String name, Calendar dueDate, String guardianName, String guardianPhone, List<Milestone> milestones) {
        long id;
        databaseHelper = new DatabaseHelper(this.context);
        db = databaseHelper.getWritableDatabase();

        // create Project entry
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ProjectEntry.COLUMN_NAME_NAME, name);
        values.put(DatabaseContract.ProjectEntry.COLUMN_NAME_DUE_DATE, Util.calendarToString(dueDate));
        values.put(DatabaseContract.ProjectEntry.COLUMN_NAME_IS_ON_TIME, true);
        values.put(DatabaseContract.ProjectEntry.COLUMN_NAME_CURRENT_MILESTONE_ID, milestones.get(0).getId());
        if (guardianName == null) {
            values.putNull(DatabaseContract.ProjectEntry.COLUMN_NAME_GUARDIAN_NAME);
        }
        else {
            values.put(DatabaseContract.ProjectEntry.COLUMN_NAME_GUARDIAN_NAME, guardianName);
        }
        if (guardianPhone == null) {
            values.putNull(DatabaseContract.ProjectEntry.COLUMN_NAME_GUARDIAN_PHONE);
        }
        else {
            values.put(DatabaseContract.ProjectEntry.COLUMN_NAME_GUARDIAN_PHONE, guardianPhone);
        }
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
        cursor.close();

        String[] milestoneProject = {
                DatabaseContract.MilestoneEntry._ID};

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
                    milestoneDatabaseOpListener));
        } while (cursor.moveToNext());

        cursor.close();
        db.close();
        databaseHelper.close();
    }

    public void onUpdate(PropertyChangeEvent event) {
        databaseHelper = new DatabaseHelper(this.context);
        db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        if (event.getPropertyName().equals(DatabaseContract.ProjectEntry.COLUMN_NAME_IS_ON_TIME)) {
            values.put(event.getPropertyName(), event.getValue().equals("true") ? 1 : 0);
        }
        else {
            values.put(event.getPropertyName(), event.getValue());
        }

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
        cursor.close();

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
