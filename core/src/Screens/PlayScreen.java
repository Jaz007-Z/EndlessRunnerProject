package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Endless;

public class PlayScreen implements Screen {

    private Endless game;
    private Texture texture;
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private static final int GROUND_Y_OFFSET = -50;


    private Texture ground;
    private Vector2 groundPos1, groundPos2;


    public PlayScreen(Endless game) {
        this.game = game;
        texture = new Texture("badlogic.jpg");
        ground = new Texture("groundTestPNG.png");
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Endless.V_WIDTH / Endless.PPM, Endless.V_HEIGHT / Endless.PPM, gamecam);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        /*Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(texture, 0, 0);
        game.batch.end();*/
        /*groundPos1 = new Vector2(gamecam.position.x - gamecam.viewportWidth / 2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((gamecam.position.x - gamecam.viewportWidth / 2) + ground.getWidth(), GROUND_Y_OFFSET);

        game.batch.begin();
        game.batch.draw(ground, groundPos1.x, groundPos1.y);
        game.batch.draw(ground, groundPos2.x, groundPos2.y);
        game.batch.end();*/
        game.batch.begin();

        for (int i = 0; i < 5; i++) {
            //groundPos1 = new Vector2(gamecam.position.x - gamecam.viewportWidth / 2, GROUND_Y_OFFSET);
            groundPos2 = new Vector2((gamecam.position.x - gamecam.viewportWidth / 2) + ground.getWidth() * i, GROUND_Y_OFFSET);

            //game.batch.draw(ground, groundPos1.x, groundPos1.y);
            game.batch.draw(ground, groundPos2.x, groundPos2.y);
        }
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
