package dontkillthetree.scu.edu.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.database.DatabaseHelper;
import dontkillthetree.scu.edu.event.MyMilestoneDatabaseOpListener;
import dontkillthetree.scu.edu.event.MyProjectDatabaseOpListener;

public class Projects {
    public static List<Project> projects = null;

    public static void getAllProjects(Context context) throws ParseException{
        List<Project> result = new ArrayList();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String[] projection = {DatabaseContract.ProjectEntry._ID};
        Cursor cursor = db.query(DatabaseContract.ProjectEntry.TABLE_NAME, projection, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                result.add(new Project(
                        cursor.getLong(cursor.getColumnIndex(DatabaseContract.ProjectEntry._ID)),
                        new MyProjectDatabaseOpListener(context),
                        new MyMilestoneDatabaseOpListener(context)));
            }
            while (cursor.moveToNext());
        }

        projects = result;
//         return result;
    }
}
