package dontkillthetree.scu.edu.Notification;

import android.content.Context;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import dontkillthetree.scu.edu.model.Milestone;
import dontkillthetree.scu.edu.model.Project;
import dontkillthetree.scu.edu.model.Projects;

/**
 * Created by xcw0420 on 5/30/16.
 */
public class PrepareNotify {

    private ArrayList<String> notifyText;
    private ArrayList<Calendar> notifyTime;
    private Context context;

    public PrepareNotify(Context context){
        this.context = context;
        notifyText = new ArrayList<>();
        notifyTime = new ArrayList<>();
        updateMilestonesForNotify();
    }

    public ArrayList<String> getNotifyText(){
        return notifyText;
    }

    public ArrayList<Calendar> getNotifyTime(){
        return notifyTime;
    }

    // get on-time and un-complete milestones to create notification
    public void updateMilestonesForNotify(){
        List<Project> projects = new ArrayList<>();
        List<Milestone> milestonesForNotify = new ArrayList<>();
        notifyText.clear();
        notifyTime.clear();
        //Calendar nearest = null;
        //int count = 0;


        try {
            Projects.getAllProjects(context);
            projects = Projects.projects;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(projects.size()< 1){
            return;
        }
        //get ontime project's number
        List<Milestone> milestones;
        for(Project project:projects){
            milestones = project.getMilestonesForNotify();
            if(!(milestones.size() < 1)) {
                //count ++;
                milestonesForNotify.addAll(milestones);
            }
        }

        if(milestonesForNotify.size()<1){
            return;
        }

        Collections.sort(milestonesForNotify);

        //Log.i("JCheng","size: " + currentProjects.size());

        //Log.i("Jc","nearst: "+Util.calendarToString(nearest));
        //get upcoming milestones
        for (Milestone milestone: milestonesForNotify){
            //String s= milestone.getName()+" is over due.";
            String s = "Your tree is dying";
            notifyText.add(s);
            notifyTime.add(milestone.getDueDate());
        }
    }
}
