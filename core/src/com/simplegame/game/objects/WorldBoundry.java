package com.simplegame.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import ownLib.Own;

/**
 * Created by ravi on 29.08.16.
 */
public class WorldBoundry {

    public final static int LEFT = 1;
    public final static int RIGHT = 2;
    public final static int TOP = 3;
    public final static int BOTTOM = 4;

    private Body left = null;
    private Body top = null;
    private Body bottom = null;
    private Body right = null;

    public WorldBoundry(float WORLD_WIDTH, float WORLD_HEIGHT) {
        bottom = Own.box2d.factory.getEdgeBody(BodyDef.BodyType.StaticBody, new Vector2(0, 1), 0, 0, 0, WORLD_WIDTH, 0, 0.5f, 0.5f, 0.5f, "bottom");
        left = Own.box2d.factory.getEdgeBody(BodyDef.BodyType.StaticBody, new Vector2(0, 1), 0, 0, 0, 0, WORLD_HEIGHT, 0.5f, 0.5f, 0.5f, "left");
        right = Own.box2d.factory.getEdgeBody(BodyDef.BodyType.StaticBody, new Vector2(0, 1), 0, WORLD_WIDTH, 0, WORLD_WIDTH, WORLD_HEIGHT, 0.5f, 0.5f, 0.5f, "right");
        top = Own.box2d.factory.getEdgeBody(BodyDef.BodyType.StaticBody, new Vector2(0, 1), 0, 0, WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT, 0.5f, 0.5f, 0.5f, "top");
    }

    public void updateWorldBoundry(int type, Vector2 point, float angle) {
        switch (type) {
            case WorldBoundry.LEFT:
                left.setTransform(point, angle);
                break;
            case WorldBoundry.RIGHT:
                right.setTransform(point, angle);
                break;
            case WorldBoundry.TOP:
                top.setTransform(point, angle);
                break;
            case WorldBoundry.BOTTOM:
                bottom.setTransform(point, angle);
                break;
            default:
                break;
        }
    }
}
