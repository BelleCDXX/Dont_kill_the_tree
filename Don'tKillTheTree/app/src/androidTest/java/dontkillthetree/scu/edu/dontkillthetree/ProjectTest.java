package dontkillthetree.scu.edu.dontkillthetree;

import android.content.Context;
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
import dontkillthetree.scu.edu.database.DatabaseHelper;
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
    }

    @Test
    public void Test_ProjectCreationAndRecovery() {
        Calendar dueDate = Calendar.getInstance();
        dueDate.add(Calendar.DAY_OF_MONTH, 1);
        Project project = new Project("Project1", dueDate, 2, context);

        Project project2 = null;
        try {
            project2 = new Project(project.getId(), project.getName(), Util.calendarToString(project.getDueDate()), context);
        }
        catch(ParseException e) {
            fail(e.getMessage());
        }

        List<Milestone> milestones = project2.getMilestones();
        assertEquals(2, milestones.size());

        for (int i = 1; i <= milestones.size(); i++) {
            assertEquals("Milestone " + i, milestones.get(i - 1).getName());
        }
    }

    @Test
    public void Test_ProjectEdit() {
        Calendar dueDate = Calendar.getInstance();
        dueDate.add(Calendar.DAY_OF_MONTH, 1);
        Project project = new Project("Project1", dueDate, 2, context);

        List<Milestone> milestones = project.getMilestones();
        assertEquals(2, milestones.size());

        project.addMilestone("Milestone 3", dueDate, context);
        milestones = project.getMilestones();
        assertEquals(3, milestones.size());

        for (int i = 1; i <= milestones.size(); i++) {
            assertEquals("Milestone " + i, milestones.get(i - 1).getName());
        }
    }
}
