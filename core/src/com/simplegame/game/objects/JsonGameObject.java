//package com.simplegame.game.objects;
//
//import com.badlogic.gdx.math.MathUtils;
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
// * Created by ravi on 18.09.16.
// */
//public class JsonGameObject extends GameObject {
//    private float angle;
//    private float density;
//    private float restitution;
//    private float friction;
//
//    public JsonGameObject(World world, GameEntry gameEntry, JsonValue data) {
//        this.world = world;
//        this.gameEntry = gameEntry;
//        this.id = data.getString("id");
//        this.angle = data.getFloat("angle");
//        this.width = data.getFloat("width");
//        this.height = data.getFloat("height");
//        this.restitution = data.getFloat("restitution");
//        this.density = data.getFloat("density");
//        this.friction = data.getFloat("friction");
//        this.position = new Vector2(data.getFloat("x"), data.getFloat("y"));
//        this.create();
//    }
//
//    @Override
//    public void create() {
//        Own.box2d.factory.setWorld(this.world);
//        this.body = Own.box2d.factory.createJsonBody(this.id.toUpperCase(),
//                this.id,
//                BodyDef.BodyType.StaticBody,
//                position,
//                this.angle * MathUtils.degreesToRadians,
//                this.width,
//                this.height,
//                this.density,
//                this.friction,
//                this.restitution,
//                this.id);
//    }
//
//    @Override
//    public float getWidth() {
//        return this.width;
//    }
//
//    @Override
//    public float getHeight() {
//        return this.height;
//    }
//
//    @Override
//    public void dispose() {
//
//    }
//
//    @Override
//    public Vector2 getPosition() {
//        return null;
//    }
//
//    @Override
//    public void setPosition(Vector2 pos) {
//
//    }
//
//    @Override
//    public void handleInput(Vector3 touchPoint) {
//
//    }
//
//    @Override
//    public void update(float deltaTime) {
//
//    }
//
//    @Override
//    public void drawGui() {
//        Own.box2d.gui.drawJsonBody(gameEntry.batch, this.id.toUpperCase(), this.body);
//    }
//
//    public Body getBody() {
//        return this.body;
//    }
//}
