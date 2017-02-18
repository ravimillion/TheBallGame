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


































