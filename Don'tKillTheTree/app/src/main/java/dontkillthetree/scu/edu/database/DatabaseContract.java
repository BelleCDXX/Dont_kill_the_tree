package dontkillthetree.scu.edu.database;

import android.provider.BaseColumns;

public final class DatabaseContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private DatabaseContract() {}

    public static abstract class ProjectEntry implements BaseColumns {
        public static final String TABLE_NAME = "Project";
        public static final String _ID = "ID";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_DUE_DATE = "DueDate";
        public static final String COLUMN_NAME_CURRENT_MILESTONE_ID = "CurrentMilestoneID";
    }

    public static abstract class MilestoneEntry implements BaseColumns {
        public static final String TABLE_NAME = "Milestone";
        public static final String _ID = "ID";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_DUE_DATE = "DueDate";
        public static final String COLUMN_NAME_COMPLETED = "Completed";
    }

    public static abstract class ProjectMilestoneEntry implements BaseColumns {
        public static final String TABLE_NAME = "ProjectMilestone";
        public static final String COLUMN_NAME_PROJECT_ID = "ProjectID";
        public static final String COLUMN_NAME_MILESTONE_ID = "MilestoneID";
    }

    public static abstract class TreeEntry implements BaseColumns {
        public static final String TABLE_NAME = "Tree";
        public static final String COLUMN_NAME_STAGE = "Stage";
        public static final String COLUMN_NAME_EXPERIENCE = "Experience";
    }
}
