package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.scenes.Hud;

public class PlayScreen extends Screen {
    private Texture hero;
    private Hud hud;

    public PlayScreen(GameScreenManager gsm) {
        super(gsm);
        hero = new Texture("hero_example.png");
        hud = new Hud();
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()) {
            gsm.set(new PlayScreen(gsm));
            dispose();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(hero, 0, 0);
        sb.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
