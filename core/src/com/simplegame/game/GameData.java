package com.simplegame.game;

import com.badlogic.gdx.utils.Array;

/**
 * Created by ravi on 06.10.16.
 */

public class GameData {
    public static final int READY = 0;
    public static final int RUNNING = 1;
    public static final int PAUSED = 2;
    public static final int LEVEL_END = 3;
    public static final int GAME_OVER = 4;
    public static final int QUIT = 5;
    public static final int RESTART_LEVEL = 6;

    // splash screen
    public static final int SPLASH_SHOW_DELAY = 0;
    public static final int SPLASH_HIDE_DELAY = 0;

    // game running info
    public static final int LEVEL_ONE = 1;
    public static final int LEVEL_TWO = 2;
    public static final int LEVEL_THREE = 3;
    public static final int MENU_SCREEN = 4;

    public static final String MENU = "scene/menu.scene";
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
}
