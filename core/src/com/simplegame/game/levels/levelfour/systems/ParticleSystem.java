package com.simplegame.game.levels.levelfour.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.physics.box2d.Body;
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

import java.util.HashMap;

public class ParticleSystem extends EntityProcessingSystem implements AfterSceneInit {
    ComponentMapper<VisParticle> visParticleCm;
    ComponentMapper<PhysicsBody> physicsBodyCm;
    ComponentMapper<VisID> visIDCm;
    PhysicsSystem physicsSystem;
    ComponentMapper<Point> pointCm;
    ComponentMapper<Transform> transformCm;

    VisIDManager idManager;
    private HashMap<String, Body> fireballs;
    private RuntimeConfiguration runtimeConfig;

    public ParticleSystem() {
        super(Aspect.one(Point.class, VisParticle.class));
    }

    @Override
    protected void process(Entity e) {
        VisParticle visParticle = visParticleCm.get(e);
        VisID visID = visIDCm.get(e);

        Body body = fireballs.get("fireball1");
        if (visParticle != null) {
            ParticleEmitter particleEmitter = visParticle.getEffect().getEmitters().get(0);
            particleEmitter.setPosition(body.getPosition().x, body.getPosition().y);
        }

        if (body.getPosition().y < 7) {
            body.setTransform(body.getPosition().x, GameController.WORLD_HEIGHT - 1, body.getAngle());
            body.setLinearVelocity(0, 0);
        }
    }

    @Override
    public void afterSceneInit() {
        Entity entity = idManager.get("fireball1");
        fireballs = new HashMap<String, Body>();
        fireballs.put("fireball1", physicsBodyCm.get(entity).body);
    }
}