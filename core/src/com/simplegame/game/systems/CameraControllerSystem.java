package com.simplegame.game.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.GameController;
import com.simplegame.game.utils.ShakeCamera;

import ownLib.Own;

public class CameraControllerSystem extends BaseSystem implements AfterSceneInit {
    OrthographicCamera camera;
    GameController gameController;
    private CameraManager cameraManager;
    private PlayerSystem playerSystem;
    private ShakeCamera shakeCamera;
    private float y = 0;

    public CameraControllerSystem(GameController gameController) {
        this.gameController = gameController;

    }
    @Override
    protected void processSystem() {
        float posX = playerSystem.body.getPosition().x;
        float posY = playerSystem.body.getPosition().y;
        if (shakeCamera.isShaking()) {
            Vector3 pos = new Vector3(shakeCamera.getShakeCameraCenter(posX, y), 0);
            Own.log("Exlosion: pos.x" + pos.x + " pos.y: " + pos.y);
            camera.position.x = pos.x;
            camera.position.y = pos.y;
            camera.update();
            return;
        }


        if (posX > camera.viewportWidth / 2 && posX < this.gameController.WORLD_WIDTH - camera.viewportWidth / 2) {
            camera.position.x = posX;
            camera.position.y = this.y;
            camera.update();
        }

    }

    public void shakeCamera() {
        shakeCamera.setOriginCameraCenter(camera.position.x, camera.position.y);
        shakeCamera.startShaking();
    }


    @Override
    public void afterSceneInit() {
        camera = cameraManager.getCamera();
        this.shakeCamera = new ShakeCamera(camera);
        this.y = camera.position.y;
    }
}