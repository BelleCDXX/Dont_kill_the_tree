package dontkillthetree.scu.edu.UI;

import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import dontkillthetree.scu.edu.model.Project;
import dontkillthetree.scu.edu.model.Projects;
import dontkillthetree.scu.edu.model.Tree;

/**
 * Created by cheng11 on 5/29/16.
 */
public class CurrentFragement extends ListFragment {

    /*static interface CurrentFragementListener{
        void itemClicked(long id);
    }
    private CurrentFragementListener listener;*/
    Context context;
    List<Project> projects;
    List<Project> currentProjects;
    private int expIncreased = 30;
    private String TAG = "CHENG";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        context = getActivity();
        super.onActivityCreated(savedInstanceState);
        projects = ProjectListActivity.projectList;
        currentProjects = checkList();
        ProjectsArrayAdapter adapter = new ProjectsArrayAdapter(context, R.layout.project_row, currentProjects);
        setListAdapter(adapter);
    }

    @Override
    public void onResume() {
        context = getActivity();
        super.onResume();
        projects = ProjectListActivity.projectList;
        currentProjects = checkList();
        ProjectsArrayAdapter adapter = new ProjectsArrayAdapter(getActivity(), R.layout.project_row, currentProjects);
        setListAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        final Project mProject = currentProjects.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.mipmap.ic_launcher)
                .setTitle("Choosing")
                .setMessage("What do you want to do next? ")
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // link to project detail activity
                        Intent intent = new Intent(context, ProjectDetailActivity.class);
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
                            ProjectListActivity.projectList = Projects.projects;
                            projects = ProjectListActivity.projectList;
                            currentProjects = checkList();
                            //projectList = Projects.getAllProjects(context);
                        } catch(ParseException ex) {
                            Log.i(TAG, ex.toString());
                        }
                        ProjectListActivity activity = (ProjectListActivity)context;
                        activity.refresh();
                        //Set arrayAdapter
                        //setListAdapter(new ProjectsArrayAdapter(context,R.layout.project_row,currentProjects));
                        //projectListView.setAdapter(new ProjectsArrayAdapter(context, R.layout.project_row, projectList));

                    }
                })
                .setCancelable(true);
        builder.create().show();
    }
    private List<Project> checkList(){
        List<Project> result = new ArrayList<>();
        for(Project p : projects){
            if(p.getCurrentMilestone() != null){
                result.add(p);
            }
        }
        return result;
    }
}
