package com.simplegame.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.kotcrab.vis.runtime.component.Invisible;
import com.kotcrab.vis.runtime.component.PhysicsProperties;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.GameData;
import com.simplegame.game.components.Bounds;


public class VisibilitySystem extends EntityProcessingSystem implements AfterSceneInit {
    private ComponentMapper<Transform> transformCm;
    private ComponentMapper<Invisible> invisibleCm;
    private ComponentMapper<Bounds> boundsCm;
    private CameraManager cameraManager;

    // variable cache
    private OrthographicCamera camera;
    private Transform transform;
    private Bounds bounds;

    public VisibilitySystem() {
        super(Aspect.all(VisSprite.class, PhysicsProperties.class));
    }

    @Override
    public void afterSceneInit() {
        camera = cameraManager.getCamera();
    }

    @Override
    protected void process(Entity e) {
        transform = transformCm.get(e);
        bounds = boundsCm.get(e);

        if (transform == null) return;

        float eX = transform.getX();

        float xMax = camera.position.x + GameData.VIEWPORT_WIDTH / 2;
        float xMin = camera.position.x - GameData.VIEWPORT_WIDTH / 2;

        xMin -= bounds != null ? bounds.getWidth() * transform.getScaleX() : 5;
        xMax += bounds != null ? bounds.getWidth() * transform.getScaleX() : 1;

        if (eX > xMin && eX < xMax && isHidden(e)) {
            show(e);
        } else if ((eX < xMin || eX > xMax) && !isHidden(e)) {
            hide(e);
        }
    }

    private boolean isHidden(Entity e) {
        return invisibleCm.getSafe(e) != null;
    }

    private void show(Entity e) {
        e.edit().remove(Invisible.class);
    }

    private void hide(Entity e) {
        e.edit().add(new Invisible());
    }
}
