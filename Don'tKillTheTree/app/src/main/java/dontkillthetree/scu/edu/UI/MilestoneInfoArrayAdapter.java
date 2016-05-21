package dontkillthetree.scu.edu.UI;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by jasonzhang on 5/19/16.
 */
public class MilestoneInfoArrayAdapter extends ArrayAdapter<MilestoneInfo> {
    private final List<MilestoneInfo> milestoneInfoList;
    private Context context;

    public MilestoneInfoArrayAdapter(Context context, int resource, List<MilestoneInfo> milestoneInfoList) {
        super(context, resource, milestoneInfoList);
        this.milestoneInfoList = milestoneInfoList;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ScrapViewHolder holder;

        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.milestone_row, parent, false);

            holder = new ScrapViewHolder();
            holder.milestoneName = (TextView) row.findViewById(R.id.milestoneName);
            holder.milestoneDueDay = (TextView) row.findViewById(R.id.milestoneDueDay);
            row.setTag(holder);

        } else {
            holder = (ScrapViewHolder) row.getTag();
        }

        holder.milestoneName.setText(milestoneInfoList.get(position).getMilestoneName());
        holder.milestoneName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MilestoneInfo milestoneInfo = milestoneInfoList.get(position);
                // build a alertDialog
            }
        });

        holder.milestoneDueDay.setText(milestoneInfoList.get(position).getMilestoneDueDay());
        holder.milestoneDueDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MilestoneInfo milestoneInfo = milestoneInfoList.get(position);
                // build a datePickerDialog
            }
        });

        return row;
    }

    public class ScrapViewHolder {
        TextView milestoneName;
        TextView milestoneDueDay;
    }
}
