package ownLib.listener;

/**
 * Created by ravi on 11.07.16.
 */
public interface OnTouchListener {
    void touchDown(int screenX, int screenY, int pointer);

    void touchUp(int screenX, int screenY, int pointer);

    void touchDragged(int screenX, int screenY, int pointer);
}
