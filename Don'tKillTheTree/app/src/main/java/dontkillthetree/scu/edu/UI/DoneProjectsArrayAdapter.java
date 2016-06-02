package dontkillthetree.scu.edu.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;

import java.util.List;

import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.model.Project;
/**
 * Created by cheng11 on 5/29/16.
 */
public class DoneProjectsArrayAdapter extends ArrayAdapter<Project> {
    private final List<Project> mProjects;
    private Context context;

    public DoneProjectsArrayAdapter(Context context, int resource, List<Project> projects) {
        super(context, resource, projects);
        this.mProjects = projects;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ScrapViewHolder holder;

        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item_completed_project, parent, false);

            holder = new ScrapViewHolder();
            holder.projectName = (TextView) row.findViewById(R.id.list_item_completed_project_name);
            holder.milestoneName = (TextView) row.findViewById(R.id.list_item_completed_milestone_name);
            holder.milestoneDueDate = (TextView) row.findViewById(R.id.list_item_completed_milestone_due_date);
            holder.swipeRevealLayout = (SwipeRevealLayout) row.findViewById(R.id.list_item_completed_swipe_layout);
            holder.mainLayout = row.findViewById(R.id.list_item_completed_main_layout);
            holder.editView = row.findViewById(R.id.list_item_completed_edit);
            row.setTag(holder);

        } else {
            holder = (ScrapViewHolder) row.getTag();
        }


        if (mProjects.get(position).getCurrentMilestone() == null) {
            holder.projectName.setText(mProjects.get(position).getName());
            holder.milestoneName.setText("Done");
            holder.milestoneDueDate.setText(Util.calendarToString(mProjects.get(position).getDueDate()));
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
        }

        return row;
    }

    private class ScrapViewHolder {
        TextView projectName;
        TextView milestoneName;
        TextView milestoneDueDate;
        View mainLayout;
        View editView;
        SwipeRevealLayout swipeRevealLayout;
    }
}
