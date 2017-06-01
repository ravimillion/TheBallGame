package com.jauntymarble.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.physics.box2d.Body;
import com.brashmonkey.spriter.Player;
import com.kotcrab.vis.plugin.spriter.runtime.component.VisSpriter;
import com.kotcrab.vis.runtime.component.Invisible;
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
    private ComponentMapper<Invisible> invisibleCm;
    private ComponentMapper<Transform> transformCm;
    private ComponentMapper<OriginalRotation> originalRotationCm;
    private PhysicsBodyManager physicsBodyManager;
    private VisibilitySystem visibilitySystem;

    // variable cache
    private Body body;
    private Player player;
    private Bag<Entity> entityBag;

    public SpriterPhysicsSystem() {
        super(Aspect.one(VisSpriter.class));
    }

    @Override
    public void inserted(Entity e) {
        physicsBodyManager.inserted(e);
    }

    @Override
    protected void processSystem() {
        for (Entity e : entityBag) {
            if (e == null) continue;
            // ask visibility system to fix visibility of in viewport
            visibilitySystem.process(e);

            // if visibility system decided that the object is visible
            if (invisibleCm.get(e) == null) {
                PhysicsBody physicsBody = physicsCm.get(e);
                if (physicsBody == null) continue;

                body = physicsCm.get(e).body;
                if (body == null) return;

                player = visSpriterCm.get(e).getPlayer();
                player.setPosition(body.getPosition().x, body.getPosition().y);

                // set rotation #enable if animations are moving.
//                player.setAngle(originalRotationCm.get(e).rotation + body.getAngle() * MathUtils.radiansToDegrees);
            }
        }

    }

    @Override
    public void afterSceneInit() {
        entityBag = getEntities();
        // initially hide all the entities and let the visibility system decide which entity needs to be shown
        for (Entity e : entityBag) {
            e.edit().add(new Invisible());
        }
    }
}
