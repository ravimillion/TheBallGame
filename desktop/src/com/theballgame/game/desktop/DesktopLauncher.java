package com.theballgame.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.simplegame.game.screens.GameEntry;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.height = 32*15;
		config.width = 32*15*16/9;
//		config.fullscreen = true;
        GameEntry gameEntry = new GameEntry(null);
        gameEntry.debugMode(true);

		new LwjglApplication(gameEntry, config);
	}
}
