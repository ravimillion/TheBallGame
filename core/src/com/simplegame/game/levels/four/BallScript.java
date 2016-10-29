package com.simplegame.game.levels.four;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import ownLib.Own;

/**
 * Created by ravi on 29.10.16.
 */

public class BallScript implements IScript {
    private Entity ball;
    private Body physicsBody;
    private float torque = 1f;

    @Override
    public void init(Entity entity) {
        this.ball = entity;
        PhysicsBodyComponent physicsBodyComponent = ComponentRetriever.get(ball, PhysicsBodyComponent.class);
        physicsBody = physicsBodyComponent.body;
        physicsBody.destroyFixture(physicsBody.getFixtureList().get(0));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1.0f;

        CircleShape circle = new CircleShape();
        circle.setRadius(5/50f);
        fixtureDef.shape = circle;
//        circle.dispose();

        physicsBody.createFixture(fixtureDef);

    }

    @Override
    public void act(float delta) {
        if (Gdx.input.isTouched()) {
            if (Gdx.input.getX() > Own.device.getScreenWidth() / 2) {
                physicsBody.applyTorque(torque * -1, true);
            } else {
                physicsBody.applyTorque(torque, true);
            }
        }
    }

    @Override
    public void dispose() {

    }
}
