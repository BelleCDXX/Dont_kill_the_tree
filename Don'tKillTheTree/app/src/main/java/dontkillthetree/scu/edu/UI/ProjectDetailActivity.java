package dontkillthetree.scu.edu.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProjectDetailActivity extends ParentActivity implements AdapterView.OnItemClickListener {
    private List<MilestoneInfo> milestoneInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        // get the widgets
        EditText projectName = (EditText) findViewById(R.id.projectName);
        EditText days = (EditText) findViewById(R.id.days);
        EditText numberOfMilestone = (EditText) findViewById(R.id.numberOfMilestone);
        EditText projectPartner = (EditText) findViewById(R.id.projectPartner);
        ListView listView = (ListView) findViewById(R.id.listView);

        // get data from db and create a arrayList for listView
        milestoneInfoList = new ArrayList<>();
        milestoneInfoList.add(new MilestoneInfo("MS1", "05/21/2016"));
        milestoneInfoList.add(new MilestoneInfo("MS1", "05/21/2016"));
        milestoneInfoList.add(new MilestoneInfo("MS1", "05/21/2016"));

        // set data to EditText

        // create milestone list in run-time
        listView.setAdapter(new MilestoneInfoArrayAdapter(this, R.layout.milestone_row, milestoneInfoList));
        listView.setOnItemClickListener(this);

        // get the save button
        Button saveButton = (Button) findViewById(R.id.saveButton);
        if (saveButton != null) {
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // add data to db
                }
            });
        } else {
            toastShow("fail to get the save button!");
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // actually we don't need this one
    }

    private void toastShow(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
