package dontkillthetree.scu.edu.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import dontkillthetree.scu.edu.model.Milestone;

/**
 * Created by Joey Zheng on 5/14/16.
 */
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
     * For example, given 05/15/2016 20:47:55.789 will return 06/16/2016 00:00:00.000
     * @param calendar
     */
    public static final void toNearestDueDate(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
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
}
