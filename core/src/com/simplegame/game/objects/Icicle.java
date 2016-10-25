package com.simplegame.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonValue;
import com.simplegame.game.screens.GameEntry;

import ownLib.Own;

public class Icicle extends GameObject {
    private float density;
    private float friction;
    private float restitution;

    private Vector2 position;

    public Icicle(World world, GameEntry gameEntry, JsonValue data) {
        this.world = world;
        this.gameEntry = gameEntry;
        this.id = data.getString("id");
        this.angle = data.getFloat("angle");
        this.width = data.getFloat("width");
        this.height = data.getFloat("height");
        this.restitution = data.getFloat("restitution");
        this.density = data.getFloat("density");
        this.friction = data.getFloat("friction");
        this.position = new Vector2(data.getFloat("x"), data.getFloat("y"));
    }

    @Override
    public void create() {
        Own.box2d.factory.setWorld(this.world);
        this.body = Own.box2d.factory.createJsonBody("ICICLE",
                "icicle",
                BodyDef.BodyType.StaticBody,
                position,
                this.angle,
                this.width,
                this.height,
                this.density,
                this.friction,
                this.restitution,
                this.id);
    }

    @Override
    public Vector2 getPosition() {
        return this.getBody().getPosition();
    }

    @Override
    public void setPosition(Vector2 pos) {
        this.getBody().setTransform(pos, 0);
    }

    @Override
    public void handleInput(Vector3 touchPoint) {

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void drawGui() {
        Own.box2d.gui.drawJsonBody(gameEntry.batch, "ICICLE", this.body);
    }

    public float getHeight() {
        return this.height;
    }

    @Override
    public void dispose() {

    }

    public float getWidth() {
        return this.width;
    }

    public Body getBody() {
        return this.body;
    }
}