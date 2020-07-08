package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Endless;

import LevelGen.FireArea;
import LevelGen.Level;
import Sprites.Player;
import sun.rmi.runtime.Log;

public class PlayScreen implements Screen {

    private Endless game;

    private Level level;

    private TextureRegion textureRegion;
    private OrthographicCamera gamecam;
    private Viewport gamePort;


    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private Body b2body;
    float playerSpeed = 10.0f; // 10 pixels per second.
    float playerX;
    float playerY;

    //sprites
    private Player player = new Player();


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
        world = new World(new Vector2(0, -1), true); //lowered gravity from -10 to show effect
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();


        //Level prep
        //level = new Level(world);
        level = new FireArea(world);

        /*
        //creates ground, temporarily here for testing and wil end up in level-gen family
        BodyDef bdef = new BodyDef();
        bdef.position.set(0 / Endless.PPM, -60 / Endless.PPM); //position of the polygon
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        //makes a box. It can be a straight line like now or vertical. hx is length, hy is height. vector2's x sets new center for box relative to position.
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(50 / Endless.PPM ,0 / Endless.PPM, new Vector2(50 / Endless.PPM ,0 / Endless.PPM), 0 / Endless.PPM );


        fdef.shape = groundShape;
        b2body.createFixture(fdef).setUserData(this); */
        level.generateDesign();



        //circle for testing purposes - code from Mario
        /*Body b2body2;
        BodyDef bdef2 = new BodyDef();
        bdef2.position.set(0 / Endless.PPM, 0 / Endless.PPM);
        bdef2.type = BodyDef.BodyType.DynamicBody;
        b2body2 = world.createBody(bdef2);

        FixtureDef fdef2 = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Endless.PPM);

        fdef2.shape = shape;
        b2body2.createFixture(fdef2).setUserData(this);*/

        //player = new Player();
        player.definePlayer(world);

        //temp code from hud to render stage
        viewport = new FitViewport(Endless.V_WIDTH, Endless.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);


        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);



        Gdx.app.log(TAG, "rendered");
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){
        //control our player using immediate impulses
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);

    }




    public void update(float dt){
        //handle user input first
        handleInput(dt);

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);


        //add code to update player and enemies

        //attach our gamecam to our players.x coordinate
        //if(player.currentState != Player.State.DEAD) {
        //  gamecam.position.x = player.b2body.getPosition().x;
        // }



        //update our gamecam with correct coordinates after changes
        //gamecam.update();
        //tell our renderer to draw only what our camera can see in our game world.
        //add for loop that will have all of the levels draw themselves in the right range
        System.out.println(gamecam.position);

    }


    @Override
    public void render(float delta) {

        //Clear the game screen with Black
        update(delta);

        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);




        game.batch.setProjectionMatrix(gamecam.combined);



        //renderer our Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT))
            playerX -= Gdx.graphics.getDeltaTime() * playerSpeed;
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT))
            playerX += Gdx.graphics.getDeltaTime() * playerSpeed;
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_UP))
            playerY += Gdx.graphics.getDeltaTime() * playerSpeed;
        if(Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN))
            playerY -= Gdx.graphics.getDeltaTime() * playerSpeed;


        //gamecam.update();
        game.batch.begin();
        //game.batch.draw('player', (int)playerX, (int)playerY);
        //player.draw(game.batch);
        game.batch.end();


        //game.batch.setProjectionMatrix(stage.getCamera().combined);


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
