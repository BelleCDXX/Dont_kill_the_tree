package dontkillthetree.scu.edu.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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

}
