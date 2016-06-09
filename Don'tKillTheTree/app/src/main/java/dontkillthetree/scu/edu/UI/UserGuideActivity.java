package dontkillthetree.scu.edu.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stephentuso.welcome.WelcomeScreenBuilder;
import com.stephentuso.welcome.ui.WelcomeActivity;
import com.stephentuso.welcome.util.WelcomeScreenConfiguration;

public class UserGuideActivity extends WelcomeActivity {

    @Override
    protected WelcomeScreenConfiguration configuration() {
        return new WelcomeScreenBuilder(this)
                .theme(R.style.WelcomeScreenTheme_Light)
                .defaultBackgroundColor(R.color.default_background_color)
                .titlePage(R.drawable.calender_background, "Don't kill the tree", R.color.lightGreen)
                .basicPage(R.drawable.small_tree, "Start and own your tree!", "",R.color.lightGreen)
                .basicPage(R.drawable.create_new_project, "How to start", "Click + to create a new project.", R.color.lightGreen)
                .basicPage(R.drawable.view_project_detail, "How to edit", "Click the milestone name or due date to edit.", R.color.lightGreen)
                .basicPage(R.drawable.full_tree, "Complete your milestones, your tree will grow up!", "", R.color.lightGreen)
                .swipeToDismiss(true)
                .build();
    }


    public static String welcomeKey() {
        return "WelcomeScreen";
    }
}
