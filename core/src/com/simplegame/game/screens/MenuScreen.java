package com.simplegame.game.screens;

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
import com.simplegame.game.components.Bounds;

import java.util.HashMap;

import aurelienribon.tweenengine.TweenManager;
import ownLib.Own;

public class MenuScreen extends BaseSystem implements AfterSceneInit, InputProcessor {
    ComponentMapper<Bounds> boundsCm;
    ComponentMapper<VisSprite> spriteCm;
    ComponentMapper<Transform> transformCm;

    VisIDManager idManager;
    CameraManager cameraManager;
    TweenManager tweenManager = new TweenManager();


    GameController gameController;
    String[] buttons = {"idOne", "idTwo", "idThree"};
    HashMap<String, Entity> entityMap = new HashMap<String, Entity>();
    HashMap<String, Bounds> boundsMap = new HashMap<String, Bounds>();
    VisSprite oneSprite;

    public MenuScreen(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void afterSceneInit() {
        for (int i = 0; i < buttons.length; i++) {
            String btnId = buttons[i];
            Entity entity = idManager.get(btnId);

            entityMap.put(btnId, idManager.get(btnId));
            boundsMap.put(btnId, boundsCm.get(entity));
        }

        Own.io.addProcessor(this);

//        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
//
//        Entity entityOne = idManager.get("idTwo");
//        oneSprite = spriteCm.get(entityOne);
//        Tween.set(sprite, SpriteAccessor.FADE_IN_OUT).target(0).start(tweenManager);
//        Tween.to(sprite, SpriteAccessor.FADE_IN_OUT, 15).target(1).start(tweenManager).setCallback(new TweenCallback() {
//            @Override
//            public void onEvent(int type, BaseTween<?> source) {
//                Gdx.app.postRunnable(new Runnable() {
//                    @Override
//                    public void run() {
//                        Own.log("Finished tweening");
//                    }
//                });
//            }
//        }).start(tweenManager);

        // TWEEN MANAGER
//        Tween.registerAccessor(VisSprite.class, new VisSpriteAccessor());
//
//        Tween.set(oneSprite, VisSpriteAccessor.FADE_IN_OUT).target(0).start(tweenManager);
//        Tween.to(oneSprite, VisSpriteAccessor.FADE_IN_OUT, 5).target(1).start(tweenManager).setCallback(new TweenCallback() {
//            @Override
//            public void onEvent(int type, BaseTween<?> source) {
//                Gdx.app.postRunnable(new Runnable() {
//                    @Override
//                    public void run() {
//                        Own.log("Finished tweening");
//                    }
//                });
//            }
//        }).start(tweenManager);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPoint = new Vector3();
        touchPoint.set(screenX, screenY, 0);
        cameraManager.getCamera().unproject(touchPoint);

        String buttonId = getPressedButtonId(touchPoint);
        if (buttonId != null) {
            this.dispose();
            if (buttonId.equals("idOne")) gameController.loadLevelOneScene();
            if (buttonId.equals("idTwo")) gameController.loadLevelTwoScene();
            if (buttonId.equals("idThree")) gameController.loadLevelThreeScene();
        }

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


    @Override
    protected void processSystem() {
    }

    @Override
    public void dispose() {
        Own.io.removeProcessor(this);
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
