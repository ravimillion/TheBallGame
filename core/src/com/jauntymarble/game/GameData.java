package com.jauntymarble.game;

import com.badlogic.gdx.utils.Array;

/**
 * Created by ravi on 06.10.16.
 */

public class GameData {
    public static final boolean RELEASE = true;
    public static final boolean SHOW_ADS = false;
    // game states
    public static final int READY = 0;
    public static final int RUNNING = 1;
    public static final int PAUSED = 2;
    public static final int LEVEL_END = 3;
    public static final int GAME_OVER = 4;
    public static final int QUIT = 5;
    public static final int RESTART_LEVEL = 6;

    // game running info
    public static final String ID_MENU = "ID_MENU";
    public static final String ID_LEVEL_ONE = "ID_LEVEL_ONE";
    public static final String ID_LEVEL_TWO = "ID_LEVEL_TWO";
    public static final String ID_LEVEL_THREE = "ID_LEVEL_THREE";
    public static final String ID_LEVEL_TUTORIAL = "ID_LEVEL_TUTORIAL";
    // CURRENT_LEVEL info
    public static final String LEVEL_NOT_PLAYED = "NOT_PLAYED";
    public static final String LEVEL_IN_PROGRESS = "IN_PROGRESS";
    public static final String LEVEL_FINISHED = "FINISHED";

    public static final String SCENE_MENU = "scene/menu.scene";
    public static final String SCENE_TUTORIAL = "scene/tutorial.scene";
    public static final String SCENE_ONE = "scene/levelone.scene";
    public static final String SCENE_TWO = "scene/leveltwo.scene";
    public static final String SCENE_THREE = "scene/levelthree.scene";
    // add ids of super powers
    public static final Array<String> SUPER_POWER_LIST = new Array(
            new String[]{"idPowerSize", "idPowerBounce", "idPowerGravity"}
    );
    public static final Array<String> CAMERA_SHAKE_LIST = new Array(
            new String[]{"idSpike", "idFire"}
    );
    public static final Array<String> STATE_CHANGE_LIST = new Array(
            new String[]{"idSpike", "idFire"}
    );
    public static final Array<String> REMOVE_ON_COLLISIOIN_LIST = new Array(
            new String[]{"idAnimStar", "idPowerBounce", "idPowerGravity", "idPowerSize"}
    );
    // GAME DATA
    public static int WORLD_WIDTH = 573;
    public static int WORLD_HEIGHT = 32;
    public static int VIEWPORT_WIDTH = 53;


    public static float VOLUME = 0.5f;

    public static float getConfigData(String KEY) {
        switch (KEY) {
            case "SPLASH_SHOW_DELAY":
                return GameData.RELEASE ? 3f : 0;
            case "SPLASH_HIDE_DELAY":
                return GameData.RELEASE ? 2f : 0;
        }

        return 0;
    }
}
