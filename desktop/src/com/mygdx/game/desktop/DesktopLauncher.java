package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Endless;

import jdk.nashorn.internal.runtime.regexp.joni.ast.EncloseNode;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Endless.V_WIDTH;
		config.height = Endless.V_HEIGHT;
		config.title = Endless.TITLE;
		new LwjglApplication(new Endless(), config);
	}
}
