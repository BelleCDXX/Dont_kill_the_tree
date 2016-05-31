package dontkillthetree.scu.edu.Notification;

import android.content.Context;
import android.content.Intent;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;

import dontkillthetree.scu.edu.UI.R;

/**
 * Created by xcw0420 on 5/30/16.
 */
public abstract class CreateNotifyIntent {
    static Intent notifyServiceIntent;

    public static void makeIntent(Context context){
        PrepareNotify prepareNotify = new PrepareNotify(context);

        ArrayList<String> notifyText = prepareNotify.getNotifyText();
        ArrayList<Calendar> notifyTime = prepareNotify.getNotifyTime();

        if(notifyServiceIntent != null){
            context.stopService(notifyServiceIntent);
            notifyServiceIntent = null;
        }

        notifyServiceIntent = new Intent(context, NotificationService.class);

        notifyServiceIntent.putExtra(NotificationService.EXTRA_TEXT, notifyText);
        notifyServiceIntent.putExtra(NotificationService.EXTRA_TIME, notifyTime);
        context.startService(notifyServiceIntent);
    }
}
