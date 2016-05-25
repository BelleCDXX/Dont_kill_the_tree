package dontkillthetree.scu.edu.UI;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.text.ParseException;
import java.util.List;

import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.database.DatabaseHelper;
import dontkillthetree.scu.edu.event.MyMilestoneDatabaseOpListener;
import dontkillthetree.scu.edu.event.MyProjectDatabaseOpListener;
import dontkillthetree.scu.edu.model.Milestone;
import dontkillthetree.scu.edu.model.Project;

public class ProjectDetailActivity extends ParentActivity implements AdapterView.OnItemClickListener {
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Context context;
    private final String TAG = "Sen";
    private List<Milestone> mMilestones;

    // don't delete these
    public static final String EXTRA_PROJECT_NAME = "project_name";
    public static final String EXTRA_PROJECT_ID = "project_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        context = this;
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getReadableDatabase();


        // get the widgets
        EditText ET_projectName = (EditText) findViewById(R.id.projectName);
        EditText ET_dueDate = (EditText) findViewById(R.id.dueDate);
        EditText ET_numberOfMilestone = (EditText) findViewById(R.id.numberOfMilestone);
        EditText ET_projectPartner = (EditText) findViewById(R.id.projectPartner);
        ListView listView = (ListView) findViewById(R.id.listView);

        // get data from db and create a list for listView
        long mProjectId = (long) getIntent().getExtras().get(EXTRA_PROJECT_ID);
        String[] projection = {DatabaseContract.ProjectEntry._ID,
                DatabaseContract.ProjectEntry.COLUMN_NAME_NAME,
                DatabaseContract.ProjectEntry.COLUMN_NAME_DUE_DATE};
        String selection = DatabaseContract.ProjectEntry._ID + " = " + mProjectId;
        Cursor mCursor = db.query(DatabaseContract.ProjectEntry.TABLE_NAME, projection, selection, null, null, null, null);

        mCursor.moveToFirst();
        String mProjectName = (String) mCursor.getString(mCursor.getColumnIndex(DatabaseContract.ProjectEntry.COLUMN_NAME_NAME));
        String mDueDate = (String) mCursor.getString(mCursor.getColumnIndex(DatabaseContract.ProjectEntry.COLUMN_NAME_DUE_DATE));
        try {
            Project mProject = new Project(mProjectId, mProjectName, mDueDate, new MyProjectDatabaseOpListener(context), new MyMilestoneDatabaseOpListener(context));
            mMilestones = mProject.getMilestones();
        } catch (ParseException e) {
            Log.i(TAG, e.toString());
        }

        // set data to EditText
        ET_projectName.setText(mProjectName);
        ET_dueDate.setText(mDueDate);
        ET_numberOfMilestone.setText(Integer.toString(mMilestones.size()));
        ET_projectPartner.setText("WRONG");

        // create milestone listView in run-time
        listView.setAdapter(new MilestonesArrayAdapter(this, R.layout.milestone_row, mMilestones));
        listView.setOnItemClickListener(this);

        // get the save button
        Button saveButton = (Button) findViewById(R.id.saveButton);
        if (saveButton != null) {
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // add data to db
                }
            });
        } else {
            toastShow("fail to get the save button!");
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // actually we don't need this one
    }

    private void toastShow(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
