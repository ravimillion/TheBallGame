package com.simplegame.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.simplegame.game.AdHandler;
import com.simplegame.game.MainMenuScreen;

import ownLib.Own;

public class GameEntry extends Game {
    public SpriteBatch batch;
    private String TAG = "GameEntry";
    private Splash splashScreen;
    private float progress = 0.0f;
    private AdHandler handler = null;

    public GameEntry(AdHandler handler) {
        this.handler = handler;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        handler.showAds(false);
        splashScreen = new Splash(this);
        this.setScreen(splashScreen);

//
//        if (isLoading()) {
//            afterLoad();
//        }
    }

//    public boolean isLoading() {
//        while (!Own.assets.update()) {
//            Own.log(TAG, "Completed: " + Own.assets.getProgress());
//            splashScreen.setProgress(Own.assets.getProgress());
//        }
//
//        return true;
//    }

//    public void afterLoad() {
//        handler.showAds(false);
//        Own.text.createFonts();
//        Own.assets.createImageAssets();
//        setScreen(new MainMenuScreen(this));
//    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        Own.dispose();
    }
}
