package dontkillthetree.scu.edu.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cheng11 on 5/19/16.
 */
public class Tree {
    private List<String> images;
    private static int currentStage;
    final private int maxStage = 5;
    /**
     * Use this constructor when install the app
     */
    public Tree (){
        images = new ArrayList<String>(Arrays.asList("tree_stage_0.png",
                "tree_stage_1.png",
                "tree_stage_2.png",
                "tree_stage_3.png",
                "tree_stage_4.png",
                "tree_stage_5.png",
                "tree_stage_6.png"));
        currentStage = 0;
    }

    //if experience is enough to go to the next stage of the tree
    public void nextStage(){
        if(checkStage()){
        currentStage ++;
        }
    }

    //check if the currentStage is max number we allowed
    private boolean checkStage(){
        if (currentStage == maxStage){
            return false;
        }else{
            return true;
        }
    }

    //get currentStge
    public int getCurrentStage(){
        return currentStage;
    }

    //get current tree view of this stage
    public String getCurrentImage(){
        return images.get(currentStage);
    }
}
