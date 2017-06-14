package com.jauntymarble.game.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.jauntymarble.game.GameController;
import com.jauntymarble.game.GameData;
import com.jauntymarble.game.components.Bounds;
import com.kotcrab.vis.runtime.component.Invisible;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

import java.util.HashMap;

import ownLib.Own;


public class ControlsSystem extends BaseSystem implements AfterSceneInit, InputProcessor {
    private ComponentMapper<Bounds> boundsCm;
    private ComponentMapper<VisSprite> spriteCm;
    private ComponentMapper<Transform> transformCm;

    private VisIDManager idManager;
    private PlayerSystem playerSystem;
    private CameraManager cameraManager;
    private SoundSystem soundSystem;

    private GameController gameController;

    private HashMap<String, Float> posMap = new HashMap<String, Float>();
    private HashMap<String, Entity> entityMap = new HashMap<String, Entity>();
    private HashMap<String, Bounds> boundsMap = new HashMap<String, Bounds>();
    private String[] buttons = {"idCongrats", "idPause", "idResume", "idReady", "idQuit", "idRestart", "idGameOver"};

    // variable cache
    private OrthographicCamera camera;
    private Entity entity;

    public ControlsSystem(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void afterSceneInit() {
        camera = cameraManager.getCamera();

        for (int i = 0; i < buttons.length; i++) {
            String btnId = buttons[i];
            Entity entity = idManager.get(btnId);

            entityMap.put(btnId, idManager.get(btnId));
            boundsMap.put(btnId, boundsCm.get(entity));
            posMap.put(btnId, transformCm.get(entity).getX() - camera.viewportWidth / 2);
        }

        Own.io.addProcessor(this);
        setState(GameData.READY);
    }

    public void setState(int state) {
        playerSystem.state = state;

        hide("*");
        switch (state) {
            case GameData.PAUSED:
                show("idResume");
                show("idQuit");
                break;
            case GameData.RUNNING:
                show("idPause");
                show("idRestart");
                break;
            case GameData.READY:
                show("idReady");
                break;
            case GameData.LEVEL_END:
                show("idCongrats");
                break;
            case GameData.RESTART_LEVEL:
                // possiblity to show an ad here
                this.gameController.showInterstitialAd();
                show("idPause");
                show("idRestart");
                playerSystem.respawnPlayer();
                break;
            case GameData.GAME_OVER:
                show("idGameOver");
                break;
            case GameData.QUIT:
                gameController.notify(GameData.QUIT);
            default:
                break;
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return false;
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

    public int getNextState(String buttonId) {

        switch (buttonId) {
            case "idPause":
                return GameData.PAUSED;
            case "idReady":
                return GameData.RUNNING;
            case "idQuit":
                return GameData.QUIT;
            case "idResume":
                return GameData.RUNNING;
            case "idCongrats":
                return GameData.QUIT;
            case "idRestart":
                return GameData.RESTART_LEVEL;
            case "idGameOver":
                return GameData.RESTART_LEVEL;
            default:
                Own.log("Error: No next CURRENT_LEVEL info found: " + buttonId);
                return GameData.RUNNING;
        }
    }


    @Override
    protected void processSystem() {
        if (GameController.CURRENT_LEVEL.equals(GameData.ID_LEVEL_TUTORIAL)) return;

        // update controls position.

        float cameraX = camera.position.x;

        for (int i = 0, len = buttons.length; i < len; i++) {
            entity = entityMap.get(buttons[i]);

            float newX = cameraX + posMap.get(buttons[i]);

            boundsCm.get(entity).setX(newX);
            transformCm.get(entity).setX(newX);
        }
    }

    private void show(String buttonId) {
        if (buttonId.equals("*")) {
            for (String btnId : buttons) {
                idManager.get(btnId).edit().remove(new Invisible());
            }
        } else {
            idManager.get(buttonId).edit().remove(new Invisible());
        }
    }

    private void hide(String buttonId) {
        if (buttonId.equals("*")) {
            for (String btnId : buttons) {
                idManager.get(btnId).edit().add(new Invisible());
            }
        } else {
            idManager.get(buttonId).edit().add(new Invisible());
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPoint = new Vector3();
        touchPoint.set(screenX, screenY, 0);
        cameraManager.getCamera().unproject(touchPoint);

        String buttonId = getPressedButtonId(touchPoint);
        Own.log("Pressed: " + buttonId);
        if (buttonId != null) setState(getNextState(buttonId));

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
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ENTER:
                setState(getNextState("idReady"));
                break;
            case Input.Keys.SPACE:
                setState(getNextState("idPause"));
                break;
            case Input.Keys.Q:
                setState(getNextState("idQuit"));
                break;
            case Input.Keys.R:
                setState(getNextState("idRestart"));
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
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void dispose() {
        Own.io.removeProcessor(this);
    }
}
