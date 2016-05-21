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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dontkillthetree.scu.edu.database.DatabaseHelper;
import dontkillthetree.scu.edu.event.MyMilestoneDatabaseOpListener;
import dontkillthetree.scu.edu.event.MyProjectDatabaseOpListener;
import dontkillthetree.scu.edu.model.Project;
import dontkillthetree.scu.edu.model.Projects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Joey Zheng on 5/20/16.
 */
@RunWith(AndroidJUnit4.class)
public class ProjectsTest {
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
    public void Test_GetProjects() {
        Calendar dueDate = Calendar.getInstance();
        dueDate.add(Calendar.DAY_OF_MONTH, 1);
        Project project = new Project(
                "Project1",
                dueDate,
                2,
                new MyProjectDatabaseOpListener(context),
                new MyMilestoneDatabaseOpListener(context), context);

        List<Project> projects = new ArrayList<>();

        try {
            projects = Projects.getAllProjects(context);
        }
        catch(ParseException ex) {
            fail(ex.getMessage());
        }

        boolean found = false;
        for (Project temp : projects) {
            if (temp.getId() == project.getId()) {
                found = true;
            }
        }

        assertEquals(true, found);
    }
}
