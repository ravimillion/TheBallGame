package com.simplegame.game.levels;

import com.uwsoft.editor.renderer.Overlap2DStage;

/**
 * Created by ravi on 23.10.16.
 */

public class LevelFour extends Overlap2DStage {

    public LevelFour() {
        initSceneLoader();

        sceneLoader.loadScene("MainScene");

        // Get the root actor and add it to stage
        addActor(sceneLoader.getRoot());
    }
}
