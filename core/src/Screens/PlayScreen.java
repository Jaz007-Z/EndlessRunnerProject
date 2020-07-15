package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
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
import LevelGen.FireHoleArea;
import LevelGen.Level;
import LevelGen.PlatformArea;
import Sprites.Player;
import sun.rmi.runtime.Log;

public class PlayScreen implements Screen {

    Endless game;

    private Level level;
    private TextureRegion textureRegion;
    public OrthographicCamera gamecam;
    private Viewport gamePort;
    


    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private Body b2body;
    float playerSpeed = 10.0f; // 10 pixels per second. May be too fast
    float playerX;
    float playerY;

    //sprites
    private Player player = new Player();


    private Texture ground;
    private Texture pausebtnActive;
    private Texture pausebtnInactive;
    private static final int PAUSE_WIDTH = 50;
    private static final int PAUSE_HEIGHT = 50;

    //testLogs
    private static final String TAG = "MyActivity";

    //temp variables to render stage from Hud
    public Stage stage;
    private Viewport viewport;
    private Music music;

    public AssetManager manager;

    public PlayScreen(Endless game, AssetManager manager) {
        this.manager = manager;
        this.game = game;

        //textures
        ground = new Texture("groundTestPNG.png");
        textureRegion = new TextureRegion(ground);
        this.pausebtnActive = new Texture("Button_62.png");
        this.pausebtnInactive = new Texture("Button_63.png");
        
        hud = new Hud(game.batch);
        //cams
        //gamecam = new OrthographicCamera(Endless.V_WIDTH / Endless.PPM, Endless.V_HEIGHT / Endless.PPM); used make cam without viewport
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Endless.V_WIDTH / Endless.PPM, Endless.V_HEIGHT / Endless.PPM, gamecam);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -5), true); //lowered gravity from -10 to show effect
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();


        //Level prep
        //level = new Level(world);
        level = new FireArea(world);
        //level = new FireHoleArea(world);
        //level = new PlatformArea(world);



        level.generateDesign();


        player.definePlayer(world);

        viewport = new FitViewport(Endless.V_WIDTH, Endless.V_HEIGHT, new OrthographicCamera());

        stage = new Stage(viewport, game.batch);


        //gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0); doesn't seem ot be needed
        //getWorldWidth breaks the game too

        music = manager.get("music/main.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();


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
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(0, 0.15f), player.b2body.getWorldCenter(), true);

    }




    public void update(float dt) {
        //handle user input first
        handleInput(dt);
        player.update(dt);

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);
        
        hud.update(dt);
        
        if(player.b2body.getPosition().y <= -1){
            player.setPlayerIsDead();
        }

        //add code to update player and enemies

        //attach our gamecam to our players.x coordinate
        //if(player.currentState != Player.State.DEAD){
        gamecam.position.x = player.b2body.getPosition().x;
        // }

        gamecam.update();
        //tell our renderer to draw only what our camera can see in our game world.
        //add for loop that will have all of the levels draw themselves in the right range
        System.out.println(gamecam.position);

    }


    @Override
    public void render(float delta) {

        //Clear the game screen with Black
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        hud.stage.draw();


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

        game.batch.draw(pausebtnInactive, Endless.V_WIDTH / 2 - PAUSE_WIDTH / 2, Endless.V_HEIGHT - 90,
                PAUSE_WIDTH, PAUSE_HEIGHT);
        
        if(player.getState().toString() == Player.State.DEAD.toString()){
            game.batch.end();
            game.setScreen(new GameOverScreen(this.game, this.manager, hud.score, game.batch));
            return;
        }

        //game.batch.draw('player', (int)playerX, (int)playerY);
        //player.draw(game.batch);
        game.batch.end();


        //game.batch.setProjectionMatrix(stage.getCamera().combined);


    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
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
        music.dispose();
        game.batch.dispose();
        manager.dispose();
    }
}
