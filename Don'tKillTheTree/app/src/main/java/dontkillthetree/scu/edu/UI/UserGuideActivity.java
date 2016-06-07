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
                .titlePage(R.drawable.calender_background, "Time of Tree", R.color.lightGreen)
                .basicPage(R.drawable.calender_background, "Create a new project", "More text.", R.color.lightGreen)
                .basicPage(R.drawable.calender_background, "Done a milestone", "More text.", R.color.lightGreen)
                .swipeToDismiss(true)
                .build();
    }


    public static String welcomeKey() {
        return "WelcomeScreen";
    }
}
