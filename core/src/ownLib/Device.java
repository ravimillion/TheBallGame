package ownLib;


import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by ravi on 10.07.16.
 */
public class Device {
    private String TAG = this.getClass().getName();
    public static final int DURATION_INFINITE = 1000 * 60 * 60; // 1 HOUR


    public int getAndroidVersion() {
        return Gdx.app.getVersion();
    }

    public ApplicationType getPlatformType() {
        return Gdx.app.getType();
    }

    public long getJavaHeapSize() {
        return Gdx.app.getJavaHeap();
    }

    public long getUsageHeapSize() {
        return Gdx.app.getNativeHeap();
    }

    public float getFrameDelta() {
        return Gdx.graphics.getDeltaTime();
    }

    public int getScreenHeight() {
        return Gdx.graphics.getHeight();
    }

    public int getScreenWidth() {
        return Gdx.graphics.getWidth();
    }

    public float getScreenRatio() {
        float h = this.getScreenHeight();
        float w = this.getScreenWidth();
        float ratio = h/w;
        Own.log(TAG, "Ratio: " + ratio);
        return ratio;
    }
    public int getFPS() {
        return Gdx.graphics.getFramesPerSecond();
    }

    public int getScreenLastX() {
        return Gdx.input.getX();
    }

    public int getScreenLastY() {
        return Gdx.input.getY();
    }

    public void startVibrate(int milli) {
        int duration = 0;

        if (milli > 0) {
            duration = milli;
        }
        Gdx.input.vibrate(duration);
    }

    public void cancelVibrate(int milli) {
        Gdx.input.cancelVibrate();
    }

    public Vector3 getAccMeter() throws OwnException {
        if (Own.device.getPlatformType() == ApplicationType.Android ||
                Own.device.getPlatformType() == ApplicationType.iOS) {

            Vector3 values = new Vector3();
            values.x = Gdx.input.getAccelerometerX();
            values.y = Gdx.input.getAccelerometerY();
            values.z = Gdx.input.getAccelerometerZ();
            return values;
        }

        throw new OwnException(OwnException.OWN_CODE_NO_ACCELEROMETER_FOUND, OwnException.OWN_MSG_NO_ACCELEROMETER_FOUND);
    }


}
