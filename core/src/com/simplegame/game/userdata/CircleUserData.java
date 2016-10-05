package com.simplegame.game.userdata;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;

/**
 * Created by ravi on 06.09.16.
 */
public class CircleUserData extends UserData {
    private CircleShape shape;
    private float radius;

    public CircleUserData(String id, String type, CircleShape circleShape, Vector2 pos, float radius) {
        super(id, type, pos);
        this.shape = circleShape;
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
    }
}
