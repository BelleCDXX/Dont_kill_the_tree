package dontkillthetree.scu.edu.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import dontkillthetree.scu.edu.Calender.CalendarAdapter;
import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.event.MyMilestoneDatabaseOpListener;
import dontkillthetree.scu.edu.event.MyProjectDatabaseOpListener;
import dontkillthetree.scu.edu.model.Project;
import dontkillthetree.scu.edu.model.Projects;

public class AddProjectDueDate extends ParentActivity implements NumberPicker.OnValueChangeListener {

    private String projectName;
    //private String projectDueDate;
    private int numberOfMilestones;
    private TextView numOfMilestones;
    private Calendar selectedDueDate;
    private Calendar currentDate = Calendar.getInstance();


    public GregorianCalendar cal_month, cal_month_copy;
    private CalendarAdapter cal_adapter;
    private TextView tv_month;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_due_date);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Due Date");

        context = this;

        projectName = (String) getIntent().getExtras().get(ProjectDetailActivity.EXTRA_PROJECT_NAME);

        numOfMilestones = ((TextView)findViewById(R.id.number_of_milestones));

        // set num of milestones button and textview listener
        findViewById(R.id.set_num_of_milestones).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                showMilestoneDialog();
            }
        });
        findViewById(R.id.number_of_milestones).setOnClickListener(new View.OnClickListener()
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

        GridView gridview = (GridView) findViewById(R.id.gv_calendar);
        if (gridview != null) {
            gridview.setAdapter(cal_adapter);
        }

        ImageButton previous = (ImageButton) findViewById(R.id.ib_prev);

        if (previous != null) {
            previous.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v){
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


        if (gridview != null) {
            // set activities when click on calendar
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    //((CalendarAdapter) parent.getAdapter()).setSelected(v,position);
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
                    else{
                        Calendar dueDate = getCalendarDueDate(selectedGridDate);
                        // test if chose after current date
                        if (dueDate.after(currentDate)){
                            ((CalendarAdapter) parent.getAdapter()).setSelected(v,position);
                            selectedDueDate = dueDate;
                            refreshCalendar();
                        }

                        //save selected date to projectDueDate
                        //projectDueDate = selectedGridDate;

                    }


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
        np.setMinValue(1);

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
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMinimum(GregorianCalendar.MONTH))
        {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        }
        else
        {
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) - 1);
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

    // convert due date to Calendar object
    private Calendar getCalendarDueDate(String projectDueDate){
        // change format to MM/dd/yyyy
        String changedFormat = "" +
                projectDueDate.substring(5, 7) + "/" +
                projectDueDate.substring(8) + "/" +
                projectDueDate.substring(0,4);

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US); // Locale.US
        Date date = null;
        try {
            date = df.parse(changedFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //test
        //Toast.makeText(AddProjectDueDate.this, Util.calendarToString(cal), Toast.LENGTH_SHORT).show();
        return cal;
    }

    // save project date to database and return project ID
    private long saveData(String projectName, Calendar selectedDueDate, int numberOfMilestones){


        Project project = new Project(
                projectName,
                selectedDueDate,
                numberOfMilestones,
                null,
                null,
                new MyProjectDatabaseOpListener(context),
                new MyMilestoneDatabaseOpListener(context), context);

        // debug
        try {
            Projects.getAllProjects(context);
        }
        catch (ParseException ex){}

        return project.getId();
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
                // when click create project button in the action bar
                numberOfMilestones = getNumberOfMilestones() - 1;
                if(selectedDueDate == null){
                    Toast.makeText(AddProjectDueDate.this, "Please select a due date.", Toast.LENGTH_SHORT).show();
                }
                else if (numberOfMilestones < 0){
                    Toast.makeText(AddProjectDueDate.this, "Number of milestones should be grater than 0.", Toast.LENGTH_SHORT).show();
                }
                else {
                    //test if num of milestones is too large
                    Calendar dateAddMilestones = (Calendar) currentDate.clone();
                    dateAddMilestones.add(Calendar.DATE, numberOfMilestones);
                    if(dateAddMilestones.after(selectedDueDate)){
                        Toast.makeText(AddProjectDueDate.this, "Too many milestones.", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    long projectID = saveData(projectName, selectedDueDate, numberOfMilestones);
                    Intent intent = new Intent(AddProjectDueDate.this, ProjectDetailActivity.class);
                    intent.putExtra(ProjectDetailActivity.EXTRA_PROJECT_ID_FROM_CREATE, projectID);
                    intent.putExtra(ProjectDetailActivity.EXTRA_ON_CREATE_PROCESS, true);
                    startActivity(intent);
                }
                break;
            default:
                return true;
        }
        return true;
    }


}
