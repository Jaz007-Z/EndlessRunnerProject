package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Endless;

public class MenuScreen extends Screen {
    private Texture background;
    private Texture playBtn;
    public MenuScreen(GameScreenManager gsm) {
        super(gsm);
        cam.setToOrtho(false, Endless.V_WIDTH, Endless.V_HEIGHT);
        background = new Texture("background.jpg");
        playBtn = new Texture("startButton.png");
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
            gsm.set(new PlayScreen(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, 0, 0, Endless.V_WIDTH, Endless.V_HEIGHT);
        sb.draw(playBtn, Endless.V_WIDTH / 2 - playBtn.getWidth() / 2, Endless.V_HEIGHT / 3 - playBtn.getHeight() / 2);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
        System.out.println("Menu State Disposed");
    }
}
