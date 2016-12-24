package com.simplegame.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.simplegame.game.AdHandler;
import com.simplegame.game.GameController;

import ownLib.Own;

//import static com.simplegame.game.screens.GameEntry.batch;


public class GameEntry extends Game {
//    public static int WORLD_WIDTH = 573;
//    public static int WORLD_HEIGHT = 32;
    public static SpriteBatch spriteBatch;
    private String TAG = "GameEntry";
    private Splash splashScreen;
    private AdHandler handler;
    private GameController gameController;

    public GameEntry(AdHandler handler) {
        this.handler = handler;
    }

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        handler.showAds(false);

//        GameController gameController = new GameController(this, spriteBatch);
//        this.setScreen(gameController);
//        gameController.loadMenuScene();
//        gameController.loadSplashScreen();

        splashScreen = new Splash(this, spriteBatch);
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
        spriteBatch.dispose();
    }

    public void finishLoading() {
        this.gameController = new GameController(this, spriteBatch);
        this.setScreen(this.gameController);;
    }
}
