package dontkillthetree.scu.edu.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import dontkillthetree.scu.edu.UI.R;

public class BackgroundMusic extends AsyncTask<Void, Void, Void> {
    private static Context mContext;
    private static BackgroundMusic mBackgroundMusic;
    private static MediaPlayer player;

    public static void startPlay(Context context) {
        mContext = context;
        mBackgroundMusic = new BackgroundMusic();
        mBackgroundMusic.execute();
    }

    public static void stopPlay(Context context) {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        player = MediaPlayer.create(mContext, R.raw.the_city_of_sky);
        player.setLooping(true); // Set looping
        player.setVolume(1.0f, 1.0f);
        player.start();
        return null;
    }
}
