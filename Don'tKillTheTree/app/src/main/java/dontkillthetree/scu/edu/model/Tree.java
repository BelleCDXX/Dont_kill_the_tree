package dontkillthetree.scu.edu.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cheng11 on 5/19/16.
 */
public class Tree {

    private static int currentStage;
    private static int experience;

    /**
     * Use this constructor when install the app
     */
    public Tree (){
        currentStage = 0;
    }

    /**
     * if experience is enough to go to the next stage of the tree
     */
    public void toNextStage(){
        if(hasNextStage()){
            currentStage ++;
        }
    }

    //check if the currentStage is max number we allowed
    private boolean hasNextStage(){
        if (currentStage == Stages.getMaxStage()){
            return false;
        }else{
            return true;
        }
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
            currentStage++;
        }

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
            currentStage--;
        }

        return experience;
    }

    public String getCurrentImage(){
        return Stages.getImage(currentStage);
    }
}
