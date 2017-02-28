package com.simplegame.game.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisText;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

public class ScoringSystem extends EntityProcessingSystem implements AfterSceneInit {
    private ComponentMapper<VisID> visIDCm;
    private ComponentMapper<VisText> visTextCm;

    private VisIDManager visIDManager;
    private int currentScore = 0;
    private OrthographicCamera camera = null;
    private SpriteBatch spriteBatch = null;

    public ScoringSystem() {
        super(Aspect.all(VisText.class));
    }

    @Override
    protected void process(Entity e) {
        VisID visID = visIDCm.get(e);

        if (visID != null && visID.id.equals("idScore")) {
            VisText visText = visTextCm.get(e);
            visText.setText("SCORE: " + String.valueOf(this.currentScore));
        }
    }

    public int scoreDown() {
        return currentScore--;
    }

    public void scoreUP() {
        this.currentScore++;
    }

    @Override
    public void afterSceneInit() {
    }
}
