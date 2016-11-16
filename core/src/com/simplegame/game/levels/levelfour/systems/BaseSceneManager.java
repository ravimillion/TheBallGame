package com.simplegame.game.levels.levelfour.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.levels.levelfour.LevelFour;
import com.simplegame.game.levels.levelfour.component.Bounds;

import ownLib.Own;

public abstract class BaseSceneManager extends BaseSystem implements AfterSceneInit, InputProcessor {
    protected ComponentMapper<Bounds> boundsCm;
    protected ComponentMapper<Transform> transformCm;
    protected ComponentMapper<VisSprite> spriteCm;
    protected LevelFour game;
    protected CameraManager cameraManager;
    protected VisIDManager idManager;
    protected Vector3 unprojectVec = new Vector3();

    public BaseSceneManager(LevelFour game) {
        this.game = game;
    }

    protected Bounds getSpriteBounds(String id) {
        Entity entity = this.idManager.get(id);
        return (Bounds) this.boundsCm.get(entity);
    }

    public void afterSceneInit() {
        Own.io.addProcessor(this);
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }

    public void processSystem() {
    }
}
