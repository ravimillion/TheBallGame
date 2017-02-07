package com.simplegame.game.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.components.Bounds;

import java.util.HashMap;


public class AlwaysInViewPortSystem extends BaseSystem implements AfterSceneInit {
    private ComponentMapper<Bounds> boundsCm;
    private ComponentMapper<VisSprite> spriteCm;
    private ComponentMapper<Transform> transformCm;

    private VisIDManager idManager;
    private CameraManager cameraManager;
    private HashMap<String, Float> posMap = new HashMap<String, Float>();
    private HashMap<String, Entity> entityMap = new HashMap<String, Entity>();
    private HashMap<String, Bounds> boundsMap = new HashMap<String, Bounds>();
    private String[] entityIds = {"idBackground"};

    @Override
    public void afterSceneInit() {
        for (int i = 0; i < entityIds.length; i++) {
            String entityId = entityIds[i];
            Entity entity = idManager.get(entityId);

            entityMap.put(entityId, idManager.get(entityId));
            boundsMap.put(entityId, boundsCm.get(entity));
            posMap.put(entityId, transformCm.get(entity).getX() - cameraManager.getCamera().viewportWidth / 2);
        }

    }

    public void setControlsPosition() {
        Entity entity;
        Bounds bounds;
        Transform transform;

        Object[] keys = entityMap.keySet().toArray();

        for (Object id : keys) {
            float newX = cameraManager.getCamera().position.x + posMap.get(id.toString());
            entity = entityMap.get(id.toString());
            bounds = boundsCm.get(entity);
            bounds.setX(newX);

            transform = transformCm.get(entity);
            transform.setX(newX);
            cameraManager.getCamera().update();
        }
    }

    @Override
    protected void processSystem() {
        setControlsPosition();
    }
}
