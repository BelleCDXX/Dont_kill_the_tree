package dontkillthetree.scu.edu.dontkillthetree;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.database.DatabaseHelper;
import dontkillthetree.scu.edu.event.MyMilestoneDatabaseOpListener;
import dontkillthetree.scu.edu.event.MyProjectDatabaseOpListener;
import dontkillthetree.scu.edu.model.Milestone;
import dontkillthetree.scu.edu.model.Project;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ProjectTest{
    Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
    }

    @After
    public void tearDown() throws Exception {
        db.close();
        databaseHelper.close();
    }

    @Test
    public void Test_ProjectCreation() {
        Calendar dueDate = Calendar.getInstance();
        dueDate.add(Calendar.DAY_OF_MONTH, 1);
        Project project = new Project(
                "Project1",
                dueDate,
                2,
                new MyProjectDatabaseOpListener(context),
                new MyMilestoneDatabaseOpListener(context), context);

        String[] projection = {DatabaseContract.ProjectEntry._ID};
        String selection = DatabaseContract.ProjectEntry._ID + " = " + project.getId();
        Cursor cursor = db.query(DatabaseContract.ProjectEntry.TABLE_NAME, projection, selection, null, null, null, null);

        assertEquals(1, cursor.getCount());

        project.dispose();
    }

    @Test
    public void Test_ProjectEdit() {
        Calendar dueDate = Calendar.getInstance();
        dueDate.add(Calendar.DAY_OF_MONTH, 1);
        Project project = new Project(
                "Project1",
                dueDate,
                2,
                new MyProjectDatabaseOpListener(context),
                new MyMilestoneDatabaseOpListener(context), context);

        List<Milestone> milestones = project.getMilestones();
        assertEquals(2, milestones.size());

        project.addMilestone("Milestone 3", dueDate, context);
        milestones = project.getMilestones();
        assertEquals(3, milestones.size());

        for (int i = 1; i <= milestones.size(); i++) {
            assertEquals("Milestone " + i, milestones.get(i - 1).getName());
        }

        project.dispose();
    }

    @Test
    public void Test_ProjectDeletion() {
        Calendar dueDate = Calendar.getInstance();
        dueDate.add(Calendar.DAY_OF_MONTH, 1);
        Project project = new Project(
                "Project1",
                dueDate,
                2,
                new MyProjectDatabaseOpListener(context),
                new MyMilestoneDatabaseOpListener(context), context);

        long projectId = project.getId();
        long[] milestoneIds = new long[2];
        milestoneIds[0] = project.getMilestones().get(0).getId();
        milestoneIds[1] = project.getMilestones().get(1).getId();
        project.dispose();

        String[] projection = {DatabaseContract.ProjectEntry._ID};
        String selection = DatabaseContract.ProjectEntry._ID + " = " + projectId;
        Cursor cursor = db.query(DatabaseContract.ProjectEntry.TABLE_NAME, projection, selection, null, null, null, null);
        assertEquals(0, cursor.getCount());

        String[] projection2 = {DatabaseContract.MilestoneEntry._ID};
        selection = DatabaseContract.MilestoneEntry._ID + " in (" + milestoneIds[0] + ", " + milestoneIds[1] + ")";
        cursor = db.query(DatabaseContract.MilestoneEntry.TABLE_NAME, projection2, selection, null, null, null, null);
        assertEquals(0, cursor.getCount());
    }
}
