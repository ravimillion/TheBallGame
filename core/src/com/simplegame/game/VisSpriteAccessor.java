package com.simplegame.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.kotcrab.vis.runtime.component.VisSprite;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by ravi on 09.02.17.
 */

public class VisSpriteAccessor implements TweenAccessor<VisSprite> {

    public static final int FADE_IN_OUT = 0;

    @Override
    public int getValues(VisSprite visSprite, int tweenType, float[] returnValues) {
        Sprite target = new Sprite(visSprite.getRegion());
        switch(tweenType) {
            case FADE_IN_OUT:
                returnValues[0] = target.getColor().a;
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(VisSprite visSprite, int tweenType, float[] newValues) {
        Sprite target = new Sprite(visSprite.getRegion());
        switch(tweenType) {
            case FADE_IN_OUT:
                target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
                break;
            default:
                assert false;
        }
    }

}