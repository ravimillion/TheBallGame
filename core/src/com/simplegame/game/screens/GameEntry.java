package com.simplegame.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.simplegame.game.AdHandler;

import ownLib.Own;

public class GameEntry extends Game {
    public static int WORLD_WIDTH = 573;
    public static int WORLD_HEIGHT = 32;
    public static SpriteBatch batch;
    private String TAG = "GameEntry";
    private Splash splashScreen;
    private AdHandler handler;

    public GameEntry(AdHandler handler) {
        this.handler = handler;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        handler.showAds(false);
        splashScreen = new Splash(this);
        this.setScreen(splashScreen);
//        this.setScreen(new LevelFour(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        Own.dispose();
        batch.dispose();
    }
}
