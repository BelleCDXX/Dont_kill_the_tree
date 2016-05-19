package dontkillthetree.scu.edu.event;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.database.DatabaseHelper;

public class ProjectChangeListener {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    public ProjectChangeListener(Context context) {
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
    }

    public void onPropertyChange(PropertyChangeEvent event) {
        ContentValues values = new ContentValues();
        values.put(event.getPropertyName(), event.getValue());
        String selection = DatabaseContract.ProjectEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(event.getId())};
        db.update(DatabaseContract.ProjectEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public void onDispose(DisposeEvent event) {
        List<Long> milestoneIds = new ArrayList<>();

        String[] projection = {DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_MILESTONE_ID};
        String selection = DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_PROJECT_ID + " = " + event.getId();
        Cursor cursor = db.query(DatabaseContract.ProjectMilestoneEntry.TABLE_NAME, projection, selection, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                milestoneIds.add(cursor.getLong(cursor.getColumnIndex(DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_MILESTONE_ID)));
            } while (cursor.moveToNext());
        }

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
        String deleteCondition = selectionStringBuilder.toString();

        String projectToDelete = DatabaseContract.ProjectEntry._ID + " = " + event.getId();
        String milestoneToDelete = DatabaseContract.MilestoneEntry._ID + " IN " + deleteCondition;
        String projectMilestoneToDelete = DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_MILESTONE_ID + " IN " + deleteCondition;
        db.delete(DatabaseContract.ProjectMilestoneEntry.TABLE_NAME, projectMilestoneToDelete, null);
        db.delete(DatabaseContract.MilestoneEntry.TABLE_NAME, milestoneToDelete, null);
        db.delete(DatabaseContract.ProjectEntry.TABLE_NAME, projectToDelete, null);
        databaseHelper.close();
    }
}
