package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Endless;

import jdk.nashorn.internal.runtime.regexp.joni.ast.EncloseNode;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//<<<<<<< HEAD
		config.foregroundFPS = 60;
		config.width = Endless.V_WIDTH;
		config.height = Endless.V_HEIGHT;
		config.resizable = false;
//=======
		config.width = Endless.V_WIDTH;
		config.height = Endless.V_HEIGHT;
		config.title = Endless.TITLE;
//>>>>>>> c3f1ffd040eb8b9e9abd1d6f5a4c1b2427df1d56
		new LwjglApplication(new Endless(), config);
	}
}
