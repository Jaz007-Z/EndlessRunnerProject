package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Screens.MenuScreen;
import Screens.PlayScreen;
import Screens.SplashScreen;

public class Endless extends Game {

	public static final String TITLE = "Endless Runner";
	public SpriteBatch batch;
	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	public static final int V_WIDTH = 500; //adjusted
	public static final int V_HEIGHT = 300;
	//public static final int V_WIDTH = 400; //better zoom this way, can be adjusted
	//public static final int V_HEIGHT = 208;
	//public static final int V_WIDTH = 600;
	//public static final int V_HEIGHT = 390;
	public static final float PPM = 100;
	public static boolean SOUND_ON = true;


	public AssetManager manager;
	public Endless game;
	//Texture img;


	@Override
	public void create () {
		manager = new AssetManager();
		batch = new SpriteBatch();
		game = this;
		manager.load("music/intro.mp3", Music.class);
		manager.load("music/main.mp3", Music.class);

		manager.finishLoading();

		setScreen(new PlayScreen(game, manager));
		//this.setScreen(new SplashScreen(game, manager));
	} //work

	@Override
	public void render () { super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		manager.dispose();
	}

	public void getMenu() {
		this.setScreen(new MenuScreen(this, manager));
	}

}
