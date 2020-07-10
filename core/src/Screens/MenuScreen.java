package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Endless;

public class MenuScreen implements Screen {

    private static final int PLAY_BTN_WIDTH = 100;
    private static final int PLAY_BTN_HEIGHT = 100;
    private static final int EXIT_BTN_WIDTH = 50;
    private static final int EXIT_BTN_HEIGHT = 50;
    private static final int TITLE_WIDTH = 350;
    private static final int TITLE_HEIGHT = 80;

    Endless game;
    Texture title;
    Texture playbtnActive;
    Texture playbtnInactive;
    Texture exitbtnActive;
    Texture exitbtnInactive;
    Texture background;

    public Music music;
    public AssetManager manager;

    public MenuScreen (Endless game, AssetManager manager) {
        this.manager = manager;
        this.game = game;
        this.title = new Texture("Endless_Runner_Title_2.png");
        this.playbtnInactive = new Texture("Button_14.png");
        this.playbtnActive = new Texture("Button_15.png");
        this.exitbtnActive = new Texture("Button_99.png");
        this.exitbtnInactive = new Texture("Button_98.png");
        this.background = new Texture("Background.png");

        music = manager.get("music/intro.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.batch.draw(title, Endless.V_WIDTH / 2 - TITLE_WIDTH / 2, Endless.V_HEIGHT - 90,
                TITLE_WIDTH, TITLE_HEIGHT);
        //PLAY BUTTON HANDLING
        if((Gdx.input.getX() > (Endless.V_WIDTH / 2 - PLAY_BTN_WIDTH / 2) &&
                Gdx.input.getX() < (Endless.V_WIDTH / 2 - PLAY_BTN_WIDTH / 2) + PLAY_BTN_WIDTH)
                &&
                (Gdx.input.getY() > (Endless.V_HEIGHT / 2 - PLAY_BTN_HEIGHT / 2) &&
                        Gdx.input.getY() < (Endless.V_HEIGHT / 2 - PLAY_BTN_HEIGHT / 2) + PLAY_BTN_HEIGHT)){

            game.batch.draw(playbtnActive, Endless.V_WIDTH / 2 - PLAY_BTN_WIDTH / 2,
                    Endless.V_HEIGHT / 2 - PLAY_BTN_HEIGHT / 2, PLAY_BTN_WIDTH, PLAY_BTN_HEIGHT);
            if(Gdx.input.justTouched()) {
                music.stop();
                game.setScreen(new PlayScreen(game, manager));
            }
        }
        else{
            game.batch.draw(playbtnInactive, Endless.V_WIDTH / 2 - PLAY_BTN_WIDTH / 2,
                    Endless.V_HEIGHT / 2 - PLAY_BTN_HEIGHT / 2, PLAY_BTN_WIDTH, PLAY_BTN_HEIGHT);
        }

        // EXIT BUTTON HANDLING
        if((Gdx.input.getX() > (Endless.V_WIDTH - EXIT_BTN_WIDTH - 20) &&
                Gdx.input.getX() < (Endless.V_WIDTH - EXIT_BTN_WIDTH - 20) + EXIT_BTN_WIDTH) &&
                (Gdx.input.getY() > (EXIT_BTN_HEIGHT - 20) &&
                        Gdx.input.getY() < (EXIT_BTN_HEIGHT - 20) + EXIT_BTN_HEIGHT)){

            game.batch.draw(exitbtnActive, Endless.V_WIDTH - EXIT_BTN_WIDTH - 20,
                    Endless.V_HEIGHT - EXIT_BTN_HEIGHT - 20, EXIT_BTN_WIDTH, EXIT_BTN_HEIGHT);
            if(Gdx.input.justTouched()){
                game.batch.end();
            }
        }
        else{
            game.batch.draw(exitbtnInactive, Endless.V_WIDTH - EXIT_BTN_WIDTH - 20,
                    Endless.V_HEIGHT - EXIT_BTN_HEIGHT - 20, EXIT_BTN_WIDTH, EXIT_BTN_HEIGHT);
        }

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
        game.batch.dispose();
        music.dispose();
        title.dispose();
        playbtnActive.dispose();
        playbtnInactive.dispose();
        exitbtnActive.dispose();
        exitbtnInactive.dispose();
        background.dispose();

    }
}
