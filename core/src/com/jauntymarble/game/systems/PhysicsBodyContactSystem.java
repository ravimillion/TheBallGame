package com.jauntymarble.game.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.jauntymarble.game.GameData;
import com.jauntymarble.game.utils.CameraShaker;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.system.VisIDManager;

import java.util.Hashtable;
import java.util.Iterator;

import ownLib.listener.OnContactListener;

public class PhysicsBodyContactSystem extends BaseSystem implements ContactListener {
    private ComponentMapper<VisID> visIDCm;
    private ComponentMapper<PhysicsBody> physicsCm;

    private VisIDManager idManager;
    private SoundSystem soundSystem;
    private PlayerSystem playerSystem;
    private ScoringSystem scoringSystem;
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

    @Override
    protected void processSystem() {
        Iterator<String> iter = collisionMap.keySet().iterator();
        while (iter.hasNext()) {
            CollisionData collisionData = collisionMap.get(iter.next());
            // process entry
            soundEffect(collisionData);
            removeOnCollision(collisionData);
            shakeCameraOnCollision(collisionData);
            changeGameStateOnCollision(collisionData);
//            applySuperPower(collisionData);
            // remove entry
            iter.remove();
        }
    }

    public void soundEffect(CollisionData collisionData) {
        VisID visID = visIDCm.get(collisionData.entity);
        if (visID == null) return;

        switch (visID.id) {
            case "idWoodBox":
                if (collisionData.impulse > 20) {
                    soundSystem.triggerSoundEffect("idSoundWoodBox");
                }
                break;
            case "idUphill":
            case "idUpDown":
            case "idBoundary":
                if (collisionData.impulse > 100) {
                    soundSystem.triggerSoundEffect("idSoundGlass");
                }
                break;
            case "idPaintBox":
                if (collisionData.impulse > 100) {
                    soundSystem.triggerSoundEffect("idSoundPaintBox");
                }
                break;
            case "idFire":
                soundSystem.triggerSoundEffect("idSoundGlassBreak");
                break;
            case "idSpike":
                if (collisionData.impulse > 50) {
                    soundSystem.triggerSoundEffect("idSoundGlassBreak");
                }
                break;
            case "idPowerGravity":
            case "idPowerBounce":
                soundSystem.triggerSoundEffect("idSoundPower");
                break;
            case "idAnimStar":
                soundSystem.triggerSoundEffect("idSoundStar");
                break;
        }
    }

    public void applySuperPower(CollisionData collisionData) {
        final VisID visID = visIDCm.get(collisionData.entity);
        if (visID == null) return;

        if (GameData.SUPER_POWER_LIST.indexOf(visID.id, false) >= 0) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    playerSystem.applyPower(visID.id);
                }
            });
        }
    }

    public void changeGameStateOnCollision(CollisionData collisionData) {
        VisID visID = visIDCm.get(collisionData.entity);
        if (visID == null) return;

        if (GameData.STATE_CHANGE_LIST.indexOf(visID.id, false) >= 0) {
            if (visID.id.equals("idSpike") && collisionData.impulse < 50) return;

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    playerSystem.setPlayerPosition();
                    controlsSystem.setState(GameData.GAME_OVER);
                }
            });
        }
    }

    public void shakeCameraOnCollision(CollisionData collisionData) {
        float threshold = 100f;

        VisID visID = visIDCm.get(collisionData.entity);
        if (visID != null && GameData.CAMERA_SHAKE_LIST.indexOf(visID.id, false) > -1 && collisionData.impulse > threshold) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    cameraControllerSystem.shakeCamera(CameraShaker.SHAKE_INTENSITY_VERY_HIGH, CameraShaker.DIMINISH_FACTOR_MEDIUM);
                }
            });
        }
    }

    public void removeOnCollision(CollisionData collisionData) {
        final CollisionData finalCollisionData = collisionData;
        final VisID visID = visIDCm.get(collisionData.entity);

        if (visID != null && GameData.REMOVE_ON_COLLISIOIN_LIST.indexOf(visID.id, false) > -1) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    // remove physics body
                    Body body = physicsCm.get(finalCollisionData.entity).body;
                    body.getWorld().destroyBody(body);

                    finalCollisionData.entity.deleteFromWorld();

                    if (visID.id.equals("idAnimStar")) scoringSystem.scoreUP();
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
