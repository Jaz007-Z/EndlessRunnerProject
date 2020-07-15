package Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Endless;
import com.mygdx.game.Scenes.Hud;

import LevelGen.FireArea;
import LevelGen.Level;
import Sprites.Player;

import static com.badlogic.gdx.Gdx.gl;

public class PlayScreen implements Screen {

    private final Texture pausebtnActive;
    private final Texture pausebtnInactive;
    Endless game;

    private Level level;
    private TextureRegion textureRegion;
    public OrthographicCamera gamecam;
    private Viewport gamePort;

    int ii;

    //Box2d variables
    public World world;
    private Box2DDebugRenderer b2dr;
    float playerSpeed = 10.0f; // 10 pixels per second. May be too fast
    float playerX;
    float playerY;

    //sprites
    private Player player;

    private TextureAtlas atlas = new TextureAtlas("RunSmall.pack");

    public TextureRegion playerStand;
    public Animation playerRun;
    public Animation playerJump;
    public Animation playerFall;
    public Animation playerDead;

    //Array<Body> bodies = new Array<Body>();
    private Texture ground;
    private Image background;


    private static final int PAUSE_WIDTH = 50;
    private static final int PAUSE_HEIGHT = 50;

    //HUD
    private Hud hud;

    //testLogs
    private static final String TAG = "MyActivity";

    //temp variables to render stage from Hud
    public Stage stage;
    private Viewport viewport;
    public Music music;

    public AssetManager manager;

    public PlayScreen(Endless game, AssetManager manager) {

        this.manager = manager;
        this.game = game;

        ii = 0;

        //textures
        ground = new Texture("ground.png");
        textureRegion = new TextureRegion(ground);
        this.pausebtnActive = new Texture("Button_62.png");
        this.pausebtnInactive = new Texture("Button_63.png");
        background = new Image(new Texture("Background.png"));

        hud = new Hud(game.batch);

        //cams
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Endless.V_WIDTH / Endless.PPM, Endless.V_HEIGHT / Endless.PPM, gamecam);
        //gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
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

        player = new Player(this, manager);

        //temp code from hud to render stage
        viewport = new FitViewport(Endless.V_WIDTH, Endless.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, game.batch);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        music = manager.get("music/main.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();


        Gdx.app.log(TAG, "rendered");
    }

    public TextureAtlas getAtlas() {return atlas;}

    /** Called when this screen becomes the current screen for a {@link Game}. */
    @Override
    public void show() {
        //input.setCatchKey(Input.Keys.BACK, true);
        //Gdx.input.setInputProcessor(stage);
    }

    public void handleInput(float dt){
        //control our player using immediate impulses
        if(player.currentState != Player.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                player.jump();
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            /*if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
                player.fire();*/
        }
    }




    public void update(float dt){
        //handle user input first
        handleInput(dt);

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        //add code to update player and enemies
        player.update(dt);

        //update the HUD
        hud.update(dt);

        if(player.b2body.getPosition().x <= -0.52){
            player.setPlayerIsDead();
        }
        //attach our gamecam to our players.x coordinate
        if(player.currentState != Player.State.DEAD) {
            gamecam.position.x = player.b2body.getPosition().x;
        }
        //update our gamecam with correct coordinates after changes
        gamecam.update();

        //tell our renderer to draw only what our camera can see in our game world.
        //add for loop that will have all of the levels draw themselves in the right range
        System.out.println(gamecam.position);
        System.out.println(player.currentState);
    }


    @Override
    public void render(float delta) {

        //Clear the game screen with Black
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        hud.stage.draw();

        //renderer our Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);


        //gamecam.update();
        game.batch.begin();

        game.batch.draw(pausebtnInactive, Endless.V_WIDTH / 2 - PAUSE_WIDTH / 2, Endless.V_HEIGHT - 90,
                PAUSE_WIDTH, PAUSE_HEIGHT);
        if(player.getState().toString() == Player.State.DEAD.toString()){
            game.batch.end();
            game.setScreen(new GameOverScreen(this.game, this.manager, hud.score, game.batch));
            return;
        }

        player.draw(game.batch);


        game.batch.end();


        //game.batch.setProjectionMatrix(stage.getCamera().combined);
        //hud.stage.draw();

        /*if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }*/
    }

    public boolean gameOver(){
        return player.currentState == Player.State.DEAD && player.getStateTimer() > 3;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
        manager.dispose();
        world.dispose();
        b2dr.dispose();
        stage.dispose();
        atlas.dispose();
        hud.dispose();
        game.batch.dispose();
    }

    public World getWorld() {
        return world;
    }

    //public Hud getHud(){return hud;}
}
