package dontkillthetree.scu.edu.UI;

import android.media.MediaPlayer;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.stephentuso.welcome.WelcomeScreenHelper;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.model.Audio;
import dontkillthetree.scu.edu.model.Milestone;
import dontkillthetree.scu.edu.model.Project;
import dontkillthetree.scu.edu.model.Projects;
import dontkillthetree.scu.edu.model.Stages;
import dontkillthetree.scu.edu.model.Tree;
import dontkillthetree.scu.edu.service.BackgroundMusic;

public class HomeActivity extends ParentActivity implements AdapterView.OnItemSelectedListener{

    private final static String[] DEFAULT_ITEM = {"none"};

    private static MediaPlayer mMediaPlayer;

    private Tree mTree;
    private Spinner spinner;
    private Handler mHandler;
    //private int progressBarSpeed = 10;
    private String TAG = "SEN";
    private Context context = this;

    int mCurrentStage = 0;
    int mStageMaxExp = 0;
    int lastStage = 0;

    private WelcomeScreenHelper myWelcomeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set up the welcome screen
        myWelcomeScreen = new WelcomeScreenHelper(this, UserGuideActivity.class);
        myWelcomeScreen.show(savedInstanceState);

        mTree = Tree.getInstance(this);
        mCurrentStage = mTree.getCurrentStage();
        lastStage = mCurrentStage;

        //set tree image
        ImageView treeImage = (ImageView)findViewById(R.id.treeImage);
        Bitmap b = getBitmapFromAsset(this,mTree.getCurrentImage());
        treeImage.setImageBitmap(b);

        //set exp bar
        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mStageMaxExp = Stages.getStageMaxExp(mCurrentStage);
        mProgressBar.setMax(mStageMaxExp);

        int mCurrentExp = mTree.getExperience();
        mProgressBar.setProgress(mCurrentExp);

       /* new Thread(new Runnable() {
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
        }).start();*/

        //set spinner heres
        String[] items = DEFAULT_ITEM;
        if(getUpcomingMilestones()==null || getUpcomingMilestones().isEmpty()){
            items = DEFAULT_ITEM;
        }else {
            items = getUpcomingMilestones().toArray(new String[getUpcomingMilestones().size()]);
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                // android.R.layout.simple_list_item_1,
                items)
        );
        spinner.setOnItemSelectedListener(this);

        // start playing background music
//        mMediaPlayer = MediaPlayer.create(context, R.raw.start_all_over_again);
//        mMediaPlayer.setLooping(true);
//        mMediaPlayer.start();
        BackgroundMusic.startPlay(context);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //set spinner heres
        String[] items=DEFAULT_ITEM;
        if(getUpcomingMilestones()==null || getUpcomingMilestones().isEmpty()){
            items = DEFAULT_ITEM;
        }else {
            items = getUpcomingMilestones().toArray(new String[getUpcomingMilestones().size()]);
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                // android.R.layout.simple_list_item_1,
                items)
        );
        spinner.setOnItemSelectedListener(this);

        //renew exp bar
        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mCurrentStage = mTree.getCurrentStage();
        mStageMaxExp = Stages.getStageMaxExp(mCurrentStage);
        mProgressBar.setMax(mStageMaxExp);

        int mCurrentExp = mTree.getExperience();
        mProgressBar.setProgress(mCurrentExp);

        //renew image
        ImageView treeImage = (ImageView)findViewById(R.id.treeImage);
        Bitmap b = getBitmapFromAsset(this,mTree.getCurrentImage());
        treeImage.setImageBitmap(b);
        levelUpToast();

    }

//    @Override
//    protected void onUserLeaveHint() {
//        super.onUserLeaveHint();
//        BackgroundMusic.stopPlay(context);
//
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        BackgroundMusic.stopPlay(context);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mMediaPlayer.release();
//        mMediaPlayer = null;
        BackgroundMusic.stopPlay(context);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //test button
    public void goToListButton(View view){
        Intent intent = new Intent(this,ProjectListActivity.class);
        startActivity(intent);
    }

    //getupcoming milestone
    private ArrayList<String> getUpcomingMilestones(){
        ArrayList<Project> projects = new ArrayList<Project>();
        ArrayList<Project> currentProjects = new ArrayList<Project>();
        ArrayList<String> milestones = new ArrayList<String>();
        Calendar nearest = null;
        int count = 0;


        try {
            Projects.getAllProjects(this);
            projects = (ArrayList<Project>) Projects.projects;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(projects.size()< 1){
            return milestones;
        }
        //get ontime project's number
        for(Project p:projects){
            if(p.getCurrentMilestone() != null){
                count ++;
                currentProjects.add(p);
            }
        }
        if(currentProjects.size()<1){
            return milestones;
        }
        Log.i("JCheng","size: " + currentProjects.size());
        //get upcoming duedate
        for(int i = 0; i < currentProjects.size(); i++){
            Milestone m = currentProjects.get(i).getCurrentMilestone();
            if(nearest == null){
                nearest = m.getDueDate();
            }else if(m.getDueDate().compareTo(nearest) < 0){
                nearest = m.getDueDate();
            }
        }
    Log.i("Jc","nearst: "+Util.calendarToString(nearest));
        //get upcoming milestones
        for (int i=0;i<currentProjects.size();i++){
            Milestone m = currentProjects.get(i).getCurrentMilestone();
            if(currentProjects.get(i).getCurrentMilestone().getDueDate().equals(nearest)){
                String s= currentProjects.get(i).getName()+"_"+m.getName() + " DUE AT:" + Util.calendarToString(nearest);
                milestones.add(s);
            }
        }

        return milestones;
    }
    private void levelUpToast(){
        if(mCurrentStage > lastStage){
            Toast.makeText(getApplicationContext(),"Congratulation! Tree level up!",Toast.LENGTH_SHORT).show();
            lastStage = mCurrentStage;
        }
    }


    //check if project is completed
    /*private boolean isProjectComplted(Project project){
        int count = 0;
        List<Milestone> list = new ArrayList<Milestone>();

        if(project.getMilestones().size()<1){
            return true;
        }
        list = project.getMilestones();

        for(Milestone m:list){
            if(!m.isCompleted()){
                count++;
            }
        }

        if(count > 0){
            return false;
        }
        return true;
    }*/
    //set menu, add go to list icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.go_to_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.go_to_list:
                // when click go to list button in the action bar
                Audio.makeClickSound(context);

                Intent intent = new Intent(HomeActivity.this, ProjectListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            default:
                return true;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        myWelcomeScreen.onSaveInstanceState(outState);
    }
}



