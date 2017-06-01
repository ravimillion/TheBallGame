package com.jauntymarble.game.savegame;

import com.jauntymarble.game.GameData;

public class LevelState {
    //    game running hot settings
    public int playerScore = -1;
    public float playerPosition = -1;
    //    game completion state
    public String levelCompletionState = null;

    public LevelState() {
        this.levelCompletionState = GameData.LEVEL_NOT_PLAYED;
        this.playerScore = 0;
        this.playerPosition = 5;
    }

    // copy constructor
    public LevelState(LevelState levelState) {
        this.levelCompletionState = levelState.levelCompletionState;
        this.playerScore = levelState.playerScore;
        this.playerPosition = levelState.playerPosition;
    }

    // is hot and cold game states are same
    public boolean isEqual(LevelState levelState) {
        if (playerPosition != levelState.playerPosition) return false;
        if (playerScore != levelState.playerScore) return false;

        return true;
    }
}
