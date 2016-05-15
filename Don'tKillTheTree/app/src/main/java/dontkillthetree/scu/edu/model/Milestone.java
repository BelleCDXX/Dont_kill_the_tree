package dontkillthetree.scu.edu.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Joey Zheng on 5/14/16.
 */
public class Milestone {
    private final int id;
    private String name;
    private Calendar dueDate;
    private boolean onTime;
    private boolean shown;

    /**
     * Use this constructor when it is a new milestone
     * @param name
     * @param dueDate
     */
    public Milestone(String name, Calendar dueDate) {
        Calendar currentDate = Calendar.getInstance();

        this.id = 0; //placeholder, NEED TO BE CHANGED
        this.name = name;
        this.dueDate = dueDate;
        this.onTime = currentDate.before(dueDate);
        this.shown = false;
    }

    /**
     * Use this constructor when it is constructed from the database
     * @param id
     * @param name
     * @param dueDate
     */
    public Milestone(int id, String name, String dueDate) throws ParseException{
        Calendar dueDateCalendar = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dueDateCalendar.setTime(simpleDateFormat.parse(dueDate));

        this.id = id;
        this.name = name;
        this.dueDate = dueDateCalendar;
        this.onTime = currentDate.before(dueDateCalendar);
        this.shown = false;
    }

    // getters and setters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        Calendar currentDate = Calendar.getInstance();

        this.dueDate = dueDate;
        this.onTime = currentDate.before(dueDate);
    }

    public boolean isOnTime() {
        return onTime;
    }

    public boolean isShown() {
        return shown;
    }

    public void setShown(boolean shown) {
        this.shown = shown;
    }
}
