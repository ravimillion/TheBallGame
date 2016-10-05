package com.simplegame.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ownLib.Own;

/**
 * Created by ravi on 11.09.16.
 */
public class Splash implements Screen {
    private String TAG = "Splash";
    private SpriteBatch batch;
    private Texture loading = null;
    private Sprite splash = null;
    private float progress = 0;

    public Splash(GameEntry gameEntry) {
        batch = gameEntry.batch;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Own.log("Progress: " + this.progress);
//        GL20 gl = Gdx.gl;
//        gl.glClearColor(1, 0, 0, 1f);
//        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        batch.begin();
////        batch.draw(splash, Own.device.getScreenWidth() / 2, Own.device.getScreenHeight() / 2);
//        batch.end();
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
