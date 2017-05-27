package com.simplegame.game.savegame;

public class GameState {
    // game completion state
    public int tutorial = -1;
    public int levelOne = -1;
    public int levelTwo = -1;
    public int levelThree = -1;

    // game running hot settings
    public int playerScore = 0;
    public float playerPosition = 5;

    public GameState() {

    }

    public GameState(GameState gameState) {
        this.tutorial = gameState.tutorial;
        this.levelOne = gameState.levelOne;
        this.levelTwo = gameState.levelTwo;
        this.levelThree = gameState.levelThree;
        this.playerScore = gameState.playerScore;
        this.playerPosition = gameState.playerPosition;
    }

    public boolean isEqual(GameState gameState) {
        if (playerPosition != gameState.playerPosition) return false;
        if (playerScore != gameState.playerScore) return false;

        return true;
    }
}
