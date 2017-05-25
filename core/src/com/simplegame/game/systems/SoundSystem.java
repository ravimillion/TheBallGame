package com.simplegame.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisSound;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

import java.util.HashMap;


public class SoundSystem extends EntitySystem implements AfterSceneInit {
    private ComponentMapper<Transform> transformCm;
    private ComponentMapper<VisSound> soundCm;
    private ComponentMapper<VisID> visIDCm;

    private VisIDManager idManager;
    private CameraManager cameraManager;

    // variable cache
    private OrthographicCamera camera;
    private HashMap<String, VisSound> effectMap;

    public SoundSystem() {
        super(Aspect.all(VisSound.class));
    }

    @Override
    public void afterSceneInit() {
        camera = cameraManager.getCamera();

        effectMap = new HashMap<String, VisSound>();

        // cache all the loaded sounds
        Bag<Entity> entityBag = getEntities();
        for (Entity e : entityBag) {
            effectMap.put(visIDCm.get(e).id, soundCm.get(e));
        }
    }

    public void triggerSoundEffect(PhysicsBodyContactSystem.CollisionData collisionData) {
        final VisID visID = visIDCm.get(collisionData.entity);
        if (visID == null || true) return;


        switch (visID.id) {
            case "idWoodBox":
                if (collisionData.impulse > 100) effectMap.get("idSoundWoodBox").play();
                break;
            case "idUphill":
            case "idBoundary":
                if (collisionData.impulse > 100) effectMap.get("idSoundGlass").play();
                break;
            case "idSpike":
                if (collisionData.impulse > 0) effectMap.get("idSoundGlassBreak").play();
                break;
        }
    }

    @Override
    protected void processSystem() {
    }
}
