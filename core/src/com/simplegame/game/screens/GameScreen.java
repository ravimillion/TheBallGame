package com.simplegame.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;

/**
 * Created by ravi on 20.08.16.
 */
public abstract class GameScreen implements Screen{
    protected GameEntry game;
    protected float ORTHO_WIDTH = 0;
    protected float ORTHO_HEIGHT = 0;

    protected OrthographicCamera box2DCam;
    protected OrthographicCamera orthoCam;

    protected abstract void setScreenResolution();
    protected abstract void setCamera();
}
