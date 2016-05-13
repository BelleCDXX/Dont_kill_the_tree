package dontkillthetree.scu.edu.dontkillthetree;

import android.provider.BaseColumns;

/**
 * Created by jasonzhang on 5/13/16.
 */
public class DatabaseContract {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Database.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    // SQLite does provide boolean values, use integer instead, 0 (false), 1 (true)

    private static final String COMMA_SEP = ", ";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private DatabaseContract() {}

    public static abstract class Table implements BaseColumns {

        // schema for Project table
        public static final String TABLE_NAME_PROJECT = "Project";
        public static final String COLUMN_NAME_PROJECT_NAME = "projectName";
        public static final String COLUMN_NAME_NUMBER_OF_MILESTONE = "numberOfMilestone";
        public static final String COLUMN_NAME_DUE_DATE_OF_PROJECT = "dueDateOfProject";
        public static final String COLUMN_NAME_PHONE_NUMBER_OF_MONITOR = "phoneNumberOfMonitor";
        public static final String COLUMN_NAME_ID_OF_CURRENT_MILESTONE = "idOfCurrentMilestone";

        public static final String CREATE_PROJECT_TABLE = "CREATE TABLE " + TABLE_NAME_PROJECT + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                COLUMN_NAME_PROJECT_NAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_NUMBER_OF_MILESTONE + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_DUE_DATE_OF_PROJECT + REAL_TYPE + COMMA_SEP +
                COLUMN_NAME_PHONE_NUMBER_OF_MONITOR + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_ID_OF_CURRENT_MILESTONE + INTEGER_TYPE + " );";

        public static final String DELETE_PROJECT_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME_PROJECT;

        // schema for Milestone Table
        public static final String TABLE_NAME_MILESTONE = "Milestone";
        public static final String COLUMN_NAME_MILESTONE_NAME = "milestoneName";
        public static final String COLUMN_NAME_DUE_DATE_OF_MILESTONE = "dueDateOfMilestone";
        public static final String COLUMN_NAME_ISONTIME = "isOnTime";

        public static final String CREATE_MILESTONE_TABLE = "CREATE TABLE " + TABLE_NAME_MILESTONE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                COLUMN_NAME_MILESTONE_NAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_DUE_DATE_OF_MILESTONE + REAL_TYPE + COMMA_SEP +
                COLUMN_NAME_ISONTIME + INTEGER_TYPE + " );";

        public static final String DELETE_MILESTONE_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME_MILESTONE;

        // schema for Project_Milestone table
        public static final String TABLE_NAME_PROJECT_MILESTONE = "ProjectMilestone";
        public static final String COLUMN_NAME_ID_OF_PROJECT = "idOfProject";
        public static final String COLUMN_NAME_ID_OF_MILESTONE = "idOfMilestone";

        public static final String CREATE_PROJECT_MILESTONE_TABLE = "CREATE TABLE " + TABLE_NAME_PROJECT_MILESTONE + " (" +
                COLUMN_NAME_ID_OF_PROJECT + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_ID_OF_MILESTONE + INTEGER_TYPE + " );";

        public static final String DELETE_PROJECT_MILESTONE_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME_PROJECT_MILESTONE;

        // schema for Stage table
        // for version 1, Stage table record the stage of tree
        public static final String TABLE_NAME_STAGE = "Stage";
        public static final String COLUMN_NAME_ID_OF_STAGE = "idOfStage";
        public static final String COLUMN_NAME_NAME_OF_STAGE = "nameOfStage";

        public static final String CREATE_STAGE_TABLE = "CREATE TABLE " + TABLE_NAME_STAGE + " (" +
                COLUMN_NAME_ID_OF_STAGE + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_NAME_OF_STAGE + TEXT_TYPE + " );";

        public static final String DELETE_STAGE_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME_STAGE;

        // schema for Progress bar table
        //
    }
}
