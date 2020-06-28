package com.mygdx.game.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Endless;
import com.mygdx.game.screens.Screen;

public class Hud {
    public Stage stage;
    private Viewport viewport;

    private float timeCount;
    private Integer score;
    private Integer level;

    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;

    public Hud(){
        score = 0;
        timeCount = 0;
        level = 1;

        viewport = new FitViewport(Endless.V_WIDTH, Endless.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport);
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));;
        levelLabel = new Label(String.format("%02d", level), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(scoreLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(levelLabel).expandX().padTop(10);

        stage.addActor(table);


    }
}
