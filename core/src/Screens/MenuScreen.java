package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Endless;

import static com.badlogic.gdx.Gdx.input;

public class MenuScreen implements Screen {

    private Stage stage = new Stage(new FitViewport(Endless.V_WIDTH, Endless.V_HEIGHT));

    private static final float PLAY_BTN_WIDTH = 100;
    private static final float PLAY_BTN_HEIGHT = 100;
    private static final float EXIT_BTN_WIDTH = 50;
    private static final float EXIT_BTN_HEIGHT = 50;
    private static final float TITLE_WIDTH = 350;
    private static final float TITLE_HEIGHT = 80;


    private Texture bgTexture = new Texture("Background.png");
    private Texture titleTexture = new Texture("Endless_Runner_Title_2.png");

    //Buttons for the menu screen (play and exit)
    private Texture playIdleTexture = new Texture("play_idle.png");
    private TextureRegion playIdleRegion = new TextureRegion(playIdleTexture);
    private TextureRegionDrawable playIdleButton = new TextureRegionDrawable(playIdleRegion);

    private Texture playActTexture = new Texture("play_hover.png");
    private TextureRegion playActRegion = new TextureRegion(playActTexture);
    private TextureRegionDrawable playActButton = new TextureRegionDrawable(playActRegion);

    private Texture exitIdleTexture = new Texture("exit_idle.png");
    private TextureRegion exitIdleRegion = new TextureRegion(exitIdleTexture);
    private TextureRegionDrawable exitIdleButton = new TextureRegionDrawable(exitIdleRegion);

    private Texture exitActTexture = new Texture("exit_hover.png");
    private TextureRegion exitActRegion = new TextureRegion(exitActTexture);
    private TextureRegionDrawable exitActButton = new TextureRegionDrawable(exitActRegion);

    private ImageButton playButton = new ImageButton(playIdleButton, playActButton, playActButton);
    private ImageButton exitButton = new ImageButton(exitIdleButton, exitActButton, exitActButton);


    private Image bgImg = new Image(bgTexture);
    private Image title = new Image(titleTexture);


    public Music music;
    public AssetManager manager;
    public Endless game;

    public MenuScreen (Endless game, AssetManager manager) {
        this.manager = manager;
        this.game = game;

        music = manager.get("music/intro.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();

    }

    @Override
    public void show() {

        input.setCatchKey(Input.Keys.BACK, true);


        MenuListener menuListener = new MenuListener();
        playButton.addListener(menuListener);
        exitButton.addListener(menuListener);

        playButton.setName("Play");
        exitButton.setName("Exit");

        stage.addActor(bgImg);
        stage.addActor(title);
        title.setBounds(Endless.V_WIDTH / 2 - TITLE_WIDTH / 2, Endless.V_HEIGHT - 90,
                TITLE_WIDTH, TITLE_HEIGHT);
        stage.addActor(exitButton);
        exitButton.setBounds(Endless.V_WIDTH - EXIT_BTN_WIDTH - 20,
                Endless.V_HEIGHT - EXIT_BTN_HEIGHT - 20, EXIT_BTN_WIDTH, EXIT_BTN_HEIGHT);
        stage.addActor(playButton);
        playButton.setBounds(Endless.V_WIDTH / 2 - PLAY_BTN_WIDTH / 2,
                Endless.V_HEIGHT / 2 - PLAY_BTN_HEIGHT / 2, PLAY_BTN_WIDTH, PLAY_BTN_HEIGHT);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.BACK) || Gdx.input.isKeyPressed(Input.Keys.HOME)) {
            //Implement call for a prompt that will ask the user if they are sure they want to quit.
            Gdx.app.exit();
        }

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void resume() {
        music.play();
    }

    @Override
    public void hide() {
        music.pause();
    }

    @Override
    public void dispose() {
        manager.dispose();
        stage.dispose();
        exitActTexture.dispose();
        exitIdleTexture.dispose();
        playActTexture.dispose();
        playIdleTexture.dispose();
        music.dispose();
    }

    //Looking into converting this into an abstract public class or an interface..not sure which one yet.
    private class MenuListener extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if (actor.getName().equals("Play")) {
                game.setScreen(new PlayScreen(game, manager));

            }
            else if ((actor.getName().equals("Exit"))) {
                Gdx.app.exit();
            }
            /*else if (actor.getName().equals("Sound")) {
                if (Endless.SOUND_ON) {
                    Endless.SOUND_ON = false;
                    ((TextButton) actor).setText("Sound: Off");
                } else {
                    Endless.SOUND_ON = true;
                    ((TextButton) actor).setText("Sound: On");
                }
            }*/
        }
    }
}
