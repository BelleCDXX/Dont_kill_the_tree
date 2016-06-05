package dontkillthetree.scu.edu.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import dontkillthetree.scu.edu.UI.R;
import dontkillthetree.scu.edu.model.Milestone;

public abstract class Util {
    /**
     * Convert a given calendar into string
     * @param calendar
     * @return
     */
    public static final String calendarToString(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * Convert a given date String into Calendar
     * @param date
     * @return
     * @throws ParseException
     */
    public static final Calendar stringToCalendar(String date) throws ParseException{
        Calendar result = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        result.setTime(simpleDateFormat.parse(date));

        return result;
    }

    /**
     * Given a calendar, set the calendar to the soonest due date
     * For example, given 05/15/2016 20:47:55.789 will return 05/15/2016 23:59:59.999
     * @param calendar
     */
    public static final void toNearestDueDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    /**
     * This method determines if a given due date is on time.
     * @param dueDate
     * @param currentDate Give a specific date to compare, or null if you want to compare it to the current date
     * @return
     */
    public static final boolean isOnTime(Calendar dueDate, Calendar currentDate) {
        if (currentDate == null) {
            currentDate = Calendar.getInstance();
        }

        return currentDate.before(dueDate);
    }

    public static Milestone getMilestoneById(List<Milestone> milestones, long id) {
        for (Milestone milestone : milestones) {
            if (milestone.getId() == id) {
                return milestone;
            }
        }

        return null;
    }

    /**
     * This method cut the string shorter, if the length of string is longer than 20, it will be cut with length 17
     * @param s
     * @return String
     */
    public static String cutString(String s) {
        if (s.length() <= 20) {
            return s;
        } else {
            return s.substring(0, 16) + "...";
        }
    }

    /**
     * Crop R.drawable.list_item_background into several small pieces for a given height
     * @param bitmapDrawables A list of cropped images. The number of returned images depend on the original image height and the given height. n = (image height) / height
     * @param height The height for the list item
     * @param context
     */
    public static void cropListItemBackgroundImage(List<BitmapDrawable> bitmapDrawables, int height, Context context) {
        bitmapDrawables.clear();

        Bitmap bmp= BitmapFactory.decodeResource(context.getResources(), R.drawable.list_item_background);
        Bitmap resizedBitmap;
        int n = bmp.getHeight() / height;

        for (int i = 0; i < n; i++) {
            resizedBitmap = Bitmap.createBitmap(bmp, 0, i * height, bmp.getWidth(), height);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(resizedBitmap);
            bitmapDrawable.setTileModeX(Shader.TileMode.REPEAT);
            bitmapDrawables.add(bitmapDrawable);
        }
    }
}
