package dontkillthetree.scu.edu.UI;

import android.graphics.drawable.ClipDrawable;
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

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
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
    //img with overdue
    private final static int DIE_COUNT = 5;
    private boolean isDie = false;
    private int numRabbit;

    private static MediaPlayer mMediaPlayer;

    private Tree mTree;
    private Spinner spinner;
    private Handler mHandler;
    //private int progressBarSpeed = 10;
    private String TAG = "SEN";
    private Context context = this;

    private int mCurrentStage = 0;
    private int mStageMaxExp = 0;
    private int lastStage = 0;

    // progress bar
    private ClipDrawable mImageDrawable;

    // a field in your class
    private int mLevel = 0;
    private static int fromLevel = 0;
    private int toLevel = 0;
    public static final int MAX_LEVEL = 10000;
    public static final int LEVEL_DIFF = 100;
    public static final int DELAY = 30;

   /* private Handler mUpHandler = new Handler();
    private Runnable animateUpImage = new Runnable() {
        @Override
        public void run() {
            doTheUpAnimation(fromLevel, toLevel);
        }
    };

    private Handler mDownHandler = new Handler();
    private Runnable animateDownImage = new Runnable() {
        @Override
        public void run() {
            doTheDownAnimation(fromLevel, toLevel);
        }
    };
*/
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

        //set tree image
        if(isDie){
            ImageView deadTree = (ImageView)findViewById(R.id.treeImage);
            Bitmap d = getBitmapFromAsset(this,"tree_die.png");
            deadTree.setImageBitmap(d);
            mTree.decreaseExperience(mTree.getExperience());
        }else {
            ImageView treeImage = (ImageView) findViewById(R.id.treeImage);
            Bitmap b = getBitmapFromAsset(this, mTree.getCurrentImage());
            treeImage.setImageBitmap(b);
        }
        //set rabbit image
        ImageView rabbit = (ImageView) findViewById(R.id.rabbit);
        Bitmap r = getBitmapFromAsset(this, "rabbit.png");
        rabbit.setImageBitmap(r);
        if(numRabbit == 1) {
            rabbit.setVisibility(View.VISIBLE);
        }else{
            rabbit.setVisibility(View.GONE);
        }
        //set exp bar
        /*final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mStageMaxExp = Stages.getStageMaxExp(mCurrentStage);
        mProgressBar.setMax(mStageMaxExp);

        int mCurrentExp = mTree.getExperience();
        mProgressBar.setProgress(mCurrentExp);*/
        renewExpBar();

        // start playing background music
//        BackgroundMusic.startPlay(context);

        // set progress bar
        //ImageView img_clipSource = (ImageView) findViewById(R.id.clip_source);
        //mImageDrawable = (ClipDrawable) img_clipSource.getDrawable();
        //mImageDrawable.setLevel(0);


//        toLevel = (mCurrentExp / mStageMaxExp) * MAX_LEVEL;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        myWelcomeScreen.onSaveInstanceState(outState);
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
                //android.R.layout.simple_spinner_dropdown_item,
                // android.R.layout.simple_list_item_1,
                R.layout.mullti_lines_spinner,
                items)
        );
        spinner.setOnItemSelectedListener(this);

        //renew image
        if(isDie){
            ImageView deadTree = (ImageView)findViewById(R.id.treeImage);
            Bitmap d = getBitmapFromAsset(this,"tree_die.png");
            deadTree.setImageBitmap(d);
            mTree.decreaseExperience(mTree.getExperience());
        }else {
            ImageView treeImage = (ImageView) findViewById(R.id.treeImage);
            Bitmap b = getBitmapFromAsset(this, mTree.getCurrentImage());
            treeImage.setImageBitmap(b);
        }
        //set Rabbit
        ImageView rabbit = (ImageView) findViewById(R.id.rabbit);
        Bitmap r = getBitmapFromAsset(this, "rabbit.png");
        rabbit.setImageBitmap(r);
        if(numRabbit == 1) {
            rabbit.setVisibility(View.VISIBLE);
        }else{
            rabbit.setVisibility(View.GONE);
        }

        //renew exp bar
        /*final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mCurrentStage = mTree.getCurrentStage();
        mStageMaxExp = Stages.getStageMaxExp(mCurrentStage);
        mProgressBar.setMax(mStageMaxExp);

        int mCurrentExp = mTree.getExperience();
        mProgressBar.setProgress(mCurrentExp);
*/
        renewExpBar();
        // renew progress bar
        /*double tmp = (double)mCurrentExp / (double)mStageMaxExp;
        int temp_level = (int) (tmp * MAX_LEVEL);

        if (toLevel == temp_level) {
            return;
        } else if(temp_level > MAX_LEVEL) {
            mLevel = 0;
            fromLevel = 0;
            toLevel = 0;
            ImageView img_clipSource = (ImageView) findViewById(R.id.clip_source);
            mImageDrawable = (ClipDrawable) img_clipSource.getDrawable();
            mImageDrawable.setLevel(0);
        } else {
            toLevel = (temp_level <= MAX_LEVEL) ? temp_level : toLevel;
            if (toLevel > fromLevel) {
                // cancel previous process first
                mDownHandler.removeCallbacks(animateDownImage);
                HomeActivity.this.fromLevel = toLevel;

                mUpHandler.post(animateUpImage);
            } else {
                // cancel previous process first
                mUpHandler.removeCallbacks(animateUpImage);
                HomeActivity.this.fromLevel = toLevel;

                mDownHandler.post(animateDownImage);
            }
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        BackgroundMusic.stopPlay(context);
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
            numRabbit = 0;
            return milestones;
        }
        //get ontime project's number
        int die = 0;
        for(Project p:projects){
            if(p.getCurrentMilestone() != null){
                count ++;
                currentProjects.add(p);
                if(!p.getCurrentMilestone().isOnTime()){
                    die++;
                }
            }
        }
        Log.i("Jcheng","die count is:"+die);
        //decide if let tree die
        if(die >= DIE_COUNT){
            isDie = true;
            numRabbit = 0;
        }else if(die < DIE_COUNT && die > 0){
            numRabbit = 1;
        }else{
            numRabbit = 0;
        }
        Log.i("Jcheng","num of rabbit:"+numRabbit);

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
                String s;
                if(!m.isOnTime()){
                    s= currentProjects.get(i).getName()+"\n"+m.getName() + " OVERDUE: " + Util.calendarToString(nearest)+"\n";
                }else{
                    s= currentProjects.get(i).getName()+"\n"+m.getName() + " DUE AT: " + Util.calendarToString(nearest)+"\n";
                }
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


   /* private void doTheUpAnimation(int fromLevel, int toLevel) {
        mLevel += LEVEL_DIFF;
        mImageDrawable.setLevel(mLevel);
        if (mLevel <= toLevel) {
            mUpHandler.postDelayed(animateUpImage, DELAY);
        } else {
            mUpHandler.removeCallbacks(animateUpImage);
            HomeActivity.this.fromLevel = toLevel;
        }
    }

    private void doTheDownAnimation(int fromLevel, int toLevel) {
        mLevel -= LEVEL_DIFF;
        mImageDrawable.setLevel(mLevel);
        if (mLevel >= toLevel) {
            mDownHandler.postDelayed(animateDownImage, DELAY);
        } else {
            mDownHandler.removeCallbacks(animateDownImage);
            HomeActivity.this.fromLevel = toLevel;
        }

    }*/

private void renewExpBar(){
    final RoundCornerProgressBar expBar = (RoundCornerProgressBar)findViewById(R.id.expBar);
    mCurrentStage = mTree.getCurrentStage();
    int maxExp = Stages.getStageMaxExp(mCurrentStage);
    int currentExp = mTree.getExperience();
    expBar.setMax(maxExp);
    expBar.setProgress(currentExp);
}
}



