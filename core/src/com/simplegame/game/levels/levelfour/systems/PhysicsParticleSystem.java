package com.simplegame.game.levels.levelfour.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.kotcrab.vis.runtime.RuntimeConfiguration;
import com.kotcrab.vis.runtime.component.PhysicsProperties;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisParticle;
import com.kotcrab.vis.runtime.component.VisPolygon;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by ravi on 13.11.16.
 */

public class PhysicsParticleSystem extends EntityProcessingSystem implements AfterSceneInit {
    ComponentMapper<VisParticle> visParticleCm;
    ComponentMapper<Transform> transformCm;

    private RuntimeConfiguration runtimeConfig;


    public PhysicsParticleSystem() {
        super(Aspect.all(PhysicsProperties.class, VisPolygon.class, VisParticle.class));
//        this.runtimeConfig = runtimeConfig;
    }

    @Override
    protected void process(Entity e) {
//        float y = visParticleCm.get(e).getEffect().getEmitters().get(0).getY();
//        y -= 0.1f;
//        if (y < 6.0f) y = 32f;
//
//        visParticleCm.get(e).getEffect().getEmitters().get(0).setPosition(visParticleCm.get(e).getEffect().getEmitters().get(0).getX(), y);
    }

    @Override
    public void afterSceneInit() {

    }
}
