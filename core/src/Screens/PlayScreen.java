package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Endless;

import sun.rmi.runtime.Log;

public class PlayScreen implements Screen {

    private Endless game;

    private TextureRegion textureRegion;
    private OrthographicCamera gamecam;
    private Viewport gamePort;


    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private Body b2body;


    private Texture ground;

    //testLogs
    private static final String TAG = "MyActivity";

    //temp variables to render stage from Hud
    public Stage stage;
    private Viewport viewport;


    public PlayScreen(Endless game) {


        this.game = game;

        //textures
        ground = new Texture("groundTestPNG.png");
        textureRegion = new TextureRegion(ground);

        //cams
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Endless.V_WIDTH / Endless.PPM, Endless.V_HEIGHT / Endless.PPM, gamecam);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -10), true);
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();



        //creates ground an circle, temporaily here for testing and wil end up in level-gen family
        BodyDef bdef = new BodyDef();
        bdef.position.set(30 / Endless.PPM, 30 / Endless.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        ChainShape groundShape = new ChainShape();
        groundShape.createChain(new Vector2[] {new Vector2(-100 ,0), new Vector2(100, 0)});

        fdef.shape = groundShape;
        b2body.createFixture(fdef).setUserData(this);

        Body b2body2;
        BodyDef bdef2 = new BodyDef();
        bdef2.position.set(32 / Endless.PPM, 32 / Endless.PPM);
        bdef2.type = BodyDef.BodyType.DynamicBody;
        b2body2 = world.createBody(bdef2);

        FixtureDef fdef2 = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Endless.PPM);

        fdef2.shape = shape;
        b2body2.createFixture(fdef2).setUserData(this);





        //temp code from hud to render stage
        viewport = new FitViewport(Endless.V_WIDTH, Endless.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);


        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        Gdx.app.log(TAG, "rendered");
    }

    @Override
    public void show() {

    }



    @Override
    public void render(float delta) {
        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);




        game.batch.setProjectionMatrix(gamecam.combined);

        game.batch.setProjectionMatrix(stage.getCamera().combined);

        //renderer our Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        /*gamecam.update();
        game.batch.begin();
        game.batch.end();

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.setProjectionMatrix(stage.getCamera().combined);*/


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
