package dontkillthetree.scu.edu.UI;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import dontkillthetree.scu.edu.UI.R;

public class AddProjectName extends ParentActivity {
    int year_x, month_x, day_x;
    static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_name);
    }



    private String getProjectName(){
        EditText inputText = ((EditText)findViewById(R.id.project_name));
        if (inputText != null && inputText.getText() != null) {
            return inputText.getText().toString();
        }
        else {
            return "";
        }
    }

    //set menu, add set due date icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_project_name_menu, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.set_due_date:
                String projectName = getProjectName();
                // .equals only support API 19 and above
                if(!Objects.equals(projectName, "")){
                    Intent intent = new Intent(AddProjectName.this, AddProjectDueDate.class);
                    intent.putExtra(ProjectDetailActivity.EXTRA_PROJECT_NAME, projectName);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(AddProjectName.this, "Please set the project name.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                return true;
        }
        return true;
    }
}
