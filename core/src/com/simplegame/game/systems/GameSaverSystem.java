package com.simplegame.game.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.runtime.component.Invisible;
import com.kotcrab.vis.runtime.component.OriginalRotation;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.Variables;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisParticle;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.GameData;
import com.simplegame.game.savegame.GameState;

import ownLib.Own;

/**
 * Created by ravi on 27.05.17.
 */

public class GameSaverSystem extends BaseSystem implements AfterSceneInit {
    public static final String GAME_STATE_PERSIST_FILE = "game_state.json";
    private ComponentMapper<VisParticle> visParticleCm;
    private ComponentMapper<VisID> visIdCm;
    private ComponentMapper<Invisible> invisibleCm;
    private ComponentMapper<PhysicsBody> physicsCm;
    private ComponentMapper<Transform> transformCm;
    private ComponentMapper<Variables> variableCm;
    private ComponentMapper<OriginalRotation> originalRotationCm;
    private VisIDManager idManager;
    private PlayerSystem playerSystem;
    private ScoringSystem scoringSystem;
    private CameraManager cameraManager;
    private VisibilitySystem visibilitySystem;
    private GameState coldGameState = null;
    // variable cache
    private FileHandle fileHandle = Gdx.files.local(GAME_STATE_PERSIST_FILE);
    private Json json = new Json();
    private boolean saveNow = false;

    @Override
    protected void processSystem() {
        if (!saveNow) return;
        saveNow = false;

        coldGameState = getColdGameState();
        // generate hotGame State base
        GameState hotGameState = new GameState(coldGameState);

        // hot playerScore
        hotGameState.playerScore = scoringSystem.getScore();
        // hot player position
        if (playerSystem.body != null) {
            hotGameState.playerPosition = (int) playerSystem.body.getPosition().x;
        }

        // save state if hot and cold are different
        if (hotGameState.isEqual(coldGameState) == false) {
            coldGameState = hotGameState;

            saveToDisk(hotGameState);
            Own.log("Saving: " + json.toJson(hotGameState));
        }
    }

    private void saveToDisk(GameState gameState) {
        fileHandle.writeString(json.toJson(gameState), false);
    }

    public GameState createInitState() {
        GameState gameState = new GameState();
        gameState.tutorial = GameData.LEVEL_NOT_YET_PLAYED;
        gameState.levelOne = GameData.LEVEL_NOT_YET_PLAYED;
        gameState.levelTwo = GameData.LEVEL_NOT_YET_PLAYED;
        gameState.levelThree = GameData.LEVEL_NOT_YET_PLAYED;

        // game running hot settings
        gameState.playerScore = 0;
        gameState.playerPosition = 5;
        saveToDisk(gameState);
        return gameState;
    }

    public GameState getColdGameState() {
        if (coldGameState != null) return coldGameState;

        if (fileHandle.exists()) {
            coldGameState = json.fromJson(GameState.class, fileHandle);
            Own.log("Loading from disk: " + json.toJson(coldGameState));
        } else {
            coldGameState = createInitState();
        }

        return coldGameState;
    }

    @Override
    public void afterSceneInit() {
        // always start with new state
//        fileHandle.delete();
    }

    public void updateLevelStatus(int currentLevel, int status) {
        GameState gameState = getColdGameState();
        switch (currentLevel) {
            case GameData.LEVEL_ONE:
                gameState.levelOne = status;
                break;
            case GameData.LEVEL_TWO:
                gameState.levelTwo = status;
                break;
            case GameData.LEVEL_THREE:
                gameState.levelThree = status;
                break;
            case GameData.LEVEL_TUTORIAL:
                gameState.tutorial = status;
                break;
        }
        saveToDisk(gameState);
    }

    public boolean isStateFound() {
        return fileHandle.exists();
    }

    public void saveGame() {
        saveNow = true;
    }

    public int getStatusForLevel(int currentLevel) {
        GameState gameState = getColdGameState();

        switch (currentLevel) {
            case GameData.LEVEL_ONE:
                return gameState.levelOne;
            case GameData.LEVEL_TWO:
                return gameState.levelTwo;
            case GameData.LEVEL_THREE:
                return gameState.levelThree;
            case GameData.LEVEL_TUTORIAL:
                return gameState.tutorial;
        }

        return 0;
    }

    public Vector2 getPlayerPosition() {
        GameState coldGameState = getColdGameState();
        return new Vector2(coldGameState.playerPosition, GameData.WORLD_HEIGHT - 5);
    }
}
