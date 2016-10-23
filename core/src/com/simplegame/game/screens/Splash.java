package com.simplegame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.simplegame.game.MainMenuScreen;
import com.simplegame.game.levels.LevelOne;

import aurelienribon.tweenengine.TweenManager;
import ownLib.Own;

public class Splash implements Screen {
    private String TAG = "Splash";
    private SpriteBatch batch;
    private Texture loading = null;
    private Sprite splash = null;
    private float progress = 0;
    GameEntry gameEntry;
    private Vector2 loadingMsgPos = new Vector2(Own.device.getScreenWidth()/2 - 50, Own.device.getScreenHeight()/2);
    private TweenManager tweenManager;

    public Splash(GameEntry gameEntry) {
        this.gameEntry = gameEntry;
        batch = gameEntry.batch;
        Own.text.createFontForSplash();
    }

    @Override
    public void show() {
    }

    public boolean isLoading() {
        if (!Own.assets.update()) {
            Own.log(TAG, "Completed: " + Own.assets.getProgress());
            progress = Own.assets.getProgress();
            return true;
        }

        return false;
    }

    public void afterLoad() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Own.text.createFonts();
                Own.assets.createImageAssets();
//                gameEntry.setScreen(new MainMenuScreen(gameEntry));
                gameEntry.setScreen(new LevelOne(gameEntry));
            }
        });
    }

    @Override
    public void render(float delta) {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 0, 1f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        Own.text.showProgress(batch, (int)(progress*100), loadingMsgPos);
        if (!isLoading()){
            afterLoad();
            batch.end();
            gl.glClearColor(0, 0, 0, 1f);
            gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();
            Own.text.showProgress(batch, 100, loadingMsgPos);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
