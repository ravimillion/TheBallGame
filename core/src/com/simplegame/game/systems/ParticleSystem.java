package com.simplegame.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.runtime.RuntimeConfiguration;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Point;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisParticle;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.GameController;
import com.simplegame.game.userdata.UserData;

import java.util.HashMap;

import ownLib.Own;

public class ParticleSystem extends EntityProcessingSystem implements AfterSceneInit {
    ComponentMapper<VisParticle> visParticleCm;
    ComponentMapper<PhysicsBody> physicsBodyCm;
    ComponentMapper<VisID> visIDCm;
    PhysicsSystem physicsSystem;
    ComponentMapper<Point> pointCm;
    ComponentMapper<Transform> transformCm;
    PlayerSystem playerSystem;

    VisIDManager idManager;
    Body body = null;
    int FIREBALL_COUNT = 28;
    int MAX_PARTICLE_COUNT = 500;
    private HashMap<String, Body> fireballs;
    private RuntimeConfiguration runtimeConfig;

    public ParticleSystem() {
        super(Aspect.one(Point.class, VisParticle.class));
    }

    @Override
    protected void process(Entity e) {
        VisID visID = visIDCm.get(e);
        if (visID.id.equals("boundary")) return;

        float minRange = playerSystem.body.getPosition().x - GameController.VIEWPORT_WIDTH / 2;
        float maxRange = playerSystem.body.getPosition().x + GameController.VIEWPORT_WIDTH / 2;

        VisParticle visParticle = visParticleCm.get(e);

        body = null;
        if (visParticle != null) {
            String fireballId = "fireball" + visID.id.replace("partical", "");
            body = fireballs.get(fireballId);
            if (body != null) {
                float bx = body.getPosition().x;
                float by = body.getPosition().y;

                ParticleEmitter particleEmitter = visParticle.getEffect().getEmitters().get(0);

                if (bx < minRange || bx > maxRange) {
                    particleEmitter.setMaxParticleCount(0);
                    return;
                }

                particleEmitter.setPosition(bx, by);
                if (particleEmitter.getMaxParticleCount() == 0) {
                    particleEmitter.setMaxParticleCount(MAX_PARTICLE_COUNT);
//                    particleEmitter.setMinParticleCount(MAX_PARTICLE_COUNT);
                }
                if (by < 7) {
                    body.setTransform(bx, GameController.WORLD_HEIGHT - 1, body.getAngle());
                    body.setLinearVelocity(0, 0);
                }
            } else {
                Own.log("empty body: " + fireballId);
            }
        }
    }

    @Override
    public void afterSceneInit() {
        fireballs = new HashMap<String, Body>();

        for (int i = 1; i < FIREBALL_COUNT + 1; i++) {
            String id = "fireball" + i;


            Body body = physicsBodyCm.get(idManager.get(id)).body;
            Array<Fixture> fixtureArray = body.getFixtureList();

            for (int j = 0; j < fixtureArray.size; j++) {
                fixtureArray.get(j).setUserData(new UserData(id, "Point", body.getPosition()));
            }

            fireballs.put(id, body);
        }
    }
}


































