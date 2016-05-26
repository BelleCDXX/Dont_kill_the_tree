package dontkillthetree.scu.edu.UI;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import dontkillthetree.scu.edu.model.Stages;
import dontkillthetree.scu.edu.model.Tree;

public class HomeActivity extends ParentActivity implements AdapterView.OnItemSelectedListener{
    private Tree mTree;
    private Spinner spinner;
    private Handler mHandler;
    private int progressBarSpeed = 10;
    private String TAG = "SEN";
    private String[] items = { "Data-0", "Data-1", "Data-2", "Data-3", "Data-4", "Data-5", "Data-6", "Data-7" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTree = Tree.getInstance(this);

        //set tree image
        ImageView treeImage = (ImageView)findViewById(R.id.treeImage);
        Bitmap b = getBitmapFromAsset(this,mTree.getCurrentImage());
        treeImage.setImageBitmap(b);

        //set exp bar
        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // set the max exp of current stage
                int mCurrentStage = mTree.getCurrentStage();
                int mStageMaxExp = Stages.getStageMaxExp(mCurrentStage);
                mProgressBar.setMax(mStageMaxExp);

                // get the current exp
                int mCurrentExp = mTree.getExperience();
                int mProgressStatus = 0;
                while (mProgressStatus < mCurrentExp) {
                    // update the progress bar
                    mProgressBar.setProgress(mProgressStatus);
                    mProgressStatus += progressBarSpeed;
                    try {
                        Thread.sleep(200);
                    } catch (Exception ex) {
                        Log.i(TAG, ex.toString());
                    }
                }
            }
        }).start();


        //set spinner here
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                // android.R.layout.simple_list_item_1,
                items)
        );
        spinner.setOnItemSelectedListener(this);


    }


    //get bitmap from assets
    private static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Position: " + position + ", Data: " + items[position], Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(), "Nothing selected", Toast.LENGTH_SHORT).show();
    }

    //test button
    public void goToListButton(View view){
        Intent intent = new Intent(this,ProjectListActivity.class);
        startActivity(intent);
    }
}
