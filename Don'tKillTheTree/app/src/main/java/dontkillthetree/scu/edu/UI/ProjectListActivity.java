package dontkillthetree.scu.edu.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.model.Project;
import dontkillthetree.scu.edu.model.Projects;

public class ProjectListActivity extends ParentActivity{
    private Context context = this;
    public static List<Project> projectList = new ArrayList<>();
    private static List<BitmapDrawable> backgroundImages;
    private static int backgroundImgHeight = -1;
    private String TAG = "CHENG";

    // fragments
    private Fragment doneFrag;
    private Fragment currentFrag;

    // tabs and pager
    private CustomViewPager pager;

    MyPaperAdapter myPaperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
//        context = ProjectListActivity.this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //Populate the arrayList with Project object
        updateProjectList();

        backgroundImgHeight = -1;
        backgroundImages = new ArrayList<>();

        initializeView();
        intializeFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProjectList();
    }

    private void initializeView(){
        pager = (CustomViewPager) findViewById(R.id.project_list_view_pager);
    }

    private void intializeFragment(){
        doneFrag = new DoneFragement();
        currentFrag = new CurrentFragement();

        myPaperAdapter = new MyPaperAdapter(getSupportFragmentManager());
        pager.setAdapter(myPaperAdapter);
    }

    //set menu, add go to tree icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_project_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.create_project_menu:
                // when click go to tree button in the action bar
                Intent intent = new Intent(ProjectListActivity.this, AddProjectName.class);
                startActivity(intent);
                break;
            case android.R.id.home:
                // when click go to tree button in the action bar
                Intent intent4 = new Intent(this, HomeActivity.class);
                intent4.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent4);
//                finish();
                break;
            default:
                return true;
        }
        return true;
    }

    public void refresh(){
        updateProjectList();
        currentFrag = new CurrentFragement();
        doneFrag = new DoneFragement();

        myPaperAdapter = new MyPaperAdapter(getSupportFragmentManager());
        pager.setAdapter(myPaperAdapter);

    }

    public static void updateBackgroundImages(Context context) {
        if (backgroundImgHeight > 0) {
            Util.cropListItemBackgroundImage(backgroundImages, backgroundImgHeight, context);
        }
    }

    public static List<BitmapDrawable> getBackgroundImages() {
        return backgroundImages;
    }

    public static int getBackgroundImgHeight() {
        return backgroundImgHeight;
    }

    public static void setBackgroundImgHeight(int newHeight) {
        backgroundImgHeight = newHeight;
    }

    public void updateProjectList() {
        try {
            Projects.getAllProjects(context);
            projectList = Projects.projects;
        } catch(ParseException ex) {
            MyErrorPage.ShowAlertDialog(this, ex.toString());
        }
    }

    public class MyPaperAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Current", "Completed"};

        public MyPaperAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                return currentFrag;
            }
            else if (position == 1){
                return doneFrag;
            }
            else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }
}
