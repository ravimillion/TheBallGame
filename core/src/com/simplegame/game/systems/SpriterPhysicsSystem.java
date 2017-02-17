package com.simplegame.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.math.MathUtils;
import com.kotcrab.vis.plugin.spriter.runtime.component.VisSpriter;
import com.kotcrab.vis.runtime.component.OriginalRotation;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.system.physics.PhysicsBodyManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by ravi on 17.02.17.
 */
public class SpriterPhysicsSystem extends EntitySystem implements AfterSceneInit {

    private ComponentMapper<VisID> visIdCm;
    private ComponentMapper<PhysicsBody> physicsCm;
    private ComponentMapper<VisSpriter> visSpriterCm;
    private ComponentMapper<Transform> transformCm;
    private ComponentMapper<OriginalRotation> originalRotationCm;
    private PhysicsBodyManager physicsBodyManager;

    public SpriterPhysicsSystem() {
        super(Aspect.one(VisSpriter.class));
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

            VisSpriter visSpriter = visSpriterCm.get(e);
            visSpriter.getPlayer().setPosition(physics.body.getPosition().x, physics.body.getPosition().y);
            OriginalRotation originalRotation = originalRotationCm.get(e);
            visSpriter.getPlayer().setAngle(originalRotation.rotation + physics.body.getAngle() * MathUtils.radiansToDegrees);
        }

    }

    @Override
    public void afterSceneInit() {

    }
}
