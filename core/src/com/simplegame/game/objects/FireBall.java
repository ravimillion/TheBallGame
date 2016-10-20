package com.simplegame.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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
public class FireBall extends GameObject {
    private World world;
    private GameEntry gameEntry;

    private float radius;
    private Vector2 position;
    private float density;
    private float restitution;
    private float friction;
    private ParticleEffect particleEffect;
    private OrthographicCamera box2DCam;

    public FireBall(World world, GameEntry gameEntry, JsonValue data, OrthographicCamera camera) {
        this.box2DCam = camera;
        this.gameEntry = gameEntry;
        this.world = world;
        this.id = data.getString("id");
        this.radius = data.getFloat("radius");
        this.restitution = data.getFloat("restitution");
        this.density = data.getFloat("density");
        this.friction = data.getFloat("friction");
        this.position = new Vector2(data.getFloat("x"), data.getFloat("y"));
    }

    public void create() {
        Own.box2d.factory.setWorld(this.world);
        this.body = Own.box2d.factory.getCircleBody(BodyDef.BodyType.KinematicBody,
                position,
                0,
                radius,
                density,
                friction,
                restitution,
                id);

        this.body.setLinearVelocity(new Vector2(0, -5f));
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("particle/fire.json"), Gdx.files.internal("particle"));
        particleEffect.getEmitters().first().setPosition(this.getPosition().x, this.getPosition().y);
        particleEffect.start();
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void dispose() {
        particleEffect.dispose();
    }

    @Override
    public float getWidth() {
        return 0;
    }

    @Override
    public float getHeight() {
        return this.height;
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
//        Own.box2d.gui.drawCircleWithRotation(gameEntry.batch, "FIREBALL", this.body);
        Vector3 pos = this.box2DCam.project(new Vector3(new Vector2(this.getPosition().x, this.getPosition().y), 0));
        particleEffect.setPosition(pos.x, pos.y);
        particleEffect.setDuration(50);
        particleEffect.draw(this.gameEntry.batch, Gdx.graphics.getDeltaTime());
        if (particleEffect.isComplete()) particleEffect.reset();
    }

    public Vector2 getVelocity() {
        return this.body.getLinearVelocity();
    }
}
