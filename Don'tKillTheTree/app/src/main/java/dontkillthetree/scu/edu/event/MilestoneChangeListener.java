package dontkillthetree.scu.edu.event;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.database.DatabaseHelper;

public class MilestoneChangeListener implements ChangeListener {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    public MilestoneChangeListener(Context context) {
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
    }

    public void onPropertyChange(PropertyChangeEvent event) {
        ContentValues values = new ContentValues();
        if (event.getPropertyName().equals(DatabaseContract.MilestoneEntry.COLUMN_NAME_COMPLETED)) {
            values.put(event.getPropertyName(), event.getValue().equals("true") ? 1 : 0);
        }
        else {
            values.put(event.getPropertyName(), event.getValue());
        }

        String selection = DatabaseContract.MilestoneEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(event.getId())};
        db.update(DatabaseContract.MilestoneEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public void onDispose(DisposeEvent event) {
        String milestoneToDelete = DatabaseContract.MilestoneEntry._ID + " = " + event.getId();
        String projectMilestoneToDelete = DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_MILESTONE_ID + " = " + event.getId();
        db.delete(DatabaseContract.ProjectMilestoneEntry.TABLE_NAME, projectMilestoneToDelete, null);
        db.delete(DatabaseContract.MilestoneEntry.TABLE_NAME, milestoneToDelete, null);
        databaseHelper.close();
    }
}
