package com.simplegame.game.levels.four;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.simplegame.game.levels.LevelScreen;
import com.simplegame.game.objects.Ball;
import com.simplegame.game.screens.GameEntry;
import com.simplegame.game.userdata.UserData;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

import ownLib.Own;

/**
 * Created by ravi on 23.10.16.
 */

public class LevelFour extends LevelScreen {

    SceneLoader sceneLoader;
    Ball ball;

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
        this.world = sceneLoader.world;
        this.debugRenderer = new Box2DDebugRenderer();

        ItemWrapper rootEntity = new ItemWrapper(sceneLoader.getRoot());
        ItemWrapper ball = rootEntity.getChild("ball");
        BallScript ballScript = new BallScript();
        ball.addScript(ballScript);
    }

    boolean isInitialized = false;

    @Override
    protected void renderLevel() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());

        if (!isInitialized) {
            setupLevel();
            isInitialized = true;
        }
        debugRenderer.render(this.world, box2DCam.combined);
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
