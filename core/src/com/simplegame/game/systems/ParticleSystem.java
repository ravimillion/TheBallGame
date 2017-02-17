package com.simplegame.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.physics.box2d.Body;
import com.kotcrab.vis.plugin.spriter.runtime.component.VisSpriter;
import com.kotcrab.vis.runtime.component.OriginalRotation;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.PhysicsProperties;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisParticle;
import com.kotcrab.vis.runtime.component.VisPolygon;
import com.kotcrab.vis.runtime.system.physics.PhysicsBodyManager;
import com.simplegame.game.GameController;

public class ParticleSystem extends EntitySystem {

    private ComponentMapper<VisParticle> visParticleCm;
    private ComponentMapper<VisID> visIdCm;
    private ComponentMapper<PhysicsBody> physicsCm;
    private ComponentMapper<VisSpriter> visSpriterCm;
    private ComponentMapper<Transform> transformCm;
    private ComponentMapper<OriginalRotation> originalRotationCm;
    private PhysicsBodyManager physicsBodyManager;

    public ParticleSystem() {
        super(Aspect.all(VisParticle.class, VisPolygon.class, PhysicsProperties.class));
    }

    @Override
    public void inserted(Entity e) {
        physicsBodyManager.inserted(e);
//        VisParticle visParticle = visParticleCm.get(e);
//        ParticleEmitter particleEmitter = visParticle.getEffect().getEmitters().get(0);
//        particleEmitter.setMaxParticleCount(0);
    }

    @Override
    protected void processSystem() {
        Bag<Entity> entityBag = getEntities();
        for (Entity e: entityBag) {
            PhysicsBody physics = physicsCm.get(e);
            if (physics.body == null) return;

            Body body = physics.body;
            VisParticle visParticle = visParticleCm.get(e);
            ParticleEmitter particleEmitter = visParticle.getEffect().getEmitters().get(0);
            particleEmitter.setPosition(body.getPosition().x, body.getPosition().y);

            if (body.getPosition().y < 8) {
                body.setTransform(body.getPosition().x, GameController.WORLD_HEIGHT - 1, body.getAngle());
                body.setLinearVelocity(0, 0);
            }

        }
    }
}

//public class ParticleSystem extends EntityProcessingSystem implements AfterSceneInit {
//    ComponentMapper<VisParticle> visParticleCm;
//    ComponentMapper<PhysicsBody> physicsBodyCm;
//    ComponentMapper<VisID> visIDCm;
//    PhysicsSystem physicsSystem;
//    ComponentMapper<Point> pointCm;
//    ComponentMapper<Transform> transformCm;
//    PlayerSystem playerSystem;
//
//    VisIDManager idManager;
//    Body body = null;
//    int FIREBALL_COUNT = 28;
//    int MAX_PARTICLE_COUNT = 500;
//    private HashMap<String, Body> fireballs;
//    private RuntimeConfiguration runtimeConfig;
//
//    public ParticleSystem() {
//        super(Aspect.one(Point.class, VisParticle.class));
//    }
//
//    @Override
//    protected void process(Entity e) {
//        VisID visID = visIDCm.get(e);
//        if (visID.id.equals("boundary")) return;
//
//        float minRange = playerSystem.body.getPosition().x - GameController.VIEWPORT_WIDTH / 2;
//        float maxRange = playerSystem.body.getPosition().x + GameController.VIEWPORT_WIDTH / 2;
//
//        VisParticle visParticle = visParticleCm.get(e);
//
//        body = null;
//        if (visParticle != null) {
//            String fireballId = "fireball" + visID.id.replace("partical", "");
//            body = fireballs.get(fireballId);
//            if (body != null) {
//                float bx = body.getPosition().x;
//                float by = body.getPosition().y;
//
//                ParticleEmitter particleEmitter = visParticle.getEffect().getEmitters().get(0);
//
//                if (bx < minRange || bx > maxRange) {
//                    particleEmitter.setMaxParticleCount(0);
//                    return;
//                }
//
//                particleEmitter.setPosition(bx, by);
//                if (particleEmitter.getMaxParticleCount() == 0) {
//                    particleEmitter.setMaxParticleCount(MAX_PARTICLE_COUNT);
////                    particleEmitter.setMinParticleCount(MAX_PARTICLE_COUNT);
//                }
//                if (by < 7) {
//                    body.setTransform(bx, GameController.WORLD_HEIGHT - 1, body.getAngle());
//                    body.setLinearVelocity(0, 0);
//                }
//            } else {
//                Own.log("empty body: " + fireballId);
//            }
//        }
//    }
//
//    @Override
//    public void afterSceneInit() {
//        fireballs = new HashMap<String, Body>();
//
//        for (int i = 1; i < FIREBALL_COUNT + 1; i++) {
//            String id = "fireball" + i;
//
//
//            Body body = physicsBodyCm.get(idManager.get(id)).body;
//            Array<Fixture> fixtureArray = body.getFixtureList();
//
//            for (int j = 0; j < fixtureArray.size; j++) {
//                fixtureArray.get(j).setUserData(new UserData(id, "Point", body.getPosition()));
//            }
//
//            fireballs.put(id, body);
//        }
//    }
//}


































