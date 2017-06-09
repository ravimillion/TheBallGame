package com.jauntymarble.game.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.jauntymarble.game.GameController;
import com.jauntymarble.game.GameData;
import com.kotcrab.vis.runtime.component.Invisible;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisText;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;


public class ScoringSystem extends BaseSystem implements AfterSceneInit {
    private ComponentMapper<VisID> visIDCm;
    private ComponentMapper<VisText> visTextCm;

    private VisIDManager idManager;
    private GameSaverSystem gameSaverSystem;

    private int score;
    private String currentScoreText = "STAR: 0";
    private String textPrefix = "STAR: ";
    private int counter = 0;
    private int accumulateFactor = 60;
    private float accumulatedFps = 0;

    // variable cache
    private VisText scoreText;
    private VisText fpsText;

    @Override
    protected void processSystem() {
        if (GameController.CURRENT_LEVEL.equals(GameData.ID_LEVEL_TUTORIAL)) return;

        scoreText.setText(this.currentScoreText);

        if (GameData.RELEASE) return;

        // fps calculate
        if (counter >= accumulateFactor) {
            if (fpsText != null)
                fpsText.setText("FPS:" + (int) this.accumulatedFps / accumulateFactor);
            this.accumulatedFps = counter = 0;
        } else {
            this.accumulatedFps += (int) (1 / Gdx.graphics.getDeltaTime());
            counter++;
        }
    }

    public int scoreDown() {
        this.score--;
        this.currentScoreText = this.textPrefix + this.score;

        gameSaverSystem.saveGame();
        return this.score;
    }

    public int scoreUP() {
        this.score++;
        this.currentScoreText = this.textPrefix + this.score;

        gameSaverSystem.saveGame();
        return this.score;
    }

    @Override
    public void afterSceneInit() {
        if (GameController.CURRENT_LEVEL.equals(GameData.ID_LEVEL_TUTORIAL)) return;

        if (gameSaverSystem.getPlayingStatus(GameController.CURRENT_LEVEL).equals(GameData.LEVEL_FINISHED)) {
            this.score = 0;
        } else {
            this.score = gameSaverSystem.getPlayerScore(GameController.CURRENT_LEVEL);
        }
        this.currentScoreText = this.textPrefix + this.score;

        scoreText = visTextCm.get(idManager.get("idScore"));

        if (GameData.RELEASE) {
            // make fps invisible
            Entity fpsEntity = idManager.get("idFPS");
            fpsEntity.edit().add(new Invisible());
        } else {
            fpsText = visTextCm.get(idManager.get("idFPS"));
        }
    }

    public int getScore() {
        return score;
    }
}
