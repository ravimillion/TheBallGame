package com.jauntymarble.game.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CameraShaker {

    public static final float SHAKE_INTENSITY_VERY_LOW = 0.5f;
    public static final float SHAKE_INTENSITY_LOW = 1.5f;
    public static final float SHAKE_INTENSITY_MEDIUM = 3.0f;
    public static final float SHAKE_INTENSITY_HIGHT = 5.0f;
    public static final float SHAKE_INTENSITY_VERY_HIGH = 10f;
    public static final float DIMINISH_FACTOR_SHORT = 0.75f;
    public static final float DIMINISH_FACTOR_MEDIUM = 0.85f;
    public static final float DIMINISH_FACTOR_LONG = 0.95f;
    private static final String TAG = CameraShaker.class.getSimpleName();
    private boolean isShaking = false;

    private float diminishFactor;
    private float shakeIntensity;
    private float randomAngle;

    private Vector2 originalCamCenter;
    private Vector2 offset;
    private Vector2 curCamCenter;


    public CameraShaker(float originX, float originY) {
        this.diminishFactor = DIMINISH_FACTOR_MEDIUM;
        this.shakeIntensity = SHAKE_INTENSITY_MEDIUM;

        originalCamCenter = new Vector2();
        offset = new Vector2();
        curCamCenter = new Vector2();

        setOriginCameraCenter(originX / 2f, originY / 2f);
        seedRandomAngle();
    }

    public boolean isShaking() {
        return isShaking;
    }

    public void startShaking(float intensity, float diminishFactor) {
        this.shakeIntensity = intensity;
        this.diminishFactor = diminishFactor;

        this.isShaking = true;
    }

    public void setOriginCameraCenter(float cameraCenterCoordsX, float cameraCenterCoordsY) {
        this.originalCamCenter.x = cameraCenterCoordsX;
        this.originalCamCenter.y = cameraCenterCoordsY;
    }

    private void seedRandomAngle() {
        randomAngle = MathUtils.random(1, 360);
    }

    private void computeCameraOffset() {
        offset.x = MathUtils.sinDeg(randomAngle) * shakeIntensity;
        offset.y = MathUtils.cosDeg(randomAngle) * shakeIntensity;
    }

    private void computeCurCamCenterForOrigin(Vector2 origin) {
        curCamCenter.x = origin.x + offset.x;
        curCamCenter.y = origin.y + offset.y;
    }

    private void computeCurCamCenter() {
        curCamCenter.x = originalCamCenter.x + offset.x;
        curCamCenter.y = originalCamCenter.y + offset.y;
    }

    private void diminishShake(Vector2 origin) {
        if (shakeIntensity < 0.01) {
            isShaking = false;
            return;
        }

        isShaking = true;

        shakeIntensity *= this.diminishFactor;
        randomAngle += (150 + MathUtils.random(1, 60));
    }

    public void reset() {
        shakeIntensity = SHAKE_INTENSITY_MEDIUM;
        isShaking = false;
        seedRandomAngle();
        computeCameraOffset();
    }

    public Vector2 getShakeCenterForOrigin(Vector2 origin) {
        computeCameraOffset();
        computeCurCamCenterForOrigin(origin);
        diminishShake(origin);
        return curCamCenter;
    }

    public Vector2 getShakeCenterFixOrigin() {
        computeCameraOffset();
        computeCurCamCenter();
        diminishShake(originalCamCenter);
        return curCamCenter;
    }
}