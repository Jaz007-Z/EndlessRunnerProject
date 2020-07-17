package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Endless;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;


public class SplashScreen implements Screen {												//Resolver for GPGS events
    private Texture texture = new Texture(Gdx.files.internal("splashlogo.png"));    	//Load splash logo image
    private Image splashImage = new Image(texture);											//Create image from texture of splash logo
    private Stage stage = new Stage(new FitViewport(Endless.V_WIDTH, Endless.V_HEIGHT));	//Create stage to display the logo

    public AssetManager manager;
    public Endless game;

    public SplashScreen(Endless game, AssetManager manager) {
        this.manager = manager;
        this.game = game;
    }

    @Override
    public void show() {
        stage.addActor(splashImage);

		/*Set the alpha of the image to 0 for completely transparent so it can be faded in and out,
		  set position of image directly in the center of the screen then change to main menu.      */

        splashImage.setColor(1, 1, 1, 0);
        splashImage.setPosition(Endless.V_WIDTH/2 - splashImage.getWidth()/2, Endless.V_HEIGHT/2 - splashImage.getHeight()/2);
        splashImage.addAction(Actions.sequence(fadeIn(1f), delay(1.5f), fadeOut(1f), run(new Runnable() {
            @Override
            public void run() {
                game.setScreen(new MenuScreen(game, manager));
            }
        })));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        texture.dispose();
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }
}
