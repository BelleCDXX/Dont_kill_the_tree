package dontkillthetree.scu.edu.UI;

import android.app.Activity;
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
public class DoneFragement extends ListFragment {
    /*static interface DoneFragementListner {
        void itemClicked(long id);
    }

    private DoneFragementListner listner;*/
    Context context;
    List<Project> projects;
    private int expIncreased = 30;
    private String TAG = "CHENG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        projects = ProjectListActivity.projectList;
        List<Project> doneProjects = listCheck();
        DoneProjectAdapter adapter = new DoneProjectAdapter(context, R.layout.project_row, doneProjects);
        setListAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        projects = ProjectListActivity.projectList;
        List<Project> doneProjects = listCheck();
        DoneProjectAdapter adapter = new DoneProjectAdapter(context, R.layout.project_row, doneProjects);
        setListAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        List<Project> doneProjects = listCheck();
        final Project mProject = doneProjects.get(position);

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
                .setCancelable(true);
        builder.create().show();
    }
    private List<Project> listCheck(){
        List<Project> result = new ArrayList<Project>();
        for(Project p:projects){
            if(p.getCurrentMilestone() == null){
                result.add(p);
            }
        }
        return result;
    }


    }

