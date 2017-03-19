package com.simplegame.game.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisText;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

public class ScoringSystem extends BaseSystem implements AfterSceneInit {
    private ComponentMapper<VisID> visIDCm;
    private ComponentMapper<VisText> visTextCm;

    private VisIDManager idManager;

    private int score;
    private String currentScore = "SCORE: ";
    private int counter = 0;
    private int accumulateFactor = 60;
    private float accumulatedFps = 0;

    // variable cache
    private VisText scoreText;
    private VisText fpsText;

    @Override
    protected void processSystem() {
        scoreText.setText(this.currentScore);

        // fps calculate
        if (counter >= accumulateFactor) {
            fpsText.setText("FPS:" + (int) this.accumulatedFps / accumulateFactor);
            this.accumulatedFps = counter = 0;
        } else {
            this.accumulatedFps += (int) (1 / Gdx.graphics.getDeltaTime());
            counter++;
        }
    }

    public int scoreDown() {
        this.score++;
        this.currentScore = "SCORE: " + this.score;
        return this.score;
    }

    public int scoreUP() {
        this.score--;
        this.currentScore = "SCORE: " + this.score;
        return this.score;
    }

    @Override
    public void afterSceneInit() {
        scoreText = visTextCm.get(idManager.get("idScore"));
        fpsText = visTextCm.get(idManager.get("idFPS"));
    }
}
