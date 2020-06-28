package com.mygdx.game.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Endless;

import javax.swing.JViewport;

public abstract class Screen {

    protected OrthographicCamera cam;
    protected GameScreenManager gsm;
    protected Vector3 mouse;
    protected Viewport port;

    protected Screen(GameScreenManager gsm) {
        this.gsm = gsm;
        cam = new OrthographicCamera();
        port = new FitViewport(Endless.V_WIDTH, Endless.V_HEIGHT, cam);
        mouse = new Vector3();
    }

    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}
