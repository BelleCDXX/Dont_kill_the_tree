package dontkillthetree.scu.edu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "DontKillTheTree";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseStatement.MilestoneEntry.CREATE_ENTRY);
        db.execSQL(DatabaseStatement.ProjectEntry.CREATE_ENTRY);
        db.execSQL(DatabaseStatement.ProjectMilestoneEntry.CREATE_ENTRY);
        db.execSQL(DatabaseStatement.TreeEntry.CREATE_ENTRTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseStatement.ProjectMilestoneEntry.DELETE_ENTRY);
        db.execSQL(DatabaseStatement.ProjectEntry.DELETE_ENTRY);
        db.execSQL(DatabaseStatement.MilestoneEntry.DELETE_ENTRY);
        db.execSQL(DatabaseStatement.MilestoneEntry.CREATE_ENTRY);
        db.execSQL(DatabaseStatement.ProjectEntry.CREATE_ENTRY);
        db.execSQL(DatabaseStatement.ProjectMilestoneEntry.CREATE_ENTRY);
        db.execSQL(DatabaseStatement.TreeEntry.DELETE_ENTRY);
        db.execSQL(DatabaseStatement.TreeEntry.CREATE_ENTRTY);
    }
}
