package com.simplegame.game.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.GameController;

public class CameraControllerSystem extends BaseSystem implements AfterSceneInit {
    OrthographicCamera camera;
    GameController gameController;
    private CameraManager cameraManager;
    private PlayerSystem playerSystem;

    public CameraControllerSystem(GameController gameController) {
        this.gameController = gameController;
    }
    @Override
    protected void processSystem() {
        float posX = playerSystem.body.getPosition().x;
        if (posX > camera.viewportWidth / 2 && posX < this.gameController.WORLD_WIDTH - camera.viewportWidth / 2) {
            camera.position.x = posX;
            camera.update();
        }

    }

    @Override
    public void afterSceneInit() {
        camera = cameraManager.getCamera();
    }
}