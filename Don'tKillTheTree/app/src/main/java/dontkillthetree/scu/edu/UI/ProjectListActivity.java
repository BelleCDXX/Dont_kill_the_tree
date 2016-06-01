package dontkillthetree.scu.edu.UI;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import dontkillthetree.scu.edu.model.Project;
import dontkillthetree.scu.edu.model.Projects;

public class ProjectListActivity extends ParentActivity implements View.OnClickListener{
    private Context context = this;
    public static List<Project> projectList = new ArrayList<>();
    //private int expIncreased = 30;
    private String TAG = "CHENG";
    //fragments
    private Fragment doneFrag;
    private Fragment currentFrag;

    //layouts
    private FrameLayout currentLayout,doneLayout;

    //textView
    private TextView currentTextView,doneTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);
//        context = ProjectListActivity.this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //Get the ListView
       // projectListView = (ListView)findViewById(R.id.projectListView);

        //Populate the arrayList with Project object
        try {
            Projects.getAllProjects(context);
            projectList = Projects.projects;
            //projectList = Projects.getAllProjects(context);
        } catch(ParseException ex) {
            Log.i(TAG, ex.toString());
        }
        initializeView();
        intializeClickEvent();
        intializeFragment();

        //Set arrayAdapter
        //projectListView.setAdapter(new ProjectsArrayAdapter(this, R.layout.project_row, projectList));
        //projectListView.setOnItemClickListener(this);

        //set floating action button which used to create a new project
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        assert fab != null;
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ProjectListActivity.this, AddProjectName.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("cxiong", "onResume run");

        try {
            Projects.getAllProjects(context);
            projectList = Projects.projects;
            //projectList = Projects.getAllProjects(context);
        } catch(ParseException ex) {
            Log.i(TAG, ex.toString());
        }

        //Set arrayAdapter
        //projectListView.setAdapter(new ProjectsArrayAdapter(this, R.layout.project_row, projectList));
    }
    private void initializeView(){
        currentLayout = (FrameLayout)findViewById(R.id.currentLayout);
        doneLayout = (FrameLayout)findViewById(R.id.doneLayout);
        currentTextView =(TextView)findViewById(R.id.currentTextView);
        doneTextView = (TextView)findViewById(R.id.doneTextView);
    }
    private void intializeClickEvent(){
        currentLayout.setOnClickListener(this);
        doneLayout.setOnClickListener(this);
    }
    private void intializeFragment(){
        doneFrag = new DoneFragement();
        currentFrag = new CurrentFragement();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.content,currentFrag).commit();
    }


   /* @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Project mProject = projectList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(ProjectListActivity.this);
        builder.setIcon(R.mipmap.ic_launcher)
                .setTitle("Choosing")
                .setMessage("What do you want to do next? ")
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // link to project detail activity
                        Intent intent = new Intent(ProjectListActivity.this, ProjectDetailActivity.class);
                        intent.putExtra(ProjectDetailActivity.EXTRA_PROJECT_ID_FROM_LIST, mProject.getId());
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Complete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // update the status of milestone as COMPLETED
                        if(mProject.getCurrentMilestone() != null){
                            mProject.getCurrentMilestone().setCompleted(true);
                        }
                        // update the experience of Tree
                        Tree mTree = Tree.getInstance(context);
                        mTree.increaseExperience(expIncreased);

                        try {
                            Projects.getAllProjects(context);
                            projectList = Projects.projects;
                            //projectList = Projects.getAllProjects(context);
                        } catch(ParseException ex) {
                            Log.i(TAG, ex.toString());
                        }
                        //Set arrayAdapter
                        //projectListView.setAdapter(new ProjectsArrayAdapter(context, R.layout.project_row, projectList));
                    }
                })
                .setCancelable(true);
        builder.create().show();
    }*/

    private void toastShow(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.currentLayout:
                clickTab(currentFrag);
                break;
            case R.id.doneLayout:
                clickTab(doneFrag);
                break;
            default:
                break;
        }
    }

    private void clickTab(Fragment fragment){
//        clearSelected();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.hide(currentFrag);
//        transaction.hide(doneFrag);
//        transaction.show(fragment);
//        transaction.commit();
//
//        changeTab(fragment);
        transaction.replace(R.id.content, fragment).commit();
    }
    private void clearSelected(){
        if(!currentFrag.isHidden()){
            //set to original
            currentTextView.setTextColor(Color.BLACK);
            currentLayout.setBackgroundColor(Color.WHITE);
        }
        if(!doneFrag.isHidden()){
            doneTextView.setTextColor(Color.BLACK);
            doneLayout.setBackgroundColor(Color.WHITE);
        }
    }
    private void changeTab(Fragment fragment){
        if(fragment instanceof CurrentFragement){
            currentLayout.setBackgroundColor(Color.GRAY);
        }
        if(fragment instanceof  DoneFragement){
            doneLayout.setBackgroundColor(Color.GRAY);
        }
        }

    public void refresh(){
        finish();
        Intent intent = new Intent(ProjectListActivity.this,ProjectListActivity.class);
        startActivity(intent);
    }
}
