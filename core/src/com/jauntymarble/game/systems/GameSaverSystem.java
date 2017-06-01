package com.jauntymarble.game.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.jauntymarble.game.GameController;
import com.jauntymarble.game.GameData;
import com.jauntymarble.game.savegame.LevelState;
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

import java.util.HashMap;

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
    // variable cache
    private FileHandle fileHandle = Gdx.files.local(GAME_STATE_PERSIST_FILE);
    private Json json = new Json();
    private boolean saveNow = false;

    @Override
    protected void processSystem() {
        if (!saveNow) return;
        saveNow = false;

        LevelState coldLevelState = getLevelState(GameController.CURRENT_LEVEL);
        // generate hotGame State base
        LevelState hotLevelState = new LevelState(coldLevelState);

        // hot playerScore
        hotLevelState.playerScore = scoringSystem.getScore();
        // hot player position
        if (playerSystem.body != null) {
            hotLevelState.playerPosition = (int) playerSystem.body.getPosition().x;
        }

        // save state if hot and cold are different
        if (hotLevelState.isEqual(coldLevelState) == false) {
            flushData(GameController.CURRENT_LEVEL, hotLevelState);
//            Own.log("updating: " + json.prettyPrint(hotLevelState));
        }
    }

    private void flushData(String levelId, LevelState levelState) {
        HashMap<String, LevelState> states = null;

        if (fileHandle.exists()) {
            states = json.fromJson(HashMap.class, fileHandle);
        } else {
            states = createInitState();
        }
        // replace new state
        states.put(levelId, levelState);
        fileHandle.writeString(json.toJson(states), false);
//        Own.log("Flushing: " + json.prettyPrint(states));
    }

    private HashMap<String, LevelState> createInitState() {
        HashMap<String, LevelState> states = new HashMap<>();

        states.put(GameData.ID_LEVEL_ONE, new LevelState());
        states.put(GameData.ID_LEVEL_TWO, new LevelState());
        states.put(GameData.ID_LEVEL_THREE, new LevelState());
        states.put(GameData.ID_LEVEL_TUTORIAL, new LevelState());

        return states;
    }

    public LevelState getLevelState(String levelId) {
        HashMap<String, LevelState> states;
        if (fileHandle.exists()) {
            states = json.fromJson(HashMap.class, fileHandle);
//            Own.log("Loading from disk: " + states.size() + " data: " + json.toJson(states.get(levelId)));
        } else {
            states = createInitState();
//            Own.log("Creating new: " + json.toJson(states.get(levelId)));
        }

        return states.get(levelId);
    }

    @Override
    public void afterSceneInit() {
//        always start with new state
//        fileHandle.delete();
    }

    public void updatePlayingStatus(String levelId, String status) {
        LevelState levelState = getLevelState(levelId);
        levelState.levelCompletionState = status;
        flushData(levelId, levelState);
    }

    public void updatePlayerPosition(String levelId, int position) {
        LevelState levelState = getLevelState(levelId);
        levelState.playerPosition = position;
        flushData(levelId, levelState);
    }

    public boolean isStateFound() {
        return fileHandle.exists();
    }

    public void saveGame() {
        saveNow = true;
    }

    public int getPlayerScore(String levelId) {
        LevelState levelState = getLevelState(levelId);
        return levelState.playerScore;
    }

    public String getPlayingStatus(String levelId) {
        LevelState levelState = getLevelState(levelId);
        return levelState.levelCompletionState;
    }

    public Vector2 getPlayerPosition(String levelId) {
        LevelState coldLevelState = getLevelState(levelId);
        return new Vector2(coldLevelState.playerPosition, GameData.WORLD_HEIGHT - 5);
    }
}
