package com.simplegame.game.screens;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.runtime.component.Invisible;
import com.kotcrab.vis.runtime.component.Tint;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.GameController;
import com.simplegame.game.GameData;
import com.simplegame.game.TintAccessor;
import com.simplegame.game.components.Bounds;
import com.simplegame.game.savegame.LevelState;
import com.simplegame.game.systems.GameSaverSystem;

import java.util.HashMap;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import ownLib.Own;

public class MenuScreen extends BaseSystem implements AfterSceneInit, InputProcessor {
    ComponentMapper<Bounds> boundsCm;
    ComponentMapper<VisSprite> spriteCm;
    ComponentMapper<Transform> transformCm;
    ComponentMapper<Tint> tintCm;

    VisIDManager idManager;
    CameraManager cameraManager;
    TweenManager tweenManager = new TweenManager();
    String DISABLED_TINT = "4c4c4cff";
    GameController gameController;
    String[] buttons = {"idOne", "idTwo", "idThree", "idFacebook", "idTwitter", "idSettings", "idVolume", "idTutorial"};
    HashMap<String, Entity> entityMap = new HashMap<String, Entity>();
    HashMap<String, Bounds> boundsMap = new HashMap<String, Bounds>();

    boolean isMute = false;

    public MenuScreen(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void afterSceneInit() {
        for (int i = 0; i < buttons.length; i++) {
            String btnId = buttons[i];
            Entity entity = idManager.get(btnId);

            entityMap.put(btnId, entity);
            boundsMap.put(btnId, boundsCm.get(entity));
        }

        Own.io.addProcessor(this);
        configureButtonStates();
        // TWEEN MANAGER
        Tween.registerAccessor(Tint.class, new TintAccessor());

        Entity entity = idManager.get("idTutorial");
        Tween.set(tintCm.get(entity), TintAccessor.FADE_IN_OUT).target(0).start(tweenManager);
        Tween.to(tintCm.get(entity), TintAccessor.FADE_IN_OUT, 1).target(1f).start(tweenManager);

        entity = idManager.get("idOne");
        Tween.set(tintCm.get(entity), TintAccessor.FADE_IN_OUT).target(0).start(tweenManager);
        Tween.to(tintCm.get(entity), TintAccessor.FADE_IN_OUT, 1.5f).target(1f).start(tweenManager);

        entity = idManager.get("idTwo");
        Tween.set(tintCm.get(entity), TintAccessor.FADE_IN_OUT).target(0).start(tweenManager);
        Tween.to(tintCm.get(entity), TintAccessor.FADE_IN_OUT, 2f).target(1f).start(tweenManager);

        entity = idManager.get("idThree");
        Tween.set(tintCm.get(entity), TintAccessor.FADE_IN_OUT).target(0).start(tweenManager);
        Tween.to(tintCm.get(entity), TintAccessor.FADE_IN_OUT, 2.5f).target(1f).start(tweenManager);
    }

    private void configureButtonStates() {
        FileHandle fileHandle = Gdx.files.local(GameSaverSystem.GAME_STATE_PERSIST_FILE);
        Json json = new Json();

        setDisabled(GameData.MUTE, "idVolume");

        if (fileHandle.exists()) {
            HashMap<String, LevelState> levelStates = json.fromJson(HashMap.class, fileHandle);
            LevelState levelTutorial = levelStates.get(GameData.ID_LEVEL_TUTORIAL);
            LevelState levelOne = levelStates.get(GameData.ID_LEVEL_ONE);
            LevelState levelTwo = levelStates.get(GameData.ID_LEVEL_TWO);
            LevelState levelThree = levelStates.get(GameData.ID_LEVEL_THREE);

            setDisabled(true, "idOne");
            setDisabled(true, "idTwo");
            setDisabled(true, "idThree");

            boolean isNew = true;
            if (levelTutorial.levelCompletionState.equals(GameData.LEVEL_IN_PROGRESS)) {
                setDisabled(false, "idOne");
                isNew = false;
            }
            if (levelOne.levelCompletionState.equals(GameData.LEVEL_FINISHED)) {
                setDisabled(false, "idTwo");
                isNew = false;
            }
//
            if (levelTwo.levelCompletionState.equals(GameData.LEVEL_FINISHED)) {
                setDisabled(false, "idThree");
                isNew = false;
            }
//
//            if (levelState.levelThree == GameData.LEVEL_FINISHED) {
//                Own.log("Game Finished");
//            }

            if (isNew) {
                setDisabled(false, "idTutorial");
                setDisabled(true, "idOne");
                setDisabled(true, "idTwo");
                setDisabled(true, "idThree");
            }
        } else {
            Own.log("Loading button state from state");
            setDisabled(false, "idTutorial");
            setDisabled(true, "idOne");
            setDisabled(true, "idTwo");
            setDisabled(true, "idThree");
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPoint = new Vector3();
        touchPoint.set(screenX, screenY, 0);
        cameraManager.getCamera().unproject(touchPoint);

        String buttonId = getPressedButtonId(touchPoint);
        if (buttonId == null) return false;

        switch (buttonId) {
            case "idTutorial":
                if (isDisabled(buttonId)) return false;
                this.dispose();
                gameController.loadLevelTutorial();
                break;
            case "idOne":
                if (isDisabled(buttonId)) return false;
                this.dispose();
                gameController.loadLevelOneScene();
                break;
            case "idTwo":
                if (isDisabled(buttonId)) return false;
                this.dispose();
                gameController.loadLevelTwoScene();
                break;
            case "idThree":
                if (isDisabled(buttonId)) return false;
                this.dispose();
                gameController.loadLevelThreeScene();
                break;
            case "idSettings":
                FileHandle fileHandle = Gdx.files.local(GameSaverSystem.GAME_STATE_PERSIST_FILE);
                if (fileHandle.exists()) {
                    if (fileHandle.delete()) Own.log("Deleted file");
                }
                break;
            case "idVolume":
                GameData.MUTE = !GameData.MUTE;
                setDisabled(GameData.MUTE, buttonId);
                break;
            default:
                Own.log(buttonId + "pressed");
        }

        return false;
    }

    private boolean isDisabled(String buttonId) {
        Entity entity = entityMap.get(buttonId);
        Tint tint = tintCm.get(entity);
        return tint.getTint().toString().equals(DISABLED_TINT);
    }

    private void setDisabled(boolean disable, String buttonId) {
        Entity entity = entityMap.get(buttonId);
        Tint tint = tintCm.get(entity);
        if (disable) {
            tint.set(0.3f, 0.3f, 0.3f, 1);
        } else {
            tint.set(1, 1, 1, 1);
        }
    }

    private String getPressedButtonId(Vector3 touchPoint) {
        Entity entity;
        Bounds bounds;

        String buttonId = null;

        for (int i = 0; i < buttons.length; i++) {
            entity = entityMap.get(buttons[i]);
            bounds = boundsMap.get(buttons[i]);
            // if button is visible and touched
            if (entity.getComponent(Invisible.class) == null && bounds.contains(touchPoint.x, touchPoint.y)) {
                buttonId = buttons[i];
            }
        }

        return buttonId;
    }


    @Override
    protected void processSystem() {
        tweenManager.update(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
    }

    @Override
    public void dispose() {
        Own.io.removeProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.NUM_0:
                this.dispose();
                gameController.loadLevelTutorial();
                break;
            case Keys.NUM_1:
                this.dispose();
                gameController.loadLevelOneScene();
                break;
            case Keys.NUM_2:
                this.dispose();
                gameController.loadLevelTwoScene();
                break;
            case Keys.NUM_3:
                this.dispose();
                gameController.loadLevelThreeScene();
                break;
            case Keys.Q:
                this.dispose();
                gameController.exitApp();
                break;
        }
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
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
}
