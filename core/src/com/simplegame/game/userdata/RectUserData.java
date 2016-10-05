package com.simplegame.game.userdata;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Created by ravi on 01.09.16.
 */
public class RectUserData extends UserData {
    private float width;
    private float height;
    private PolygonShape shape;

    public RectUserData(String id, String type, PolygonShape shape, Vector2 pos, float width, float height) {
        super(id, type, pos);
        this.shape = shape;
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
