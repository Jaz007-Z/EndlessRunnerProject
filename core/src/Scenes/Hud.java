package Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class Hud {

    public Stage stage;

    private Viewport viewport;

    public Integer worldTimer = 0;

    public static Integer score;

    public float timeCount = 0.0F;

    public Integer scoreImplement;

    Label countLabel;

    Label scoreLabel;

    Label timeLabel;

    Label scoreTextLabel;

    Label healthLabel;


    public Hud(SpriteBatch sb) {

        score = 0;

        this.scoreImplement = 0;

        this.viewport = new FitViewport(400.0F, 208.0F, new OrthographicCamera());

        this.stage = new Stage(this.viewport, sb);

        Table table = new Table();

        table.bottom();

        table.setFillParent(true);

        this.timeLabel = new Label("TIME", new LabelStyle(new BitmapFont(), Color.WHITE));

        this.scoreTextLabel = new Label("SCORE", new LabelStyle(new BitmapFont(), Color.WHITE));

        this.scoreLabel = new Label(String.format("%d", score), new LabelStyle(new BitmapFont(), Color.WHITE));

        this.countLabel = new Label(String.format("%d", this.worldTimer), new LabelStyle(new BitmapFont(), Color.WHITE));



        table.add(this.timeLabel).padLeft(-300f);
        table.add(this.countLabel).padLeft(-160f);

        table.row();

        table.add(this.scoreTextLabel).padLeft(-280f);
        table.add(this.scoreLabel).padLeft(-160f);

        this.stage.addActor(table);

        Table tableHealth = new Table();
        tableHealth.top();
        tableHealth.setFillParent(true);
        this.healthLabel = new Label("HEALTH", new LabelStyle(new BitmapFont(), Color.WHITE));
        tableHealth.add(this.healthLabel).padLeft(-300f).padTop(5f);
        this.stage.addActor(tableHealth);

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
