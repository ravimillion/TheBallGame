package ownLib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Storage {

    private static Preferences prefs = null;
    private static String prefFileName = null;

    public void flushAll() {
        if (prefs != null) prefs.flush();
    }

    public void saveStringValue(String fileName, String key, String value, boolean doFlush) {
        if (!prefFileName.equals(fileName)) {
            prefs = Gdx.app.getPreferences(fileName);
        }

        prefs.putString(key, value);
        if (doFlush) prefs.flush();
    }

    public void saveIntValue(String fileName, String key, int value, boolean doFlush) {
        if (!prefFileName.equals(fileName)) {
            prefs = Gdx.app.getPreferences(fileName);
        }
        prefs.putInteger(key, value);
        if (doFlush) prefs.flush();
    }

    public void saveBoolValue(String fileName, String key, boolean value, boolean doFlush) {
        if (!prefFileName.equals(fileName)) {
            prefs = Gdx.app.getPreferences(fileName);
        }
        prefs.putBoolean(key, value);
        if (doFlush) prefs.flush();
    }

    public void saveFloatValue(String fileName, String key, float value, boolean doFlush) {
        if (!prefFileName.equals(fileName)) {
            prefs = Gdx.app.getPreferences(fileName);
        }
        prefs.putFloat(key, value);
        if (doFlush) prefs.flush();
    }

    public void saveLongValue(String fileName, String key, long value, boolean doFlush) {
        if (!prefFileName.equals(fileName)) {
            prefs = Gdx.app.getPreferences(fileName);
        }
        prefs.putLong(key, value);
        if (doFlush) prefs.flush();
    }


    // Read preferences
    public String getStringValue(String fileName, String key, String def) {
        if (!prefFileName.equals(fileName)) {
            prefs = Gdx.app.getPreferences(fileName);
        }
        return prefs.getString(key, def);
    }

    public int getIntValue(String fileName, String key, int def) {
        if (!prefFileName.equals(fileName)) {
            prefs = Gdx.app.getPreferences(fileName);
        }
        return prefs.getInteger(key, def);
    }

    public boolean getBoolValue(String fileName, String key, boolean def) {
        if (!prefFileName.equals(fileName)) {
            prefs = Gdx.app.getPreferences(fileName);
        }
        return prefs.getBoolean(key, def);
    }

    public float getFloatValue(String fileName, String key, float def) {
        if (!prefFileName.equals(fileName)) {
            prefs = Gdx.app.getPreferences(fileName);
        }
        return prefs.getFloat(key, def);
    }

    public long getLongValue(String fileName, String key, long def) {
        if (!prefFileName.equals(fileName)) {
            prefs = Gdx.app.getPreferences(fileName);
        }
        return prefs.getLong(key, def);
    }
}
