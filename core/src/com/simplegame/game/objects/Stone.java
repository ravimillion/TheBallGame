package com.simplegame.game.objects;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonValue;
import com.simplegame.game.screens.GameEntry;
import com.simplegame.game.userdata.RectUserData;

import ownLib.Own;

/**
 * Created by ravi on 09.09.16.
 */
public class Stone extends GameObject {
    private float angle;
    private float width;
    private float height;
    private float density;
    private float restitution;
    private float friction;
    private Vector2 position;

    public Stone(World world, GameEntry gameEntry, JsonValue data) {
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
        this.body = Own.box2d.factory.getRectangleBody(BodyDef.BodyType.KinematicBody, position, angle, width, height, density, friction, restitution, id);
        body.setAwake(false);
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public float getHeight() {
        return this.height;
    }


    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public Vector2 getPosition() {
        return this.body.getPosition();
    }

    @Override
    public void setPosition(Vector2 pos) {
        this.position = pos;
//        this.body.setTransform(pos, this.body.getAngle());
    }

    @Override
    public void handleInput(Vector3 touchPoint) {

    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void drawGui() {
        RectUserData rectUserData = (RectUserData) this.body.getUserData();
        Own.box2d.gui.drawRectangleWithRotation(this.gameEntry.batch, "CARTTILE", this.body, rectUserData.getWidth(), rectUserData.getHeight());
    }
}
