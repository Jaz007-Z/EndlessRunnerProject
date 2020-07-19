package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Screens.MenuScreen;
import Screens.SplashScreen;

/**
 * @author      Jimmy Zimsky, Dallas Eaton, Elias Moreira, Nathaniel Snow 
 * @version     1.0                         
 */

public class Endless extends Game {

	public static final String TITLE = "Endless Runner";
	public SpriteBatch batch;
	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	public static final int V_WIDTH = 500; //better zoom this way, can be adjusted
	public static final int V_HEIGHT = 300;
	public static final float PPM = 100;
	public static boolean SOUND_ON = true;

	public AssetManager manager;
	public Endless game;
	
	//Box2D Collision Bits
	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short FIRE_BIT = 4;
	public static final short COIN_BIT = 8;


	/**
 	* loads the assets
 	*/
	@Override
	public void create () {
		manager = new AssetManager();
		batch = new SpriteBatch();
		game = this;
		manager.load("music/intro.mp3", Music.class);
		manager.load("music/main.mp3", Music.class);

		manager.finishLoading();
		this.setScreen(new SplashScreen(game, manager));
	} //work

	@Override
	public void render () { super.render();
	}
	

	/**
 	* disposes the assets
 	*/
	@Override
	public void dispose () {
		batch.dispose();
		manager.dispose();
	}

	public void getMenu() {
		this.setScreen(new MenuScreen(this, manager));
	}

}
