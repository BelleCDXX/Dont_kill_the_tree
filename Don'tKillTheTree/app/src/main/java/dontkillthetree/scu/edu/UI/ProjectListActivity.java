package dontkillthetree.scu.edu.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
/*
** just UI here, not link to the project
 */
public class ProjectListActivity extends ParentActivity implements AdapterView.OnItemClickListener{
    private ListView projectListView;
    //test, can change later
    private ArrayList<String> testList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        projectListView = (ListView)findViewById(R.id.ProjectListView);

        //test adpter here, change later
        initialTestList();
        ArrayAdapter<String> testAdapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_expandable_list_item_1,testList);
        projectListView.setAdapter(testAdapter);
        projectListView.setOnItemClickListener( ProjectListActivity.this);
    }
    //initial testList, can delete later
   private void initialTestList(){
        testList.add("TestString1");
        testList.add("TestString2");
        testList.add("TestString3");
       Toast.makeText(ProjectListActivity.this, testList.get(0), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProjectListActivity.this);
        builder.setIcon(R.mipmap.ic_launcher)
                .setTitle("Choosing")
                .setMessage("What do you want to do next? ")
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // link to project detail activity

                        //Intent intent = new Intent(ProjectListActivity.this, ProjectDetailActivity.class);
                        //intent.putExtra("project_detail", currentProject);
                        //startActivity(intent);
                    }
                })
                .setNegativeButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //delete the milestone
                    }
                })
                .setCancelable(true);
        builder.create().show();
    }
}
