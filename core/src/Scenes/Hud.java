package Scenes;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Screens.PlayScreen;
import Screens.Screen;

/**
 * @author      Jimmy Zimsky, Dallas Eaton, Elias Moreira, Nathaniel Snow 
 * @version     1.0                         
 */

public class Hud {

    public Stage stage;

    private Viewport viewport;

    public Integer worldTimer = 0;
    public Integer coins;

    public static Integer score;

    public float timeCount = 0.0F;

    public Integer scoreImplement;

    Label countLabel;
    Label scoreLabel;
    Label timeLabel;
    Label scoreTextLabel;
    Label healthLabel;
    Label coinsLabel;

    public Hud(SpriteBatch sb, PlayScreen screen) {

        score = 0;
        coins = 0;

        this.scoreImplement = 0;
        this.viewport = new FitViewport(400.0F, 208.0F, new OrthographicCamera());
        this.stage = new Stage(this.viewport, sb);

        Table table = new Table();

        table.bottom();
        table.setFillParent(true);
        table.background(screen.background);

        //creates the time and score labels for the hud
        this.timeLabel = new Label("TIME", new LabelStyle(new BitmapFont(), Color.WHITE));
        this.scoreTextLabel = new Label("SCORE", new LabelStyle(new BitmapFont(), Color.WHITE));
        this.scoreLabel = new Label(String.format("%d", score), new LabelStyle(new BitmapFont(), Color.WHITE));
        this.countLabel = new Label(String.format("%d", this.worldTimer), new LabelStyle(new BitmapFont(), Color.WHITE));


        //adds labels
        table.add(this.timeLabel).padLeft(-300f);
        table.add(this.countLabel).padLeft(-160f);

        table.row();

        table.add(this.scoreTextLabel).padLeft(-280f);
        table.add(this.scoreLabel).padLeft(-160f);

        this.stage.addActor(table);

        //coin amounts on the hud
        Table table2 = new Table();
        table2.top();
        table2.setFillParent(true);
        this.coinsLabel = new Label(String.format("%d", coins), new LabelStyle(new BitmapFont(), Color.WHITE));
        table2.add(this.coinsLabel).padRight(-285f).padBottom(-47f);
        this.stage.addActor(table2);

    }

    public void update(float dt) {

        this.timeCount += dt;

        if (this.timeCount >= 1.0F) {
            Integer var2 = this.worldTimer;
            Integer var3 = this.worldTimer = this.worldTimer + 1;

            this.countLabel.setText(String.format("%d", this.worldTimer));
            this.timeCount = 0.0F;

            var2 = this.scoreImplement;
            var3 = this.scoreImplement = this.scoreImplement + 1;

        }

        //changes the score
        if (this.scoreImplement == 5) {
            score = score + 100;
            this.scoreLabel.setText(String.format("%d", score));
            this.scoreImplement = 0;

        }

    }


    public void dispose() {

        this.stage.dispose();

    }

}
