package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayScreen extends Screen {
    private Texture hero;

    protected PlayScreen(GameScreenManager gsm) {
        super(gsm);
        hero = new Texture("hero_example.png");
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
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
