package com.simplegame.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.simplegame.game.screens.GameEntry;
import com.simplegame.game.userdata.UserData;
import com.uwsoft.editor.renderer.SceneLoader;

import ownLib.Own;

/**
 * Created by ravi on 23.10.16.
 */

public class LevelFour extends LevelScreen{

    SceneLoader sceneLoader;

    public LevelFour(GameEntry gameEntry) {
        this.game = gameEntry;

        setScreenResolution();
    }

    @Override
    protected void setScreenResolution() {
        ORTHO_WIDTH = Own.device.getScreenWidth();
        ORTHO_HEIGHT = Own.device.getScreenHeight();
        setCamera();
    }

    @Override
    protected void setCamera() {
        WORLD_HEIGHT = 32f;
        WORLD_WIDTH = 480f;

        orthoCam = new OrthographicCamera(ORTHO_WIDTH, ORTHO_HEIGHT);
        orthoCam.position.set(orthoCam.viewportWidth / 2, orthoCam.viewportHeight / 2, 0);
        orthoCam.update();

        box2DCam = new OrthographicCamera(WORLD_HEIGHT/Own.device.getScreenRatio(), WORLD_HEIGHT);
        box2DCam.position.set(box2DCam.viewportWidth / 2, box2DCam.viewportHeight / 2 + 1, 0);
        box2DCam.update();

        Viewport viewport = new FitViewport(WORLD_HEIGHT/Own.device.getScreenRatio(), WORLD_HEIGHT, box2DCam); // this should be the size of camera in WORLD units. make sure you check that in editor first.
        sceneLoader = new SceneLoader(); // default scene loader loads allr esources from default RM as usual.
        sceneLoader.loadScene("MainScene", viewport);
    }

    @Override
    protected void setupLevel() {

    }

    @Override
    protected void renderLevel() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());
    }

    @Override
    protected void updateWorld() {

    }

    @Override
    public void contactListener(UserData bodyA, UserData bodyB, float normalImpulse) {

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        renderLevel();
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
