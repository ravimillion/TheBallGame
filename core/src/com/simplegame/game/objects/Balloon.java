//package com.simplegame.game.objects;
//
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.math.Vector3;
//import com.badlogic.gdx.physics.box2d.Body;
//import com.badlogic.gdx.physics.box2d.BodyDef;
//import com.badlogic.gdx.physics.box2d.World;
//import com.badlogic.gdx.utils.JsonValue;
//import com.simplegame.game.screens.GameEntry;
//
//import ownLib.Own;
//
///**
// * Created by ravi on 06.09.16.
// */
//public class Balloon extends GameObject {
//    private float density;
//    private float restitution;
//    private float friction;
//    private Vector2 position;
//
//    public Balloon(World world, GameEntry gameEntry, JsonValue data) {
//        this.gameEntry = gameEntry;
//        this.world = world;
//        this.id = data.getString("id");
//        this.angle = data.getFloat("angle");
//        this.width = data.getFloat("width");
//        this.height = data.getFloat("height");
//        this.restitution = data.getFloat("restitution");
//        this.density = data.getFloat("density");
//        this.friction = data.getFloat("friction");
//        this.position = new Vector2(data.getFloat("x"), data.getFloat("y"));
//    }
//
//    public void create() {
//        Own.box2d.factory.setWorld(this.world);
//        this.body = Own.box2d.factory.createJsonBody("BALLOON",
//                "balloon",
//                BodyDef.BodyType.DynamicBody,
//                position,
//                this.angle,
//                this.getWidth(),
//                this.getHeight(),
//                this.density,
//                this.friction,
//                this.restitution,
//                this.id);
//        this.body.setGravityScale(-0.05f);
//    }
//
//    public Body getBody() {
//        return this.body;
//    }
//
//
//    @Override
//    public Vector2 getPosition() {
//        return this.body.getPosition();
//    }
//
//    @Override
//    public void setPosition(Vector2 pos) {
//        this.body.setTransform(pos, 0);
//    }
//
//    @Override
//    public void handleInput(Vector3 touchPoint) {
//    }
//
//    @Override
//    public void update(float deltaTime) {
//    }
//
//    @Override
//    public void drawGui() {
//        Own.box2d.gui.drawJsonBody(gameEntry.batch, "BALLOON", this.body);
//    }
//
//    public Vector2 getVelocity() {
//        return this.body.getLinearVelocity();
//    }
//
//    public float getWidth() {
//        return width;
//    }
//
//    public void setWidth(float width) {
//        this.width = width;
//    }
//
//    public float getHeight() {
//        return height;
//    }
//
//    @Override
//    public void dispose() {
//
//    }
//
//    public void setHeight(float height) {
//        this.height = height;
//    }
//}
