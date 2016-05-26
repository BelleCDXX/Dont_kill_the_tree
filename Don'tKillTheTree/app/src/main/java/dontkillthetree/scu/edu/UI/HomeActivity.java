package dontkillthetree.scu.edu.UI;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.model.Milestone;
import dontkillthetree.scu.edu.model.Project;
import dontkillthetree.scu.edu.model.Projects;
import dontkillthetree.scu.edu.model.Stages;
import dontkillthetree.scu.edu.model.Tree;

public class HomeActivity extends ParentActivity implements AdapterView.OnItemSelectedListener{
    private final static String[] DEFAULT_ITEM = {"none"};

    private Tree mTree;
    private Spinner spinner;
    private Handler mHandler;
    private int progressBarSpeed = 10;
    private String TAG = "SEN";
    private ArrayList<String> upcomings = null;
    String[] items = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTree = Tree.getInstance(this);

        //set tree image
        ImageView treeImage = (ImageView)findViewById(R.id.treeImage);
        Bitmap b = getBitmapFromAsset(this,mTree.getCurrentImage());
        treeImage.setImageBitmap(b);

        //set exp bar
        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // set the max exp of current stage
                int mCurrentStage = mTree.getCurrentStage();
                int mStageMaxExp = Stages.getStageMaxExp(mCurrentStage);
                mProgressBar.setMax(mStageMaxExp);

                // get the current exp
                int mCurrentExp = mTree.getExperience();
                int mProgressStatus = 0;
                while (mProgressStatus < mCurrentExp) {
                    // update the progress bar
                    mProgressBar.setProgress(mProgressStatus);
                    mProgressStatus += progressBarSpeed;
                    try {
                        Thread.sleep(200);
                    } catch (Exception ex) {
                        Log.i(TAG, ex.toString());
                    }
                }
            }
        }).start();


        //set spinner heres
        if(getUpcomingMilestones()==null || getUpcomingMilestones().isEmpty()){
            items = DEFAULT_ITEM;
        }else {
            upcomings = getUpcomingMilestones();
            items = upcomings.toArray(new String[getUpcomingMilestones().size()]);
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                // android.R.layout.simple_list_item_1,
                items)
        );
        spinner.setOnItemSelectedListener(this);

        //set floating action button which used to create a new project
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddProjectName.class);
                startActivity(intent);
            }
        });
    }


    //get bitmap from assets
    private static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //set spinner heres
        if(getUpcomingMilestones()==null || getUpcomingMilestones().isEmpty()){
            items = DEFAULT_ITEM;
        }else {
            upcomings = getUpcomingMilestones();
            items = upcomings.toArray(new String[getUpcomingMilestones().size()]);
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                // android.R.layout.simple_list_item_1,
                items)
        );
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Position: " + position + ", Data: " + items[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(), "Nothing selected", Toast.LENGTH_SHORT).show();
    }

    //test button
    public void goToListButton(View view){
        Intent intent = new Intent(this,ProjectListActivity.class);
        startActivity(intent);
    }

    //getupcoming milestone
    private ArrayList<String> getUpcomingMilestones(){
        ArrayList<Project> projects = new ArrayList<Project>();
        ArrayList<String> milestones = new ArrayList<String>();
        Calendar nearest = null;


        try {
            Projects.getAllProjects(this);
            projects = (ArrayList<Project>) Projects.projects;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(projects.size()< 1){
            return milestones;
        }
        //get upcoming duedate
        for(int i = 0; i< projects.size();i++){
            Milestone m = projects.get(i).getCurrentMilestone();
            if(nearest == null){
                nearest = m.getDueDate();
            }else if(m.getDueDate().compareTo(nearest) > 0){
                nearest = m.getDueDate();
            }
        }

        //get upcoming milestones
        for (int i=0;i<projects.size();i++){
            Milestone m = projects.get(i).getCurrentMilestone();
            if(nearest.equals(nearest)){
                String s= projects.get(i).getName()+" _ "+m.getName() + " _ DUE AT: " + Util.calendarToString(m.getDueDate());
                milestones.add(s);
            }
        }

        return milestones;
    }
}



