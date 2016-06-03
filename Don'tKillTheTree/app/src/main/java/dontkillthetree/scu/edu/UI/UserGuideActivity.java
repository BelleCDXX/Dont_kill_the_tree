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
                .titlePage(R.drawable.huan_liu, "Title")
                .basicPage(R.drawable.long_hair_huan_liu, "Header", "More text.", R.color.colorPrimary)
                .basicPage(R.drawable.long_hair_huan_liu, "Lorem ipsum", "dolor sit amet.")
                .swipeToDismiss(true)
                .build();
    }


    public static String welcomeKey() {
        return "WelcomeScreen";
    }
}
