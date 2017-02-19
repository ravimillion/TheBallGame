package com.simplegame.game.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Shape;
import com.kotcrab.vis.runtime.component.VisID;
import com.simplegame.game.utils.CameraShaker;

import ownLib.Own;
import ownLib.listener.OnContactListener;

public class PhysicsBodyContactSystem extends BaseSystem implements ContactListener {
    private ComponentMapper<VisID> visIDCm;
    private OnContactListener contactListener;
    private CameraControllerSystem cameraControllerSystem;

    @Override
    public void beginContact(com.badlogic.gdx.physics.box2d.Contact contact) {
    }

    @Override
    public void endContact(com.badlogic.gdx.physics.box2d.Contact contact) {
    }

    @Override
    public void preSolve(com.badlogic.gdx.physics.box2d.Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(com.badlogic.gdx.physics.box2d.Contact contact, ContactImpulse impulse) {
        VisID visID = null;
        Entity entity = null;

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getShape().getType() == Shape.Type.Circle) {
            entity = (Entity) contact.getFixtureB().getBody().getUserData();
            visID = visIDCm.get(entity);

        } else if (fixtureB.getShape().getType() == Shape.Type.Circle) {
            entity = (Entity) contact.getFixtureA().getBody().getUserData();
            visID = visIDCm.get(entity);
        }

        // if visID is null then the detected collision was also not between ball and a physics body
        if (visID != null) {
            processCollision("ball", visID.id, impulse.getNormalImpulses()[0]);
        }

//        if () return;
//
//        UserData userDataA = (UserData) fixtureA.getUserData();
//        UserData userDataB = (UserData) fixtureB.getUserData();
//
//        if (userDataA != null && userDataB != null) {
//            notifyListener(userDataA, userDataB, impulse.getNormalImpulses()[0]);
//        }
    }

    public void processCollision(String idA, String idB, float normalImpulse) {
        final float impulse = normalImpulse;
        if (exactMatch(idA, idB, "ball", "spike")) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    if (impulse > 100) {
                        cameraControllerSystem.shakeCamera(CameraShaker.SHAKE_INTENSITY_VERY_HIGH, CameraShaker.DIMINISH_FACTOR_MEDIUM);
                    }

//                    controlsSystem.setState(GameData.LEVEL_END);
                }
            });
        }

        if (exactMatch(idA, idB, "ball", "dboxvert")) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    Own.log("Hit the spike");
//                    controlsSystem.setState(GameData.LEVEL_END);
                }
            });
        }
    }


    private boolean exactMatch(String idA1, String idB1, String idA2, String idB2) {
        if (idA1.equals(idA2) && idB1.equals(idB2) || idB1.equals(idA2) && idA1.equals(idB2)) return true;
        return false;
    }

    @Override
    protected void processSystem() {

    }
}
