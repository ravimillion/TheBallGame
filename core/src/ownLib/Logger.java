package ownLib;

import com.badlogic.gdx.Gdx;

/**
 * Created by ravi on 10.07.16.
 */
public class Logger {
    public void log(String TAG, String msg) {
        Gdx.app.log(TAG, msg);
    }

    public void log(String msg) {
        String TAG = "Logger";
        Gdx.app.log(TAG, msg);
    }
}
