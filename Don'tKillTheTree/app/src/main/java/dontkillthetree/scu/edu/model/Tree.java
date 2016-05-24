package dontkillthetree.scu.edu.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dontkillthetree.scu.edu.database.DatabaseContract;
import dontkillthetree.scu.edu.event.MyTreeDatabaseOpListener;
import dontkillthetree.scu.edu.event.PropertyChangeEvent;

/**
 * Created by cheng11 on 5/19/16.
 */
public class Tree {

    private static int currentStage;
    private static int experience;
    private static Tree instance;
    private static MyTreeDatabaseOpListener myTreeDatabaseOpListener;

    private Tree (Context context){
        myTreeDatabaseOpListener = new MyTreeDatabaseOpListener(context);
        int[] results = myTreeDatabaseOpListener.onSelect();
        this.currentStage = results[0];
        this.experience = results[1];
    }

    public static Tree getInstance(Context context) {
        if (instance == null) {
            instance = new Tree(context);
        }

        return instance;
    }

    //getters and setters
    public int getCurrentStage(){
        return currentStage;
    }

    public int getExperience() {
        return experience;
    }

    /**
     * Increase the experience by a given amount. If the new experience reaches a new stage, advance the stage
     * @param amount A non-negative number
     * @return The new experience
     */
    public int increaseExperience(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("The increase amount needs to be non-negative.");
        }

        int maxStageExp = Stages.getStageMaxExp(Stages.getMaxStage());
        experience = experience + amount > maxStageExp ? maxStageExp : experience + amount;

        if (experience > Stages.getStageMaxExp(currentStage) && hasNextStage()) {
            currentStage = Stages.getStage(experience);
        }

        // update the database
        myTreeDatabaseOpListener.onUpdate(new PropertyChangeEvent(0, DatabaseContract.TreeEntry.COLUMN_NAME_EXPERIENCE, String.valueOf(experience)));
        myTreeDatabaseOpListener.onUpdate(new PropertyChangeEvent(0, DatabaseContract.TreeEntry.COLUMN_NAME_STAGE, String.valueOf(currentStage)));

        return experience;
    }

    /**
     * Decrease the experience by a given amount. If the nex experience reaches the higher end of a previous
     * stage, decrease the stage.
     * @param amount A non-negative number
     * @return The new experience
     */
    public int decreaseExperience(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("The decrease amount needs to be non-negative.");
        }

        experience = experience - amount == 0 ? 0 : experience - amount;

        if (currentStage > 0 && experience <= Stages.getStageMaxExp(currentStage - 1)) {
            currentStage = Stages.getStage(experience);
        }

        // update the database
        myTreeDatabaseOpListener.onUpdate(new PropertyChangeEvent(0, DatabaseContract.TreeEntry.COLUMN_NAME_EXPERIENCE, String.valueOf(experience)));
        myTreeDatabaseOpListener.onUpdate(new PropertyChangeEvent(0, DatabaseContract.TreeEntry.COLUMN_NAME_STAGE, String.valueOf(currentStage)));

        return experience;
    }

    public String getCurrentImage(){
        return Stages.getImage(currentStage);
    }

    public void dispose() {
        instance = null;
    }

    // private methods

    /**
     * Check if the tree has next stage
     * @return
     */
    private boolean hasNextStage(){
        if (currentStage == Stages.getMaxStage()){
            return false;
        }else{
            return true;
        }
    }
}
