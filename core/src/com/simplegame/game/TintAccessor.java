package com.simplegame.game;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.Color;
import com.kotcrab.vis.runtime.component.Tint;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by ravi on 09.02.17.
 */

public class TintAccessor implements TweenAccessor<Tint> {
    public static final int FADE_IN_OUT = 0;
    public Entity entity;

    @Override
    public int getValues(Tint tint, int tweenType, float[] returnValues) {

        switch (tweenType) {
            case FADE_IN_OUT:
                returnValues[0] = tint.getTint().a;
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Tint tint, int tweenType, float[] newValues) {
        switch (tweenType) {
            case FADE_IN_OUT:
                Color c = tint.getTint();
                c.a = newValues[0];
                tint.setTint(c);
                break;
            default:
                assert false;
        }
    }

}