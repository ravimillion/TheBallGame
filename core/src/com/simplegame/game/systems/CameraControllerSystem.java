package com.simplegame.game.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.GameController;
import com.simplegame.game.utils.CameraShaker;

public class CameraControllerSystem extends BaseSystem implements AfterSceneInit {
    OrthographicCamera camera;
    GameController gameController;
    private CameraManager cameraManager;
    private PlayerSystem playerSystem;
    private CameraShaker cameraShaker;

    public CameraControllerSystem(GameController gameController) {
        this.gameController = gameController;

    }

    @Override
    protected void processSystem() {
        Vector2 bodyPosition = playerSystem.body.getPosition();

        if (cameraShaker.isShaking()) {
            // shake Origin is defined as ball x-position and vertical center of the screen
            Vector2 shakeOrigin = new Vector2(bodyPosition.x, camera.viewportHeight / 2f);
            // get afterShakePosition for the duration of the shake (random points are supposed to be generated)
            Vector2 afterShakePosition = cameraShaker.getShakeCenterForOrigin(shakeOrigin);
            camera.position.x = afterShakePosition.x;
            camera.position.y = afterShakePosition.y;
            camera.update();
            return;
        }


        if (bodyPosition.x > camera.viewportWidth / 2 && bodyPosition.x < this.gameController.WORLD_WIDTH - camera.viewportWidth / 2) {
            camera.position.x = bodyPosition.x;
            camera.position.y = camera.viewportHeight / 2f;
            camera.update();
        }

    }

    public void shakeCamera(float intensity, float diminishFactor) {
        // no need to set the origin for shake as it is calculated for the moving center of the body itself
        cameraShaker.startShaking(intensity, diminishFactor);
    }


    @Override
    public void afterSceneInit() {
        camera = cameraManager.getCamera();
        this.cameraShaker = new CameraShaker(camera.position.x, camera.position.y);
    }
}