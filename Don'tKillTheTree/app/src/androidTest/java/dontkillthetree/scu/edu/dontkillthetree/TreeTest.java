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

import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.database.DatabaseHelper;
import dontkillthetree.scu.edu.model.Tree;

import static org.junit.Assert.*;

/**
 * Created by Joey Zheng on 5/24/16.
 */

@RunWith(AndroidJUnit4.class)
public class TreeTest {
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
    public void Test_TreeCreation() {
        Tree tree = Tree.getInstance(context);

        String[] projection = {DatabaseContract.TreeEntry.COLUMN_NAME_STAGE, DatabaseContract.TreeEntry.COLUMN_NAME_EXPERIENCE};
        Cursor cursor = db.query(DatabaseContract.TreeEntry.TABLE_NAME, projection, null, null, null, null, null);

        assertEquals(1, cursor.getCount());
        assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.TreeEntry.COLUMN_NAME_STAGE)));
        assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.TreeEntry.COLUMN_NAME_EXPERIENCE)));
        assertEquals(0, tree.getCurrentStage());
        assertEquals(0, tree.getExperience());

        db.delete(DatabaseContract.TreeEntry.TABLE_NAME, null, null);
    }

    @Test
    public void Test_TreeEdit() {
        Tree tree = Tree.getInstance(context);
        tree.increaseExperience(150);

        String[] projection = {DatabaseContract.TreeEntry.COLUMN_NAME_STAGE, DatabaseContract.TreeEntry.COLUMN_NAME_EXPERIENCE};
        Cursor cursor = db.query(DatabaseContract.TreeEntry.TABLE_NAME, projection, null, null, null, null, null);
        assertEquals(1, cursor.getInt(cursor.getColumnIndex(DatabaseContract.TreeEntry.COLUMN_NAME_STAGE)));
        assertEquals(150, cursor.getInt(cursor.getColumnIndex(DatabaseContract.TreeEntry.COLUMN_NAME_EXPERIENCE)));

        tree.decreaseExperience(50);
        cursor = db.query(DatabaseContract.TreeEntry.TABLE_NAME, projection, null, null, null, null, null);
        assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.TreeEntry.COLUMN_NAME_STAGE)));
        assertEquals(100, cursor.getInt(cursor.getColumnIndex(DatabaseContract.TreeEntry.COLUMN_NAME_EXPERIENCE)));

        tree.increaseExperience(601);
        cursor = db.query(DatabaseContract.TreeEntry.TABLE_NAME, projection, null, null, null, null, null);
        assertEquals(6, cursor.getInt(cursor.getColumnIndex(DatabaseContract.TreeEntry.COLUMN_NAME_STAGE)));
        assertEquals(700, cursor.getInt(cursor.getColumnIndex(DatabaseContract.TreeEntry.COLUMN_NAME_EXPERIENCE)));

        tree.decreaseExperience(800);
        cursor = db.query(DatabaseContract.TreeEntry.TABLE_NAME, projection, null, null, null, null, null);
        assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.TreeEntry.COLUMN_NAME_STAGE)));
        assertEquals(0, cursor.getInt(cursor.getColumnIndex(DatabaseContract.TreeEntry.COLUMN_NAME_EXPERIENCE)));
    }
}
