package com.jauntymarble.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.jauntymarble.game.GameController;
import com.jauntymarble.game.OwnSceneLoader;
import com.kotcrab.vis.runtime.scene.Scene;
import com.kotcrab.vis.runtime.scene.SceneLoader.SceneParameter;

import ownLib.Own;

public class LoadingScreen implements Screen {
    GameController gameController;
    private SpriteBatch spriteBatch;
    private GameEntry gameEntry;
    // scene loading parameter
    private OwnSceneLoader manager;
    private String scenePath;
    private SceneParameter sceneParameter;
    private OrthographicCamera camera;

    public LoadingScreen(GameEntry gameEntry, GameController gameController, SpriteBatch spriteBatch, OwnSceneLoader manager) {
        this.gameEntry = gameEntry;
        this.spriteBatch = spriteBatch;
        this.manager = manager;
        this.gameController = gameController;
        this.camera = new OrthographicCamera(Own.device.getScreenWidth(), Own.device.getScreenHeight());
    }

    @Override
    public void show() {
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        this.manager.loadSceneNow(this.scenePath, this.sceneParameter);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(camera.combined);

        if (isLoading()) {
            spriteBatch.begin();
            Own.text.showProgress(spriteBatch, "Loading... " + (int) (manager.getProgress() * 100) + " %", new Vector2(Own.device.getScreenWidth() / 2, Own.device.getScreenHeight() / 2));
            spriteBatch.end();
        } else {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    dispose();
                    Scene scene = manager.getScene();
                    gameController.setScene(scene);
                    gameEntry.setScreen(gameController);
                    // start the music for the level
                    gameController.playMusic();
                }
            });
        }
    }

    public boolean isLoading() {
        return !manager.update();
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
        Own.log("Disposing Splash Screen");
    }

    public void startLoading(String scenePath, SceneParameter sceneParameter) {
        this.scenePath = scenePath;
        this.sceneParameter = sceneParameter;

        this.gameEntry.setScreen(this);
    }
}
