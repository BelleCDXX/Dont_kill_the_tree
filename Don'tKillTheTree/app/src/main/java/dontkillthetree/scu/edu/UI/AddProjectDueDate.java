package dontkillthetree.scu.edu.UI;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.GregorianCalendar;

import dontkillthetree.scu.edu.Calender.CalendarAdapter;
//import dontkillthetree.scu.edu.Calender.CalendarCollection;

public class AddProjectDueDate extends ParentActivity implements NumberPicker.OnValueChangeListener {

    String projectName;
    String projectDueDate;
    int numberOfMilestones;
    TextView numOfMilestones;


    public GregorianCalendar cal_month, cal_month_copy;
    private CalendarAdapter cal_adapter;
    private TextView tv_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_due_date);

        projectName = (String) getIntent().getExtras().get(ProjectDetailActivity.EXTRA_PROJECT_NAME);

        numOfMilestones = ((TextView)findViewById(R.id.number_of_milestones));

        // set num of milestones button listener
        findViewById(R.id.set_num_of_milestones).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                showMilestoneDialog();
            }
        });

        // set calender view
        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        cal_adapter = new CalendarAdapter(this, cal_month);



        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));

        ImageButton previous = (ImageButton) findViewById(R.id.ib_prev);

        if (previous != null) {
            previous.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setPreviousMonth();
                    refreshCalendar();
                }
            });
        }

        ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
        if (next != null) {
            next.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setNextMonth();
                    refreshCalendar();

                }
            });
        }

        GridView gridview = (GridView) findViewById(R.id.gv_calendar);
        if (gridview != null) {
            gridview.setAdapter(cal_adapter);
        }
        if (gridview != null) {
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    ((CalendarAdapter) parent.getAdapter()).setSelected(v,position);
                    String selectedGridDate = CalendarAdapter.day_string
                            .get(position);

                    String[] separatedTime = selectedGridDate.split("-");
                    String gridvalueString = separatedTime[2].replaceFirst("^0*","");
                    int gridvalue = Integer.parseInt(gridvalueString);

                    if ((gridvalue > 10) && (position < 8)) {
                        setPreviousMonth();
                        refreshCalendar();
                    } else if ((gridvalue < 7) && (position > 28)) {
                        setNextMonth();
                        refreshCalendar();
                    }
                    ((CalendarAdapter) parent.getAdapter()).setSelected(v,position);

                    //save selected date to projectDueDate
                    projectDueDate = selectedGridDate;

                    //((CalendarAdapter) parent.getAdapter()).getPositionList(selectedGridDate, AddProjectDueDate.this);
                }

            });
        }

    }

    public void showMilestoneDialog()
    {

        final Dialog dialog = new Dialog(AddProjectDueDate.this);
        dialog.setTitle("Set Num of Milestones");
        dialog.setContentView(R.layout.dialog_num_of_milestones);

        Button b1 = (Button) dialog.findViewById(R.id.set_button);
        Button b2 = (Button) dialog.findViewById(R.id.cancel_button);

        final NumberPicker np = (NumberPicker) dialog.findViewById(R.id.milestone_number_picker);
        //set max num of milestones
        np.setMaxValue(20);
        np.setMinValue(0);

        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                numOfMilestones.setText(String.valueOf(np.getValue()));
                dialog.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }


    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
                    cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
                    cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    public void refreshCalendar() {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }

    // implement NumberPicker.OnValueChangeListener
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Log.i("value is", "" + newVal);
    }

    // get number of milestones
    private int getNumberOfMilestones(){
        if (numOfMilestones != null && numOfMilestones.getText() != null) {
            String milestones = numOfMilestones.getText().toString();
            return Integer.parseInt(milestones);
        }
        else {
            return 0;
        }
    }



    //set menu, add create project icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_project_due_date_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.create_project:
                numberOfMilestones = getNumberOfMilestones();
                if(projectDueDate == null){
                    Toast.makeText(AddProjectDueDate.this, "Please select a due date.", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent intent = new Intent(AddProjectDueDate.this, ProjectDetailActivity.class);
                    intent.putExtra(ProjectDetailActivity.EXTRA_PROJECT_NAME, projectName);
                    intent.putExtra(ProjectDetailActivity.EXTRA_PROJECT_DUE_DATE, projectDueDate);
                    intent.putExtra(ProjectDetailActivity.EXTRA_NUMBER_OF_MILESTONES, numberOfMilestones);
                    startActivity(intent);
                }
                break;
            default:
                return true;
        }
        return true;
    }


}
