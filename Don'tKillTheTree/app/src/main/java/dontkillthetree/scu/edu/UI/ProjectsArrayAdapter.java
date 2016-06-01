package dontkillthetree.scu.edu.UI;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import dontkillthetree.scu.edu.Notification.CreateNotifyIntent;
import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.model.Project;
import dontkillthetree.scu.edu.model.Projects;
import dontkillthetree.scu.edu.model.Tree;

public class ProjectsArrayAdapter extends ArrayAdapter<Project> {
    private final List<Project> mProjects;
    private final ViewBinderHelper viewBinderHelper;
    private Context context;
    private int expIncreased = 30;
    private final String TAG = "Joey";

    public ProjectsArrayAdapter(Context context, int resource, List<Project> projects) {
        super(context, resource, projects);
        this.mProjects = projects;
        this.context = context;
        viewBinderHelper = new ViewBinderHelper();
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ScrapViewHolder holder;

        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.project_row, parent, false);


            holder = new ScrapViewHolder();
            holder.projectName = (TextView) row.findViewById(R.id.project_name);
            holder.milestoneName = (TextView) row.findViewById(R.id.milestone_name);
            holder.milestoneDueDate = (TextView) row.findViewById(R.id.milestone_due_date);
            holder.swipeRevealLayout = (SwipeRevealLayout) row.findViewById(R.id.swipe_layout);
            holder.mainLayout = row.findViewById(R.id.list_item_main);
            holder.editView = row.findViewById(R.id.list_item_edit);
            holder.doneView = row.findViewById(R.id.list_item_done);
            row.setTag(holder);

        } else {
            holder = (ScrapViewHolder) row.getTag();
        }

        //holder.projectName.setText(mProjects.get(position).getName());

        if (mProjects.get(position).getCurrentMilestone() != null) {
         /*   holder.milestoneName.setText("Done");
            holder.milestoneDueDate.setText("Done");
        } else {*/
            viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(mProjects.get(position).getId()));

            holder.projectName.setText(mProjects.get(position).getName());
            holder.milestoneName.setText(mProjects.get(position).getCurrentMilestone().getName());
            holder.milestoneDueDate.setText(Util.calendarToString(mProjects.get(position).getCurrentMilestone().getDueDate()));
            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.swipeRevealLayout.isClosed()) {
                        holder.swipeRevealLayout.open(true);
                    }
                    else {
                        holder.swipeRevealLayout.close(true);
                    }
                }
            });
            holder.editView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProjectDetailActivity.class);
                    intent.putExtra(ProjectDetailActivity.EXTRA_PROJECT_ID_FROM_LIST, mProjects.get(position).getId());
                    context.startActivity(intent);
                }
            });
            holder.doneView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // update the status of milestone as COMPLETED
                    Project mProject = mProjects.get(position);
                    if(mProject.getCurrentMilestone() != null){
                        mProject.getCurrentMilestone().setCompleted(true);

                        if (mProject.getCurrentMilestone() == null) {
                            mProjects.remove(position);
                            notifyDataSetChanged();
                        }
                    }
                    // update the experience of Tree
                    Tree mTree = Tree.getInstance(context);
                    mTree.increaseExperience(expIncreased);

//                    try {
//                        Projects.getAllProjects(context);
//                        ProjectListActivity.projectList = Projects.projects;
//                        projects = ProjectListActivity.projectList;
//                        currentProjects = checkList();
//                        //projectList = Projects.getAllProjects(context);
//                    } catch(ParseException ex) {
//                        Log.i(TAG, ex.toString());
//                    }
                    ProjectListActivity activity = (ProjectListActivity) context;
                    activity.refresh();

                    // create/update notification
                    CreateNotifyIntent.makeIntent(context);
                    //Set arrayAdapter
                    //setListAdapter(new ProjectsArrayAdapter(context,R.layout.project_row,currentProjects));
                    //projectListView.setAdapter(new ProjectsArrayAdapter(context, R.layout.project_row, projectList));
                }
            });
        }

        return row;
    }

    private class ScrapViewHolder {
        TextView projectName;
        TextView milestoneName;
        TextView milestoneDueDate;
        View mainLayout;
        View editView;
        View doneView;
        SwipeRevealLayout swipeRevealLayout;
    }

//    private List<Project> checkList(){
//        List<Project> result = new ArrayList<>();
//        for(Project p : mProjects){
//            if(p.getCurrentMilestone() != null){
//                result.add(p);
//            }
//        }
//        return result;
//    }

    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
