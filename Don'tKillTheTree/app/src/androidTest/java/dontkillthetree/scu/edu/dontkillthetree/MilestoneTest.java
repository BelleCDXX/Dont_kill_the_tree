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

import java.util.Calendar;

import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.database.DatabaseHelper;
import dontkillthetree.scu.edu.event.MyMilestoneDatabaseOpListener;
import dontkillthetree.scu.edu.model.Milestone;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MilestoneTest{
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
    public void Test_MilstoneCreation() {
        Calendar calendar = Calendar.getInstance();
        Milestone milestone = new Milestone("Test Milestone", calendar, new MyMilestoneDatabaseOpListener(context), context);

        String[] projection = {DatabaseContract.MilestoneEntry._ID};
        String selection = DatabaseContract.MilestoneEntry._ID + " = " + milestone.getId();
        Cursor cursor = db.query(DatabaseContract.MilestoneEntry.TABLE_NAME, projection, selection, null, null, null, null);
        assertEquals(1, cursor.getCount());

        milestone.dispose();
    }

    @Test
    public void Test_MilestoneEdit(){
        // set up
        Calendar calendar = Calendar.getInstance();
        Milestone milestone = new Milestone("Test Milestone", calendar, new MyMilestoneDatabaseOpListener(context), context);
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        Milestone milestone2 = new Milestone("Test Milestone 2", calendar, new MyMilestoneDatabaseOpListener(context), context);

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
        projection[0] = DatabaseContract.MilestoneEntry.COLUMN_NAME_COMPLETED;
        selection = DatabaseContract.MilestoneEntry._ID + " = " + milestone2.getId();
        cursor = db.query(DatabaseContract.MilestoneEntry.TABLE_NAME, projection, selection, null, null, null, null);
        cursor.moveToFirst();
        assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.MilestoneEntry.COLUMN_NAME_COMPLETED)));

        milestone2.setCompleted(true);
        projection[0] = DatabaseContract.MilestoneEntry.COLUMN_NAME_COMPLETED;
        selection = DatabaseContract.MilestoneEntry._ID + " = " + milestone2.getId();
        cursor = db.query(DatabaseContract.MilestoneEntry.TABLE_NAME, projection, selection, null, null, null, null);
        cursor.moveToFirst();
        assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.MilestoneEntry.COLUMN_NAME_COMPLETED)));

        milestone.dispose();
        milestone2.dispose();
    }

    @Test
    public void Test_MilestoneDeletion() {
        Calendar calendar = Calendar.getInstance();
        Milestone milestone = new Milestone("Test Milestone", calendar, new MyMilestoneDatabaseOpListener(context), context);

        long id = milestone.getId();
        milestone.dispose();

        String[] projection = {DatabaseContract.MilestoneEntry._ID};
        String selection = DatabaseContract.MilestoneEntry._ID + " = " + id;
        Cursor cursor = db.query(DatabaseContract.MilestoneEntry.TABLE_NAME, projection, selection, null, null, null, null);
        assertEquals(0, cursor.getCount());
    }
}
