package dontkillthetree.scu.edu.UI;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import dontkillthetree.scu.edu.Util.Util;

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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // get the layout inflater
//                LayoutInflater inflater = context.getLayoutInflater(); // doesn't work
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);

                // inflate and set the layout for the dialog
                final View alertDialogView = inflater.inflate(R.layout.milestone_name_alertdialog, null);
                builder.setView(alertDialogView)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // get the new milestone name
                                EditText editMilestoneName = (EditText) alertDialogView.findViewById(R.id.editMilestoneName);
                                String newMilestoneName = editMilestoneName.getText().toString();
                                // add the new milestone name into db
                                showToast("New milestone name is: " + newMilestoneName);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public  void onClick(DialogInterface dialog, int d) {
                                showToast("Cancel button clicked!");
//                                context.getDialog().cancel();
                            }
                        });
                builder.create().show();
            }
        });

        holder.milestoneDueDay.setText(milestoneInfoList.get(position).getMilestoneDueDay());
        holder.milestoneDueDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MilestoneInfo milestoneInfo = milestoneInfoList.get(position);
                // build a datePickerDialog
                Calendar mCalendar = Calendar.getInstance();
                // Use the current data as the default date in the picker
                int mYear = mCalendar.get(Calendar.YEAR);
                int mMonth = mCalendar.get(Calendar.MONTH);
                int mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                        Calendar mCalendar = Calendar.getInstance();
                        mCalendar.set(arg1, arg2, arg3);
                        showToast("New due date is: " + Util.calendarToString(mCalendar));
                    }
                };
                // Create a new instance of DatePickerDialog
                DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, mDateSetListener, mYear, mMonth, mDay);
                mDatePickerDialog.show();
            }
        });

        return row;
    }

    public class ScrapViewHolder {
        TextView milestoneName;
        TextView milestoneDueDay;
    }

    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
