package dontkillthetree.scu.edu.Calender;

/**
 * Created by xcw0420 on 5/19/16.
 */
import java.util.ArrayList;

public class CalendarCollection {
    public String date="";
    public String event_message="";

    public static ArrayList<CalendarCollection> date_collection_arr;
    public CalendarCollection(String date,String event_message){

        this.date=date;
        this.event_message=event_message;

    }

}
