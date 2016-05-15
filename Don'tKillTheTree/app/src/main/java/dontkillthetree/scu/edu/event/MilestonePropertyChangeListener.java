package dontkillthetree.scu.edu.event;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.database.DatabaseHelper;

/**
 * Created by Joey Zheng on 5/15/16.
 */
public class MilestonePropertyChangeListener implements PropertyChangeListener {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    public MilestonePropertyChangeListener(Context context) {
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
    }

    public void onPropertyChange(PropertyChangeEvent event) {
        ContentValues values = new ContentValues();
        values.put(event.getPropertyName(), String.valueOf(event.getValue()));
        String selection = DatabaseContract.MilestoneEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(event.getId())};
        db.update(DatabaseContract.MilestoneEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
