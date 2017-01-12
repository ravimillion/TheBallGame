package com.simplegame.game.levels.levelfour.systems;

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

public class ParticleSystem extends EntityProcessingSystem implements AfterSceneInit {
    ComponentMapper<VisParticle> visParticleCm;
    ComponentMapper<PhysicsBody> physicsBodyCm;
    ComponentMapper<VisID> visIDCm;
    PhysicsSystem physicsSystem;
    ComponentMapper<Point> pointCm;
    ComponentMapper<Transform> transformCm;

    VisIDManager idManager;
    Body body = null;
    int FIREBALL_COUNT = 3;
    private HashMap<String, Body> fireballs;
    private RuntimeConfiguration runtimeConfig;
    public ParticleSystem() {
        super(Aspect.one(Point.class, VisParticle.class));
    }

    @Override
    protected void process(Entity e) {
        VisID visID = visIDCm.get(e);
        if (visID.id.equals("boundary")) return;

        VisParticle visParticle = visParticleCm.get(e);

        body = null;
        if (visParticle != null) {
            body = fireballs.get("fireball" + visID.id.replace("partical", ""));
            ParticleEmitter particleEmitter = visParticle.getEffect().getEmitters().get(0);
            particleEmitter.setPosition(body.getPosition().x, body.getPosition().y);
        }

        if (body != null && body.getPosition().y < 7) {
            body.setTransform(body.getPosition().x, GameController.WORLD_HEIGHT - 1, body.getAngle());
            body.setLinearVelocity(0, 0);
        }
    }

    @Override
    public void afterSceneInit() {
        fireballs = new HashMap<String, Body>();

        for(int i = 1; i < FIREBALL_COUNT + 1; i++) {
            String id =  "fireball" + i;

            Body body = physicsBodyCm.get(idManager.get(id)).body;
            Array<Fixture> fixtureArray = body.getFixtureList();

            for (int j = 0; j < fixtureArray.size; j++) {
                fixtureArray.get(j).setUserData(new UserData(id, "Point", body.getPosition()));
            }

            fireballs.put(id, body);
        }
    }
}


































