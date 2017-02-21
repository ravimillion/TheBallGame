package com.simplegame.game.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.simplegame.game.GameData;
import com.simplegame.game.utils.CameraShaker;

import java.util.Hashtable;
import java.util.Iterator;

import ownLib.Own;
import ownLib.listener.OnContactListener;

public class PhysicsBodyContactSystem extends BaseSystem implements ContactListener {
    private ComponentMapper<VisID> visIDCm;

    private VisIDManager idManager;
    private ControlsSystem controlsSystem;
    private OnContactListener contactListener;
    private CameraControllerSystem cameraControllerSystem;
    private Hashtable<String, CollisionData> collisionMap = new Hashtable();

    @Override
    public void beginContact(Contact contact) {
        parseCollisionData(contact, -1);
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        parseCollisionData(contact, impulse.getNormalImpulses()[0]);
    }

    private void parseCollisionData(Contact contact, float impulse) {
        Type shapeTypeA = contact.getFixtureA().getShape().getType();
        Type shapeTypeB = contact.getFixtureB().getShape().getType();


        // parse collisions only with ball
        if (shapeTypeA == Type.Circle) {
            Entity entity = (Entity) contact.getFixtureB().getBody().getUserData();
            collisionMap.put("ball" + entity.hashCode(), new CollisionData("ball" + entity.hashCode(), entity, impulse));
        }

        if (shapeTypeB == Type.Circle) {
            Entity entity = (Entity) contact.getFixtureA().getBody().getUserData();
            collisionMap.put("ball" + entity.hashCode(), new CollisionData("ball" + entity.hashCode(), entity, impulse));
        }
    }

    public void processCollision(String[] ids, float normalImpulse) {
        String idA = ids[0], idB = ids[1];

        final float impulse = normalImpulse;
        if (exactMatch(idA, idB, "ball", "idBottle")) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    Own.log("bottle ball");
                }
            });
        }

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
        if (idA1 == null || idB1 == null) return false;

        if (idA1.equals(idA2) && idB1.equals(idB2) || idB1.equals(idA2) && idA1.equals(idB2))
            return true;
        return false;
    }

    @Override
    protected void processSystem() {
        Iterator<String> iter = collisionMap.keySet().iterator();
        Own.log("Size: " + collisionMap.size());
        while (iter.hasNext()) {
            CollisionData collisionData = collisionMap.get(iter.next());
            // process entry
            removeOnCollision(collisionData);
            shakeCameraOnCollision(collisionData);
            changeGameStateOnCollision(collisionData);
            // remove entry
            iter.remove();
        }

    }

    public void changeGameStateOnCollision(CollisionData collisionData) {
        String[] cameraShakeList = {"spike"};

        Array<String> removalArray = new Array(cameraShakeList);

        VisID visID = visIDCm.get(collisionData.entity);
        if (visID != null && removalArray.indexOf(visID.id, false) > -1) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    controlsSystem.setState(GameData.RESTART_LEVEL);
                }
            });
        }
    }

    public void shakeCameraOnCollision(CollisionData collisionData) {
        String[] cameraShakeList = {"spike"};

        Array<String> removalArray = new Array(cameraShakeList);
        float threshold = 100f;

        VisID visID = visIDCm.get(collisionData.entity);
        if (visID != null && removalArray.indexOf(visID.id, false) > -1 && collisionData.impulse > threshold) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    Own.log("Hit the spike");
                    cameraControllerSystem.shakeCamera(CameraShaker.SHAKE_INTENSITY_VERY_HIGH, CameraShaker.DIMINISH_FACTOR_MEDIUM);
                }
            });
        }
    }

    public void removeOnCollision(CollisionData collisionData) {
        String[] removalList = {"idBottle", "idAnimStar"};

        Array<String> removalArray = new Array(removalList);
        final CollisionData finalCollisionData = collisionData;
        VisID visID = visIDCm.get(collisionData.entity);
        if (visID != null && removalArray.indexOf(visID.id, false) > -1) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    finalCollisionData.entity.deleteFromWorld();
                }
            });

        }
    }

    class CollisionData {
        public String id;
        public Entity entity;
        public float impulse;

        public CollisionData(String id, Entity entity, float impulse) {
            this.id = id;
            this.entity = entity;
            this.impulse = impulse;
        }
    }
}
