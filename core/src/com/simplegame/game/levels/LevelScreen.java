package com.simplegame.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.simplegame.game.objects.WorldBoundry;
import com.simplegame.game.screens.GameScreen;
import com.simplegame.game.userdata.UserData;

import ownLib.Own;
import ownLib.controls.ControlsLayer;


public abstract class LevelScreen extends GameScreen implements InputProcessor {
    private GameState gameState = GameState.RUNNING;

    protected World world;
    protected WorldBoundry worldBoundry;
    protected ControlsLayer controlsLayer;
    protected Box2DDebugRenderer debugRenderer;
    protected JsonValue store = new JsonReader().parse(Gdx.files.internal("json/leveldata.json"));

    protected float WORLD_WIDTH = 0;
    protected float WORLD_HEIGHT = 0;

    protected float gravityX = 0;
    protected float gravityY = 0;

    protected abstract void setCamera();

    protected abstract void setupLevel();

    protected abstract void renderLevel();

    protected abstract void updateWorld();

    public abstract void contactListener(UserData bodyA, UserData bodyB, float normalImpulse);

    protected void drawBorder() {
        worldBoundry = new WorldBoundry(WORLD_WIDTH, WORLD_HEIGHT);
        worldBoundry.updateWorldBoundry(WorldBoundry.BOTTOM, new Vector2(0, 6f), 0);

    }

    protected void drawGround(String floorId) {
        //        update the ground
        Texture ground = Own.assets.getTexture(floorId);

        float imageRatio = 5f / ground.getHeight();
        for (int i = 0; i < WORLD_WIDTH; i += ground.getWidth() * imageRatio) {
            game.batch.draw(ground, i, 1, ground.getWidth() * imageRatio, 5f);
        }
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
