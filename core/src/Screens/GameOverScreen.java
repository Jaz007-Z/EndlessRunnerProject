package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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
import com.mygdx.game.Endless;


public class GameOverScreen implements Screen {

    public Stage stage;

    private Viewport viewport;

    Endless game;

    public AssetManager manager;
    int score;
    int highscore;
    Label gameOverLabel;
    Label gameOverLabel2;
    Label scoreLabel;
    Label highscoreLabel;
    Label scoreTitle;
    Label highscoreTitle;


    public GameOverScreen(Endless game, AssetManager manager, int score, SpriteBatch sb) {

        this.manager = manager;
        this.game = game;
        this.score = score;

        Preferences prefs = Gdx.app.getPreferences("endlessGame");

        this.highscore = prefs.getInteger("highscore", 0);

        if (score > this.highscore) {

            prefs.putInteger("highscore", score);
            prefs.flush();

            this.highscoreLabel = new Label(String.format("%06d", score), new LabelStyle(new BitmapFont(), Color.WHITE));
            this.gameOverLabel = new Label("You hit the HIGHSCORE!!!", new LabelStyle(new BitmapFont(), Color.WHITE));

        } else {

            this.highscoreLabel = new Label(String.format("%06d", this.highscore), new LabelStyle(new BitmapFont(), Color.WHITE));
            this.gameOverLabel = new Label("GAME OVER :(", new LabelStyle(new BitmapFont(), Color.WHITE));
        }

        this.viewport = new FitViewport(400.0F, 208.0F, new OrthographicCamera());
        this.stage = new Stage(this.viewport, sb);
        this.gameOverLabel2 = new Label("Hit the screen to play again!", new LabelStyle(new BitmapFont(), Color.WHITE));
        this.scoreTitle = new Label("Your score", new LabelStyle(new BitmapFont(), Color.WHITE));
        this.scoreLabel = new Label(String.format("%06d", score), new LabelStyle(new BitmapFont(), Color.WHITE));
        this.highscoreTitle = new Label("Highscore", new LabelStyle(new BitmapFont(), Color.WHITE));
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(this.gameOverLabel).expandX();
        table.add(this.gameOverLabel2).expandX();
        table.row();
        table.add(this.scoreTitle).expandX();
        table.add(this.scoreLabel).expandX();
        table.row();
        table.add(this.highscoreTitle).expandX();
        table.add(this.highscoreLabel).expandX();
        this.stage.addActor(table);
    }

    public void show() {

    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(16384);
        this.stage.draw();
        this.game.batch.begin();
        if (Gdx.input.isTouched()) {
            this.dispose();
            this.game.batch.end();
            this.game.setScreen(new PlayScreen(this.game, this.manager));
            return;
        } else {
            this.game.batch.end();
        }
    }

    public void resize(int width, int height) {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void hide() {
    }

    public void dispose() {
    }

}
