package com.simplegame.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.simplegame.game.AdHandler;
import com.simplegame.game.MainMenuScreen;
import com.simplegame.game.levels.LevelThree;
import com.simplegame.game.levels.LevelTwo;

import ownLib.Own;

public class GameEntry extends Game {
    private String TAG = "GameEntry";
    private Splash splashScreen;
    private float progress = 0.0f;


    public SpriteBatch batch;
    private AdHandler handler = null;

    public GameEntry(AdHandler handler) {
        this.handler = handler;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
//        splashScreen = new Splash(this);
//        this.setScreen(splashScreen);

        if (loadGame()) {
            afterLoad();
        }
    }

    public boolean loadGame() {
        while (!Own.assets.update()) {
//            splashScreen.setProgress(Own.assets.getProgress());
        }

        return true;
    }

    public void afterLoad() {
        handler.showAds(false);
        Own.text.createFonts();
        Own.assets.createImageAssets();
        setScreen(new MainMenuScreen(this));
//        setScreen(new LevelThree(this));
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
