package com.simplegame.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import ownLib.Own;

public class ShakeCamera {

    private static final String TAG = ShakeCamera.class.getSimpleName();
    private final float SHAKE_RADIUS = 3.0f;
    private boolean isShaking = false;
    private Vector2 originCameraCenter;
    private float origShakeRadius = 30.0f;
    private float shakeRadius;
    private float randomAngle;
    private Vector2 offsetCameraCenter;
    private Vector2 currentCameraCenter;


    public ShakeCamera(OrthographicCamera camera) {
        this.shakeRadius = SHAKE_RADIUS;
        this.origShakeRadius = this.shakeRadius;

        originCameraCenter = new Vector2();
        offsetCameraCenter = new Vector2();
        currentCameraCenter = new Vector2();

        setOriginCameraCenter(camera.position.x / 2f, camera.position.y / 2f);
        seedRandomAngle();
    }

    public boolean isShaking() {
        return isShaking;
    }

    public void startShaking() {
        this.isShaking = true;
    }

    public void setOriginCameraCenter(float cameraCenterCoordsX, float cameraCenterCoordsY) {
        this.originCameraCenter.x = cameraCenterCoordsX;
        this.originCameraCenter.y = cameraCenterCoordsY;
    }

    private void seedRandomAngle() {
        randomAngle = MathUtils.random(1, 360);
    }

    private void computeCameraOffset() {
        offsetCameraCenter.x = MathUtils.sinDeg(randomAngle) * shakeRadius;
        offsetCameraCenter.y = MathUtils.cosDeg(randomAngle) * shakeRadius;
        Own.log("offsetX: " + offsetCameraCenter.x + " offsetY: " + offsetCameraCenter.y);
    }

    private void computeCurrentCameraCenter(float x, float y) {
        currentCameraCenter.x = x + offsetCameraCenter.x;
        currentCameraCenter.y = y + offsetCameraCenter.y;
    }

    private void diminishShake(float x, float y) {
        if (shakeRadius < 0.001) {
            Gdx.app.log(TAG, "DONE SHAKING: shakeRadius is: " + shakeRadius + " randomAngle is: " + randomAngle);
            isShaking = false;
            shakeRadius = SHAKE_RADIUS;
            currentCameraCenter.x = x;
            currentCameraCenter.y = y;
            return;
        }

        //Gdx.app.log(TAG, "Current shakeRadius is: " + shakeRadius + " randomAngle is: " + randomAngle);

        isShaking = true;

        shakeRadius *= .9f;
        randomAngle += (150 + MathUtils.random(1, 60));
    }

    public void reset() {
        shakeRadius = origShakeRadius;
        isShaking = false;
        seedRandomAngle();
        computeCameraOffset();
    }

    public Vector2 getShakeCameraCenter(float x, float y) {
        computeCameraOffset();
        computeCurrentCameraCenter(x, y);
        diminishShake(x, y);
        return currentCameraCenter;
    }
}