package dontkillthetree.scu.edu.UI;

import java.util.Calendar;

/**
 * Created by jasonzhang on 5/19/16.
 */
public class MilestoneInfo {
    private String milestoneName;
    private String milestoneDueDay;

    public MilestoneInfo(String milestoneName, String milestoneDueDay) {
        this.milestoneName = milestoneName;
        this.milestoneDueDay = milestoneDueDay;
    }

    public String getMilestoneName() {return milestoneName;}

    public String getMilestoneDueDay() {return milestoneDueDay;}
}
