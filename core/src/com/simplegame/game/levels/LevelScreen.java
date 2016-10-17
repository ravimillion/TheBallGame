package com.simplegame.game.levels;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.simplegame.game.objects.WorldBoundry;
import com.simplegame.game.screens.GameScreen;
import com.simplegame.game.userdata.UserData;


public abstract class LevelScreen extends GameScreen implements InputProcessor {
    protected World world;
    private GameState gameState = GameState.RUNNING;
    protected WorldBoundry worldBoundry = null;
    protected Box2DDebugRenderer debugRenderer;
    protected float WORLD_WIDTH = 0;
    protected float WORLD_HEIGHT = 0;
    protected float gravityX = 0;
    protected float gravityY = 0;
    protected boolean isGameOver = false;

    protected abstract void setupLevel();

    public abstract void contactListener(UserData bodyA, UserData bodyB, float normalImpulse);

    protected void drawBorder() {
        worldBoundry = new WorldBoundry(WORLD_WIDTH, WORLD_HEIGHT);
        worldBoundry.updateWorldBoundry(WorldBoundry.BOTTOM, new Vector2(0, 4.3f), 0);

    }

    public OrthographicCamera getOrthoCam() {
        return orthoCam;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
