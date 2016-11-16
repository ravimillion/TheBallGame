package com.simplegame.game.levels.levelfour.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.screens.GameEntry;

public class CameraControllerSystem extends BaseSystem implements AfterSceneInit {
    private CameraManager cameraManager;
    private PlayerSystem playerSystem;
    private OrthographicCamera camera;

    @Override
    protected void processSystem() {
        float posX = playerSystem.body.getPosition().x;
        if (posX > camera.viewportWidth / 2 && posX < GameEntry.WORLD_WIDTH - camera.viewportWidth / 2) {
            camera.position.x = posX;
        }

    }

    @Override
    public void afterSceneInit() {
        camera = cameraManager.getCamera();
    }
}