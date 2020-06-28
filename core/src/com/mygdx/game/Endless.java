package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.mygdx.game.screens.GameScreenManager;
import com.mygdx.game.screens.MenuScreen;

public class Endless extends Game {

	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	public static final int V_WIDTH = 700;
	public static final int V_HEIGHT = 350;
	public static final String TITLE = "EndlessRunner";

	private GameScreenManager gsm;
	public SpriteBatch batch;


	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameScreenManager();
		gsm.push(new MenuScreen(gsm));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
