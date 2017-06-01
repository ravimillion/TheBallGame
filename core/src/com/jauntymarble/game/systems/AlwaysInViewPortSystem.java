package com.jauntymarble.game.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.jauntymarble.game.GameController;
import com.jauntymarble.game.GameData;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

import java.util.HashMap;


public class AlwaysInViewPortSystem extends BaseSystem implements AfterSceneInit {
    private ComponentMapper<Transform> transformCm;

    private VisIDManager idManager;
    private CameraManager cameraManager;
    private HashMap<String, Float> posMap = new HashMap<String, Float>();
    private HashMap<String, Entity> entityMap = new HashMap<String, Entity>();
    private String[] entityIds = {"idScore", "idBackground", "idFPS"};

    // variable cache
    private OrthographicCamera camera;

    @Override
    public void afterSceneInit() {
        if (GameController.CURRENT_LEVEL.equals(GameData.ID_LEVEL_TUTORIAL)) return;

        for (int i = 0, len = entityIds.length; i < len; i++) {
            String entityId = entityIds[i];
            Entity entity = idManager.get(entityId);

            entityMap.put(entityId, idManager.get(entityId));
            posMap.put(entityId, transformCm.get(entity).getX() - cameraManager.getCamera().viewportWidth / 2);
        }

        camera = cameraManager.getCamera();
    }

    public void setControlsPosition() {
        for (int i = 0; i < entityIds.length; i++) {
            String id = entityIds[i];
            transformCm.get(entityMap.get(id)).setX(camera.position.x + posMap.get(entityIds[i]));
        }
    }

    @Override
    protected void processSystem() {
        if (GameController.CURRENT_LEVEL.equals(GameData.ID_LEVEL_TUTORIAL)) return;

        setControlsPosition();
    }
}
