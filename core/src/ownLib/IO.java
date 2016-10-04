package ownLib;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;

import ownLib.listener.OnKeyListener;
import ownLib.listener.OnTouchListener;

public class IO implements InputProcessor {
    private String TAG = "IO";
    private ArrayList<OnTouchListener> onTouchListener = new ArrayList<OnTouchListener>();
    private ArrayList<OnKeyListener> onKeyListener = new ArrayList<OnKeyListener>();

    public IO() {
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
        if (Own.device.getPlatformType() == Application.ApplicationType.Desktop) {
            Gdx.input.setCursorCatched(true);
        }
    }

    public void setOnTouchListener(ownLib.listener.OnTouchListener touchListener) {
        this.onTouchListener.add(touchListener);
    }

    public void setOnKeyListener(OnKeyListener keyListener) {
        this.onKeyListener.add(keyListener);
    }

    @Override
    public boolean keyDown(int keycode) {
        for (int i = 0; i < this.onKeyListener.size(); i++) {
            this.onKeyListener.get(i).onKeyDown(keycode);
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        for (int i = 0; i < this.onKeyListener.size(); i++) {
            this.onKeyListener.get(i).onKeyUp(keycode);
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        for (int i = 0; i < this.onKeyListener.size(); i++) {
            this.onKeyListener.get(i).onKeyTyped(character);
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        for (int i = 0; i < this.onTouchListener.size(); i++) {
            this.onTouchListener.get(i).touchDown(screenX, screenY, pointer);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        for (int i = 0; i < this.onTouchListener.size(); i++) {
            this.onTouchListener.get(i).touchUp(screenX, screenY, pointer);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        for (int i = 0; i < this.onTouchListener.size(); i++) {
            this.onTouchListener.get(i).touchDragged(screenX, screenY, pointer);
        }
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
