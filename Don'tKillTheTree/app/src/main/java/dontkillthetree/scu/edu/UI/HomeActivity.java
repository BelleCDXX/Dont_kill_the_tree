package dontkillthetree.scu.edu.UI;

import android.app.Dialog;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;

import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.stephentuso.welcome.WelcomeScreenHelper;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.model.Milestone;
import dontkillthetree.scu.edu.model.Project;
import dontkillthetree.scu.edu.model.Projects;
import dontkillthetree.scu.edu.model.Stages;
import dontkillthetree.scu.edu.model.Tree;

public class HomeActivity extends ParentActivity implements AdapterView.OnItemSelectedListener,SensorEventListener {

    //sensor related
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    final private static int FRUIT_COST = 50;
    ImageView fruitImg;
    TextView fruitText;
    private int fruitCount = 0;
    final static String fruitPath="fruit.png";

    private final static String[] DEFAULT_ITEM = {"none"};
    //img with overdue
    private final static int DIE_COUNT = 5;
    private boolean isDie = false;
    private int numRabbit;

//    private static MediaPlayer mMediaPlayer;

    private Tree mTree;
    private Spinner spinner;
    private Handler mHandler;
    //private int progressBarSpeed = 10;
    private String TAG = "SEN";
    private Context context = this;

    private int mCurrentStage = 0;
    private int mStageMaxExp = 0;
    private int lastStage = 0;

    // cheer up and boo down sound
    private MediaPlayer mMediaPlayer;

    private WelcomeScreenHelper myWelcomeScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //sensor related
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;


        // Set up the welcome screen
        myWelcomeScreen = new WelcomeScreenHelper(this, UserGuideActivity.class);
        myWelcomeScreen.show(savedInstanceState);

        mTree = Tree.getInstance(this);
        mCurrentStage = mTree.getCurrentStage();
        lastStage = mCurrentStage;

        //set fruit
        fruitImg = (ImageView)findViewById(R.id.fruit);
        Bitmap fb = getBitmapFromAsset(this,fruitPath);
        fruitImg.setImageBitmap(fb);
        fruitText =(TextView)findViewById(R.id.numFruit);
        fruitText.setText(String.valueOf(fruitCount));

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

        renewExpBar();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        myWelcomeScreen.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //sensor change
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
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
        //fruit
        fruitText.setText(String.valueOf(fruitCount));
        //renew exp bar
        renewExpBar();
        // Show toast and sound when tree level up and down
        levelChangeToast();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        BackgroundMusic.stopPlay(context);
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
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
            isDie = false;
        }else{
            numRabbit = 0;
            isDie = false;
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


    private void levelChangeToast(){

        if(mCurrentStage > lastStage){
            Toast.makeText(getApplicationContext(),"Congratulation! Tree level up!",Toast.LENGTH_SHORT).show();
            mMediaPlayer = MediaPlayer.create(context, R.raw.tree_level_up);
            mMediaPlayer.setVolume(1.0f, 1.0f);
            mMediaPlayer.start();
        } else if (mCurrentStage < lastStage) {
            Toast.makeText(getApplicationContext(),"Whoops! Tree level down!",Toast.LENGTH_SHORT).show();
            mMediaPlayer = MediaPlayer.create(context, R.raw.tree_level_down);
            mMediaPlayer.setVolume(1.0f, 1.0f);
            mMediaPlayer.start();
        }
        lastStage = mCurrentStage;
    }

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
                Intent intent = new Intent(HomeActivity.this, ProjectListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            default:
                return true;
        }
        return true;
    }


    private void renewExpBar(){
        final RoundCornerProgressBar expBar = (RoundCornerProgressBar)findViewById(R.id.expBar);
        mCurrentStage = mTree.getCurrentStage();
        int maxExp = Stages.getStageMaxExp(mCurrentStage);
        int currentExp = mTree.getExperience();
        expBar.setMax(maxExp);
        expBar.setProgress(currentExp);
    }
    private void displayAcceleration(){
        float accel = Math.abs( mAccel);
        if (accel > 1.0f) {
            shakeOp();
        }
    }
    private void shakeOp(){
        mCurrentStage = mTree.getCurrentStage();
        if(mCurrentStage ==Stages.getMaxStage()) {
            int max = Stages.getStageMaxExp(mCurrentStage);
            int current = mTree.getExperience();
            if (max == current) {
                getFruit();
                mTree.decreaseExperience(FRUIT_COST);
                renewExpBar();
            }
        }
    }
    private void getFruit(){
        Dialog d = new Dialog(this);
        d.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        d.setContentView(getLayoutInflater().inflate(R.layout.fruit_layout
                , null));
        d.show();
        //fruit num change
        fruitCount++;
        fruitText.setText(String.valueOf(fruitCount));
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta * 0.1f; // perform low-cut filter

        displayAcceleration();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}



