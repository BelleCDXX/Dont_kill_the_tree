package dontkillthetree.scu.edu.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;

import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.database.DatabaseHelper;
import dontkillthetree.scu.edu.event.MyMilestoneDatabaseOpListener;
import dontkillthetree.scu.edu.event.MyProjectDatabaseOpListener;
import dontkillthetree.scu.edu.model.Project;

/**
 * Created by xcw0420 on 5/29/16.
 */
public class GetNextMilestoneForNotif {
    private Context context;
    private long projectID;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Project project;


    public GetNextMilestoneForNotif(Context context, long projectID){
        this.context = context;
        this.projectID = projectID;
    }

    public void getNextMilestone() throws ParseException {
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        project = new Project(projectID, new MyProjectDatabaseOpListener(context), new MyMilestoneDatabaseOpListener(context));
        String[] projection = {DatabaseContract.ProjectEntry.COLUMN_NAME_IS_ON_TIME};
        String selection = DatabaseContract.ProjectEntry._ID + " = " + project.getId();
    }
}
