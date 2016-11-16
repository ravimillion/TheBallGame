package com.simplegame.game.levels.levelfour.systems;

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
import com.simplegame.game.levels.GameState;
import com.simplegame.game.levels.levelfour.LevelFour;
import com.simplegame.game.levels.levelfour.component.Bounds;

import java.util.HashMap;

import ownLib.Own;


public class ControlsSystem extends BaseSystem implements AfterSceneInit, InputProcessor {
    private ComponentMapper<Bounds> boundsCm;
    private ComponentMapper<VisSprite> spriteCm;
    private ComponentMapper<Transform> transformCm;

    private VisIDManager idManager;
    private PlayerSystem playerSystem;
    private CameraManager cameraManager;

    private LevelFour game;

    private HashMap<String, Float> posMap = new HashMap<String, Float>();
    private HashMap<String, Entity> entityMap = new HashMap<String, Entity>();
    private HashMap<String, Bounds> boundsMap = new HashMap<String, Bounds>();
    private String[] buttons = {"idPause", "idResume", "idReady", "idQuit", "idRestart", "idGameOver"};

    public ControlsSystem(LevelFour game) {
        this.game = game;
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
        setState(GameState.READY);
    }

    public void setState(GameState state) {
        playerSystem.state = state;

        hide("*");
        switch (state) {
            case PAUSED:
                show("idResume");
                show("idQuit");
                break;
            case RUNNING:
                show("idPause");
                show("idRestart");
                break;
            case READY:
                show("idReady");
                break;
            case LEVEL_END:
                game.notify(GameState.LEVEL_END);
                break;
            case RESTART_LEVEL:
                game.notify(GameState.RESTART_LEVEL);
                break;
            case QUIT:
                game.notify(GameState.QUIT);
            default:
                break;
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPoint = new Vector3();
        touchPoint.set(screenX, screenY, 0);
        cameraManager.getCamera().unproject(touchPoint);

        String pressedBtn = getPressedButton(touchPoint);
        if (pressedBtn != null) setState(getNextState(pressedBtn));

        return false;
    }

    private String getPressedButton(Vector3 touchPoint) {
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

    public GameState getNextState(String buttonId) {

        switch (buttonId) {
            case "idPause":
                return GameState.PAUSED;
            case "idReady":
                return GameState.RUNNING;
            case "idQuit":
                return GameState.QUIT;
            case "idResume":
                return GameState.RUNNING;
            case "idRestart":
                return GameState.RESTART_LEVEL;
            default:
                break;
        }

        return null;
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
