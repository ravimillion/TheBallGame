package com.jauntymarble.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.jauntymarble.game.GameData;
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

    public void triggerSoundEffect(String soundId) {
        if (GameData.VOLUME <= 0f) return;

        effectMap.get(soundId).play();
    }

    @Override
    protected void processSystem() {
    }
}
