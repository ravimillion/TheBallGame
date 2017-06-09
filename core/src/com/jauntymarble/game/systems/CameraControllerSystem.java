package com.jauntymarble.game.systems;

import com.artemis.BaseSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.jauntymarble.game.GameController;
import com.jauntymarble.game.GameData;
import com.jauntymarble.game.utils.CameraShaker;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

public class CameraControllerSystem extends BaseSystem implements AfterSceneInit {
    OrthographicCamera camera;
    GameController gameController;
    private CameraManager cameraManager;
    private PlayerSystem playerSystem;
    private CameraShaker cameraShaker;

    // variable cache
    private float MAX_FOLLOW;
    private float MIN_FOLLOW;

    public CameraControllerSystem(GameController gameController) {
        this.gameController = gameController;

    }

    @Override
    protected void processSystem() {
        if (GameController.CURRENT_LEVEL.equals(GameData.ID_LEVEL_TUTORIAL)) return;

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


        // camera y
        camera.position.y = camera.viewportHeight / 2f;

        // camera x
        float x = Math.max(bodyPosition.x, MIN_FOLLOW);
        camera.position.x = Math.min(x, MAX_FOLLOW);
    }

    public void shakeCamera(float intensity, float diminishFactor) {
        // no need to set the origin for shake as it is calculated for the moving center of the body itself
        cameraShaker.startShaking(intensity, diminishFactor);
    }


    @Override
    public void afterSceneInit() {
        camera = cameraManager.getCamera();
        this.cameraShaker = new CameraShaker(camera.position.x, camera.position.y);
        this.MAX_FOLLOW = GameData.WORLD_WIDTH - camera.viewportWidth / 2;
        this.MIN_FOLLOW = camera.viewportWidth / 2;
    }
}