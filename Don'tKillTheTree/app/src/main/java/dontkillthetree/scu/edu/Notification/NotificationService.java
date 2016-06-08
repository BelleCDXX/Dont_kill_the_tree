package dontkillthetree.scu.edu.Notification;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import dontkillthetree.scu.edu.UI.HomeActivity;
import dontkillthetree.scu.edu.UI.R;
import dontkillthetree.scu.edu.Util.Util;
import dontkillthetree.scu.edu.model.Milestone;
import dontkillthetree.scu.edu.model.Project;
import dontkillthetree.scu.edu.model.Projects;

/**
 * Created by xcw0420 on 5/30/16.
 */
public class NotificationService extends IntentService {
    public static final String EXTRA_TEXT = "text";
    public static final String EXTRA_TIME = "time";
    public static final int NOTIFICATION_ID = 1234;

    boolean stopped;

    public NotificationService(){
        super("NotificationService");
        stopped = false;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ArrayList<String> notifyText = intent.getStringArrayListExtra(EXTRA_TEXT);
        ArrayList<Calendar> notifyTime = (ArrayList<Calendar>) intent.getExtras().get(EXTRA_TIME);
        Log.i("CXiong-log",  "size of arrylist of milestones is " + notifyTime.size());

        synchronized(this){
            for(int i = 0; i < notifyTime.size(); ++i){
                try {
                    Calendar dueTime = notifyTime.get(i);
                    // milestones with the same duetime only send notification once
                    if(i < notifyTime.size() - 1){
                        for (int j = i + 1; j < notifyTime.size(); ++j){
                            if (notifyTime.get(j).equals(dueTime)){
                                ++i;
                            }
                            else break;
                        }
                    }
                    Calendar currentDate = Calendar.getInstance();
                    long waitTime = dueTime.getTimeInMillis() - currentDate.getTimeInMillis();
                    Log.i("CXiong-log", "currentdate: " +
                            Util.calendarToString(currentDate) +
                            ",  duetime: " +
                            Util.calendarToString(dueTime) +
                            ", waittime: " +
                            waitTime +
                            ", " +
                            (notifyTime.size() - i) +
                            " notis to come");
                    wait(waitTime);
                    //wait(10000); //wait 10s
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                if(stopped){
                    break;
                }

                Log.i("CXiong-log", "count end. ");
                String text = notifyText.get(i);
                showText(text); // send notification
            }
            Log.i("CXiong-log", "stopped notify service");
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showText(final String text){
        // when click on the notif main activity will be called, on book p563
        Intent intent = new Intent(this, HomeActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(HomeActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setContentText(text)
                .build();

        NotificationManager notificationManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManger.notify(NOTIFICATION_ID, notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopped = true;
    }
}
