package dontkillthetree.scu.edu.model;

import android.content.Context;
import android.media.MediaPlayer;

import dontkillthetree.scu.edu.UI.R;

/**
 * Created by jasonzhang on 5/27/16.
 */
public class Audio {
    public static final int NORMAL_CLICK_SOUND = R.raw.normal_click_sound;
    public static final int URGENT_CLICK_SOUND = R.raw.urgent_click_sound;
    private static MediaPlayer mMediaPlayer;

    public static void makeClickSound(Context context) {
        mMediaPlayer = MediaPlayer.create(context, NORMAL_CLICK_SOUND);
        mMediaPlayer.start();
    }

    public static void stopClickSound() {
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
}
