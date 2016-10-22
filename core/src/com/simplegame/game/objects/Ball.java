package com.simplegame.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonValue;
import com.simplegame.game.screens.GameEntry;

import ownLib.Own;

/**
 * Created by ravi on 06.09.16.
 */
public class Ball extends GameObject {
    private World world;
    private GameEntry gameEntry;

    private float radius;
    private Vector2 position;
    private float density;
    private float restitution;
    private float friction;

    public Ball(World world, GameEntry gameEntry, JsonValue data) {
        this.gameEntry = gameEntry;
        this.world = world;
        this.id = data.getString("id");
        this.radius = data.getFloat("radius");
        this.restitution = data.getFloat("restitution");
        this.density = data.getFloat("density");
        this.friction = data.getFloat("friction");
        this.position = new Vector2(data.getFloat("x"), data.getFloat("y"));
        this.create();
    }

    public void create() {
        Own.box2d.factory.setWorld(this.world);
        this.body = Own.box2d.factory.getCircleBody(BodyDef.BodyType.DynamicBody,
                position,
                0,
                radius,
                density,
                friction,
                restitution,
                id);
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

    @Override
    public void dispose() {

    }

    public Body getBody() {
        return this.body;
    }

    @Override
    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    @Override
    public void setPosition(Vector2 pos) {
        this.body.setTransform(pos, 0);
    }

    public void setDamping(float dampFactor) {
        this.body.setLinearDamping(dampFactor);
    }

    @Override
    public void handleInput(Vector3 touchPoint) {
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void drawGui() {
        Own.box2d.gui.drawCircleWithRotation(gameEntry.batch, "TRANSBALL", this.body);
    }

    public Vector2 getVelocity() {
        return this.body.getLinearVelocity();
    }
}
