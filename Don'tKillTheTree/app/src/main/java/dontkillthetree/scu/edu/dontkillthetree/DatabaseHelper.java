package dontkillthetree.scu.edu.dontkillthetree;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/**
 * Created by jasonzhang on 5/13/16.
 */
public class DatabaseHelper {
    Context context;

    public DatabaseHelper(Context context) {
//        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
//        this.context = context;
    }

//    @Override
//    public void onCreate(SQLiteDatabase db) {
////        db.execSQL(DatabaseContract.Table.CREATE_TABLE);
//        toastShow("Creating database...");
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
////        db.execSQL(DatabaseContract.Table.DELETE_TABLE);
////        db.execSQL(DatabaseContract.Table.CREATE_TABLE);
//        toastShow("Upgrading Database...");
//    }


    private void toastShow(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
