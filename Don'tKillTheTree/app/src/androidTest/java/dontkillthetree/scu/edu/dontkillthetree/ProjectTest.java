package dontkillthetree.scu.edu.dontkillthetree;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.database.DatabaseHelper;
import dontkillthetree.scu.edu.model.Milestone;
import dontkillthetree.scu.edu.model.Project;

/**
 * Created by Joey Zheng on 5/15/16.
 */
public class ProjectTest extends AndroidTestCase{
    RenamingDelegatingContext context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        context = new RenamingDelegatingContext(getContext(), "test_");
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

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
}
