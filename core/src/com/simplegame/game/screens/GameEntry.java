package com.simplegame.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.simplegame.game.AdHandler;
import com.simplegame.game.levels.four.LevelFour;

import ownLib.Own;

public class GameEntry extends Game {
    public SpriteBatch batch;
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
//        splashScreen = new Splash(this);
//        this.setScreen(splashScreen);
        this.setScreen(new LevelFour(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        Own.dispose();
    }
}
