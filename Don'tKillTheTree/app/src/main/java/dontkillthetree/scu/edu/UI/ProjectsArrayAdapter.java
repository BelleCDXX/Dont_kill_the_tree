package dontkillthetree.scu.edu.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.model.Milestone;
import dontkillthetree.scu.edu.model.Project;
import dontkillthetree.scu.edu.model.Projects;

/**
 * Created by jasonzhang on 5/24/16.
 */
public class ProjectsArrayAdapter extends ArrayAdapter<Project> {
    private final List<Project> mProjects;
    private Context context;

    public ProjectsArrayAdapter(Context context, int resource, List<Project> projects) {
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
            row = inflater.inflate(R.layout.project_row, parent, false);

            holder = new ScrapViewHolder();
            holder.projectName = (TextView) row.findViewById(R.id.project_name);
            holder.milestoneName = (TextView) row.findViewById(R.id.milestone_name);
            holder.milestoneDueDate = (TextView) row.findViewById(R.id.milestone_due_date);
            row.setTag(holder);

        } else {
            holder = (ScrapViewHolder) row.getTag();
        }

        holder.projectName.setText(mProjects.get(position).getName());
        holder.milestoneName.setText(mProjects.get(position).getCurrentMilestone().getName());
        holder.milestoneDueDate.setText(Util.calendarToString(mProjects.get(position).getCurrentMilestone().getDueDate()));

        return row;
    }

    public class ScrapViewHolder {
        TextView projectName;
        TextView milestoneName;
        TextView milestoneDueDate;
    }

    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
