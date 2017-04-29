package com.simplegame.game.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.runtime.component.Invisible;
import com.kotcrab.vis.runtime.component.OriginalRotation;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.Variables;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisParticle;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

import static java.lang.Float.parseFloat;

public class MovingPlatform extends BaseSystem implements AfterSceneInit {
    private ComponentMapper<VisParticle> visParticleCm;
    private ComponentMapper<VisID> visIdCm;
    private ComponentMapper<Invisible> invisibleCm;
    private ComponentMapper<PhysicsBody> physicsCm;
    private ComponentMapper<Transform> transformCm;
    private ComponentMapper<Variables> variableCm;
    private ComponentMapper<OriginalRotation> originalRotationCm;

    private VisIDManager idManager;
    private PlayerSystem playerSystem;
    private CameraManager cameraManager;
    private VisibilitySystem visibilitySystem;

    // variable cache
    private Array<Entity> platforms;
    private float MIN_HEIGHT = 8;
    private float MAX_HEIGHT = 20;

    @Override
    protected void processSystem() {
        for (int i = 0, len = platforms.size; i < len; i++) {
            Entity e = platforms.get(i);
            visibilitySystem.process(e);

            if (invisibleCm.get(e) == null) {
                PhysicsBody physicsBody = physicsCm.get(e);
                Body body = physicsBody.body;
                Vector2 pos = body.getPosition();
                String dir = variableCm.get(e).get("dir");
                float speed = parseFloat(variableCm.get(e).get("speed"));

                if (dir.equals("up")) {
                    if (pos.y > MAX_HEIGHT) {
                        variableCm.get(e).put("dir", "down");
                    } else {
                        body.setTransform(pos.x, pos.y + speed, body.getAngle());
                    }
                } else if (dir.equals("down")) {
                    if (pos.y < MIN_HEIGHT) {
                        variableCm.get(e).put("dir", "up");
                    } else {
                        body.setTransform(pos.x, pos.y - speed, body.getAngle());
                    }
                }
//                Note: Need to set the anchor position for this
//                else if (dir.equals("left")) {
//                    if (pos.y < MIN_HEIGHT) {
//                        body.setTransform(pos.x, MAX_HEIGHT, body.getAngle());
//                    } else {
//                        body.setTransform(pos.x, pos.y - SPEED, body.getAngle());
//                    }
//                } else if (dir.equals("right")) {
//                    if (pos.y < MIN_HEIGHT) {
//                        body.setTransform(pos.x, MAX_HEIGHT, body.getAngle());
//                    } else {
//                        body.setTransform(pos.x, pos.y - SPEED, body.getAngle());
//                    }
//                }
            }
        }
    }

    @Override
    public void afterSceneInit() {
        platforms = idManager.getMultiple("idMovingPlatform");
    }
}
