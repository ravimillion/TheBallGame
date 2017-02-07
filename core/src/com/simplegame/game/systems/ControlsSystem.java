package com.simplegame.game.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.kotcrab.vis.runtime.component.Invisible;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.GameController;
import com.simplegame.game.GameData;
import com.simplegame.game.components.Bounds;

import java.util.HashMap;

import ownLib.Own;


public class ControlsSystem extends BaseSystem implements AfterSceneInit, InputProcessor {
    private ComponentMapper<Bounds> boundsCm;
    private ComponentMapper<VisSprite> spriteCm;
    private ComponentMapper<Transform> transformCm;

    private VisIDManager idManager;
    private PlayerSystem playerSystem;
    private CameraManager cameraManager;

    private GameController gameController;

    private HashMap<String, Float> posMap = new HashMap<String, Float>();
    private HashMap<String, Entity> entityMap = new HashMap<String, Entity>();
    private HashMap<String, Bounds> boundsMap = new HashMap<String, Bounds>();
    private String[] buttons = {"idCongrats", "idPause", "idResume", "idReady", "idQuit", "idRestart", "idGameOver"};

    public ControlsSystem(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void afterSceneInit() {
        for (int i = 0; i < buttons.length; i++) {
            String btnId = buttons[i];
            Entity entity = idManager.get(btnId);

            entityMap.put(btnId, idManager.get(btnId));
            boundsMap.put(btnId, boundsCm.get(entity));
            posMap.put(btnId, transformCm.get(entity).getX() - cameraManager.getCamera().viewportWidth / 2);
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
                gameController.notify(GameData.LEVEL_END);
                break;
            case GameData.RESTART_LEVEL:
                gameController.notify(GameData.RESTART_LEVEL);
                break;
            case GameData.QUIT:
                gameController.notify(GameData.QUIT);
            default:
                break;
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPoint = new Vector3();
        touchPoint.set(screenX, screenY, 0);
        cameraManager.getCamera().unproject(touchPoint);

        String buttonId = getPressedButtonId(touchPoint);
        if (buttonId != null) setState(getNextState(buttonId));

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
            case "idRestart":
                return GameData.RESTART_LEVEL;
            default:
                Own.log("Error: No next level info found: " + buttonId);
                return GameData.RUNNING;
//                break;
        }

//        return -1;
    }

    public void setControlsPosition() {
        Entity entity;
        Bounds bounds;
        Transform transform;

        Object[] keys = entityMap.keySet().toArray();

        for (Object id : keys) {
            float newX = cameraManager.getCamera().position.x + posMap.get(id.toString());
            entity = entityMap.get(id.toString());
            bounds = boundsCm.get(entity);
            bounds.setX(newX);

            transform = transformCm.get(entity);
            transform.setX(newX);
        }
    }

    @Override
    protected void processSystem() {
        setControlsPosition();
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
}
