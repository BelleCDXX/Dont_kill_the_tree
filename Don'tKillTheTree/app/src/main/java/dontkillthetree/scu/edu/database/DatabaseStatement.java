package dontkillthetree.scu.edu.database;

import java.util.Calendar;

/**
 * Created by Joey Zheng on 5/14/16.
 */
public final class DatabaseStatement {
    private static final String PRIMARY_KEY = " PRIMARY KEY AUTOINCREMENT";
    private static final String FOREIGN_KEY = "FOREIGN KEY(";
    private static final String REFERENCES = " REFERENCES ";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String NULL_TYPE = " NULL";
    private static final String BLOB_TYPE = " BLOB";
    // SQLite does provide boolean values, use integer instead, 0 (false), 1 (true)
    private static final String COMMA_SEP = ", ";

    public static abstract class MilestoneEntry {
        protected static final String CREATE_ENTRY =
                "CREATE TABLE " + DatabaseContract.MilestoneEntry.TABLE_NAME + " (" +
                DatabaseContract.MilestoneEntry._ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                DatabaseContract.MilestoneEntry.COLUMN_NAME_NAME + BLOB_TYPE + COMMA_SEP +
                DatabaseContract.MilestoneEntry.COLUMN_NAME_DUE_DATE + TEXT_TYPE + COMMA_SEP +
                DatabaseContract.MilestoneEntry.COLUMN_NAME_COMPLETED + INTEGER_TYPE +
                ")";

        protected static final String DELETE_ENTRY =
                "DROP TABLE IF EXISTS " + DatabaseContract.MilestoneEntry.TABLE_NAME;
    }

    public static abstract class ProjectEntry {
        protected static final String CREATE_ENTRY =
                "CREATE TABLE " + DatabaseContract.ProjectEntry.TABLE_NAME + " (" +
                DatabaseContract.ProjectEntry._ID + INTEGER_TYPE + PRIMARY_KEY + COMMA_SEP +
                DatabaseContract.ProjectEntry.COLUMN_NAME_NAME + BLOB_TYPE + COMMA_SEP +
                DatabaseContract.ProjectEntry.COLUMN_NAME_DUE_DATE + TEXT_TYPE + COMMA_SEP +
                DatabaseContract.ProjectEntry.COLUMN_NAME_CURRENT_MILESTONE_ID + INTEGER_TYPE + COMMA_SEP +
                FOREIGN_KEY + DatabaseContract.ProjectEntry.COLUMN_NAME_CURRENT_MILESTONE_ID +
                        ")" + REFERENCES + DatabaseContract.MilestoneEntry.TABLE_NAME + "(" + DatabaseContract.MilestoneEntry._ID + ")" +
                ")";

        protected static final String DELETE_ENTRY =
                "DROP TABLE IF EXISTS " + DatabaseContract.ProjectEntry.TABLE_NAME;
    }

    public static abstract class ProjectMilestoneEntry {
        protected static final String CREATE_ENTRY =
                "CREATE TABLE " + DatabaseContract.ProjectMilestoneEntry.TABLE_NAME + " (" +
                DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_PROJECT_ID + INTEGER_TYPE + COMMA_SEP +
                DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_MILESTONE_ID + INTEGER_TYPE + COMMA_SEP +
                FOREIGN_KEY + DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_PROJECT_ID + ")" +
                        REFERENCES + DatabaseContract.ProjectEntry.TABLE_NAME + "(" + DatabaseContract.ProjectEntry._ID + ")" + COMMA_SEP +
                FOREIGN_KEY + DatabaseContract.ProjectMilestoneEntry.COLUMN_NAME_MILESTONE_ID + ")" +
                        REFERENCES + DatabaseContract.MilestoneEntry.TABLE_NAME + "(" + DatabaseContract.MilestoneEntry._ID + ")" +
                ")";

        protected static final String DELETE_ENTRY =
                "DROP TABLE IF EXISTS" + DatabaseContract.ProjectMilestoneEntry.TABLE_NAME;
    }

    public static abstract class TreeEntry {
        protected static final String CREATE_ENTRTY =
                "CREATE TABLE " + DatabaseContract.TreeEntry.TABLE_NAME + " (" +
                DatabaseContract.TreeEntry.COLUMN_NAME_STAGE + INTEGER_TYPE + COMMA_SEP +
                DatabaseContract.TreeEntry.COLUMN_NAME_EXPERIENCE + INTEGER_TYPE +
                ")";

        protected static final String DELETE_ENTRY =
                "DROP TABLE IF EXISTS" + DatabaseContract.TreeEntry.TABLE_NAME;
    }
}
