package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Screens.MenuScreen;
import Screens.PlayScreen;

public class Endless extends Game {

	public SpriteBatch batch;
	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	public static final int V_WIDTH = 600; //screwed up, needs fixing
	public static final int V_HEIGHT = 390;
	public static final float PPM = 100;



	public AssetManager manager;

	//Texture img;


	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MenuScreen(this));
	} //work

	@Override
	public void render () { super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
