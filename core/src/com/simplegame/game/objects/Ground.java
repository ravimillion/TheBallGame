package com.simplegame.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonValue;
import com.simplegame.game.screens.GameEntry;

/**
 * Created by ravi on 18.10.16.
 */

public class Ground extends GameObject {

    float density;
    float friction;
    float restitution;

    public Ground(World world, GameEntry gameEntry, JsonValue data) {
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
    public Vector2 getPosition() {
        return null;
    }

    @Override
    public void setPosition(Vector2 pos) {

    }

    @Override
    public void handleInput(Vector3 touchPoint) {

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void drawGui() {

    }

    @Override
    public void create() {

    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return 0;
    }
}
