package dontkillthetree.scu.edu.UI;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // build action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Don't Kill the Tree");
        actionBar.setSubtitle("Group1");
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.action_bar_background));
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_show_tree:
                Intent intent1 = new Intent(this, HomeActivity.class);
                intent1.addFlags(intent1.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent1);
                break;
            case R.id.action_show_project_list:
                Intent intent2 = new Intent(this, ProjectListActivity.class);
                intent2.addFlags(intent2.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent2);
//                finish();
                break;
            case R.id.action_uninstall:
                Uri packageURI = Uri.parse("package:" + HomeActivity.class.getPackage().getName());
                Intent intent3 = new Intent(Intent.ACTION_DELETE, packageURI);
                startActivity(intent3);
                break;
            case android.R.id.home:
                Intent intent4 = new Intent(this, ProjectListActivity.class);
                intent4.addFlags(intent4.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent4);
//                finish();
                break;
            default:
                toastShow("unknown action ...");
        }

        return super.onOptionsItemSelected(item);
    }

    private void toastShow(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
