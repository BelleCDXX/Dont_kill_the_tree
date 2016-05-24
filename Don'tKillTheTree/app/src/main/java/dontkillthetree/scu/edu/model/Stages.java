package dontkillthetree.scu.edu.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Stages {
    private static final List<String> images = new ArrayList(Arrays.asList(
            "tree_stage_0.png",
            "tree_stage_1.png",
            "tree_stage_2.png",
            "tree_stage_3.png",
            "tree_stage_4.png",
            "tree_stage_5.png",
            "tree_stage_6.png"));
    private static final List<Integer> stageExperience = new ArrayList(Arrays.asList(
            100,
            200,
            300,
            400,
            500,
            600,
            700
    ));

    private Stages(){}

    /**
     * Get a 0-indexed stage number from a given experience
     * @param experience Experience, for example 201
     * @return 0-indexed stage number.
     */
    public static int getStage(int experience) {
        for (int i = 0; i < stageExperience.size(); i++) {
            if (experience <= stageExperience.get(i)) {
                return i;
            }
        }

        return stageExperience.size() - 1;
    }

    /**
     * Get the image of a given stage
     * @param stage 0-indexed stage
     * @return
     */
    public static String getImage(int stage) {
        return images.get(stage);
    }

    /**
     * Get the maximum experience of a given stage
     * @param stage 0-indexed stage
     * @return
     */
    public static int getStageMaxExp(int stage) {
        return stageExperience.get(stage);
    }

    /**
     * Get the maximum stage id (0-indexed). If there are 7 stages, then this returns 6.
     * @return
     */
    public static int getMaxStage() {
        return images.size() - 1;
    }
}
