package dontkillthetree.scu.edu.dontkillthetree;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import java.util.Calendar;
import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.database.DatabaseHelper;
import dontkillthetree.scu.edu.model.Milestone;

/**
 * Created by Joey Zheng on 5/14/16.
 */
public class MilestoneTest extends AndroidTestCase{
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

    public void milestoneCreation(){
        Calendar calendar = Calendar.getInstance();
        Milestone milestone = new Milestone("Test Milestone", calendar, context);
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        Milestone milestone2 = new Milestone("Test Milestone 2", calendar, context);

        String[] projection = {DatabaseContract.MilestoneEntry._ID};
        String selection = DatabaseContract.MilestoneEntry._ID + " in (" + milestone.getId() + ", " + milestone2.getId() + ")";
        Cursor cursor = db.query(DatabaseContract.MilestoneEntry.TABLE_NAME, projection, selection, null, null, null, null);
        assertEquals(2, cursor.getCount());
        assertEquals(false, milestone.isOnTime());
        assertEquals(true, milestone2.isOnTime());
    }
}
