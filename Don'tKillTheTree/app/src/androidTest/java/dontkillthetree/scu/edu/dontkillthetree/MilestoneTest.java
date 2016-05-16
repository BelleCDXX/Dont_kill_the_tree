package dontkillthetree.scu.edu.dontkillthetree;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import java.util.Calendar;
import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.database.DatabaseHelper;
import dontkillthetree.scu.edu.event.MilestoneChangeListener;
import dontkillthetree.scu.edu.event.ChangeListener;
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
        // set up
        Calendar calendar = Calendar.getInstance();
        Milestone milestone = new Milestone("Test Milestone", calendar, context);
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        Milestone milestone2 = new Milestone("Test Milestone 2", calendar, context);
        ChangeListener changeListener = new MilestoneChangeListener(context);
        milestone.addPropertyChangeListener(changeListener);
        milestone2.addPropertyChangeListener(changeListener);

        // isOnTime
        String[] projection = {DatabaseContract.MilestoneEntry._ID};
        String selection = DatabaseContract.MilestoneEntry._ID + " in (" + milestone.getId() + ", " + milestone2.getId() + ")";
        Cursor cursor = db.query(DatabaseContract.MilestoneEntry.TABLE_NAME, projection, selection, null, null, null, null);
        cursor.moveToFirst();
        assertEquals(2, cursor.getCount());
        assertEquals(false, milestone.isOnTime());
        assertEquals(true, milestone2.isOnTime());

        // name edit
        milestone.setName("Milestone New Name");
        projection[0] = DatabaseContract.MilestoneEntry.COLUMN_NAME_NAME;
        selection = DatabaseContract.MilestoneEntry._ID + " = " + milestone.getId();
        cursor = db.query(DatabaseContract.MilestoneEntry.TABLE_NAME, projection, selection, null, null, null, null);
        cursor.moveToFirst();
        assertEquals("Milestone New Name", cursor.getString(cursor.getColumnIndex(DatabaseContract.MilestoneEntry.COLUMN_NAME_NAME)));

        // completed
        milestone2.setCompleted(true);
        projection[0] = DatabaseContract.MilestoneEntry.COLUMN_NAME_COMPLETED;
        selection = DatabaseContract.MilestoneEntry._ID + " = " + milestone2.getId();
        cursor = db.query(DatabaseContract.MilestoneEntry.TABLE_NAME, projection, selection, null, null, null, null);
        cursor.moveToFirst();
        assertEquals(true, cursor.getString(cursor.getColumnIndex(DatabaseContract.MilestoneEntry.COLUMN_NAME_COMPLETED)));
    }
}
