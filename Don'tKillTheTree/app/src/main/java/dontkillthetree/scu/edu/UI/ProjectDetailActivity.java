package dontkillthetree.scu.edu.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProjectDetailActivity extends ParentActivity {

    public static final String EXTRA_PROJECT_NAME = "Project Name";
    public static final String EXTRA_PROJECT_DUE_DATE = "Project Due Name";
    public static final String EXTRA_NUMBER_OF_MILESTONES = "Number of Milestones";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        // test if get the right name and due date
        Intent intent = getIntent();
        String projectName;
        String projectDueDate;
        String numberOfMilestones;

        projectName = intent.getExtras().getString(EXTRA_PROJECT_NAME);
        projectDueDate = intent.getExtras().getString(EXTRA_PROJECT_DUE_DATE);
        numberOfMilestones = Integer.toString(intent.getExtras().getInt(EXTRA_NUMBER_OF_MILESTONES));

        ((TextView)findViewById(R.id.project_name_in_detail)).setText(projectName);
        ((TextView)findViewById(R.id.due_date_in_detail)).setText(projectDueDate);
        ((TextView)findViewById(R.id.number_of_milestones_in_detail)).setText(numberOfMilestones);
    }
}
