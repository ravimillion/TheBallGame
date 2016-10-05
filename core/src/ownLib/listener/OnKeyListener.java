package ownLib.listener;

/**
 * Created by ravi on 11.07.16.
 */
public interface OnKeyListener {
    void onKeyUp(int keycode);

    void onKeyDown(int keycode);

    void onKeyTyped(char character);
}
