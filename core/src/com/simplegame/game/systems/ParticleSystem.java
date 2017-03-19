package com.simplegame.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.kotcrab.vis.runtime.component.Invisible;
import com.kotcrab.vis.runtime.component.OriginalRotation;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.PhysicsProperties;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisParticle;
import com.kotcrab.vis.runtime.component.VisPolygon;
import com.kotcrab.vis.runtime.system.physics.PhysicsBodyManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.GameController;

public class ParticleSystem extends EntitySystem implements AfterSceneInit{

    private ComponentMapper<VisParticle> visParticleCm;
    private ComponentMapper<VisID> visIdCm;
    private ComponentMapper<Invisible> invisibleCm;
    private ComponentMapper<PhysicsBody> physicsCm;
    private ComponentMapper<Transform> transformCm;
    private ComponentMapper<OriginalRotation> originalRotationCm;

    private PhysicsBodyManager physicsBodyManager;
    private VisibilitySystem visibilitySystem;

    // variable cache
    private Body body;
    private ParticleEmitter particleEmitter;
    private Bag<Entity> entityBag;

    public ParticleSystem() {
        super(Aspect.all(VisParticle.class, VisPolygon.class, PhysicsProperties.class));
    }

    @Override
    public void inserted(Entity e) {
        physicsBodyManager.inserted(e);
    }

    @Override
    protected void processSystem() {
        for (Entity e: entityBag) {
            // ask visibility system to fix visibility of in viewport
            visibilitySystem.process(e);

            // if visibility system decided that the object is visible
            if (invisibleCm.get(e) == null) {
                body = physicsCm.get(e).body;
                Vector2 bodyPos = body.getPosition();

                // update position of entity itself
                transformCm.get(e).setPosition(bodyPos.x, bodyPos.y);

                // update position of emitter
                VisParticle visParticle = visParticleCm.get(e);
                particleEmitter = visParticle.getEffect().getEmitters().get(0);
                particleEmitter.setPosition(bodyPos.x, bodyPos.y);

                if (bodyPos.y < 8) {
                    body.setTransform(bodyPos.x, GameController.WORLD_HEIGHT - 1, body.getAngle());
                    body.setLinearVelocity(0, 0);
                    body.setAwake(true);
                }
            }

        }
    }

    @Override
    public void afterSceneInit() {
        entityBag = getEntities();
        // initially hide all the entities and let the visibility system decide which entity needs to be shown
        for (Entity e: entityBag) {
            e.edit().add(new Invisible());
        }
    }
}


































