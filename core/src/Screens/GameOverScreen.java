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
import com.mygdx.game.Scenes.Hud;

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
    Hud hud;

    public GameOverScreen(Endless game, AssetManager manager, Hud hud, SpriteBatch sb) {
        this.hud = hud;
        this.manager = manager;
        this.game = game;
        this.score = hud.score;

        Preferences prefs = Gdx.app.getPreferences("endlessGame");

        highscore = prefs.getInteger("highscore", 0);

        if (score > highscore) {

            prefs.putInteger("highscore", score);
            prefs.flush();

            highscoreLabel = new Label(String.format("%06d", score), new LabelStyle(new BitmapFont(), Color.WHITE));
            gameOverLabel = new Label("You hit the HIGHSCORE!!!", new LabelStyle(new BitmapFont(), Color.WHITE));

        } else {

            highscoreLabel = new Label(String.format("%06d", highscore), new LabelStyle(new BitmapFont(), Color.WHITE));
            gameOverLabel = new Label("GAME OVER :(", new LabelStyle(new BitmapFont(), Color.WHITE));
        }

        viewport = new FitViewport(400.0F, 208.0F, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        gameOverLabel2 = new Label("Hit the screen to play again!", new LabelStyle(new BitmapFont(), Color.WHITE));
        scoreTitle = new Label("Your score", new LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new LabelStyle(new BitmapFont(), Color.WHITE));
        highscoreTitle = new Label("Highscore", new LabelStyle(new BitmapFont(), Color.WHITE));
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(gameOverLabel).expandX();
        table.add(gameOverLabel2).expandX();
        table.row();
        table.add(scoreTitle).expandX();
        table.add(scoreLabel).expandX();
        table.row();
        table.add(highscoreTitle).expandX();
        table.add(highscoreLabel).expandX();
        stage.addActor(table);
    }

    public void show() {}

    public void render(float delta) {
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(16384);
        stage.draw();
        game.batch.begin();

        if (Gdx.input.isTouched()) {
            dispose();
            game.batch.end();
            game.setScreen(new PlayScreen(game, manager));

            return;
        } else {
            game.batch.end();
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
        hud.dispose();
    }
}
