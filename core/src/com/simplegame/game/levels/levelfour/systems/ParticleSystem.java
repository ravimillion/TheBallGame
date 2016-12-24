package com.simplegame.game.levels.levelfour.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.kotcrab.vis.runtime.RuntimeConfiguration;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisParticle;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by ravi on 13.11.16.
 */

public class ParticleSystem extends EntityProcessingSystem implements AfterSceneInit {
    ComponentMapper<VisParticle> visParticleCm;
    ComponentMapper<Transform> transformCm;

    PlayerSystem playerSystem;

    private RuntimeConfiguration runtimeConfig;


    public ParticleSystem() {
        super(Aspect.all(VisParticle.class));
    }

    @Override
    protected void process(Entity e) {
        ParticleEmitter particleEmitter = visParticleCm.get(e).getEffect().getEmitters().get(0);
        particleEmitter.setContinuous(true);
        particleEmitter.setPosition(playerSystem.body.getPosition().x, particleEmitter.getY());
    }

    @Override
    public void afterSceneInit() {

    }
}
