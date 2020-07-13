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

import java.util.ArrayList;
import java.util.Random;

import LevelGen.FireArea;
import LevelGen.FireHoleArea;
import LevelGen.HoleArea;
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
    

    //areas
    private ArrayList<Level> levels = new ArrayList<>();
    Level level0;
    Level level1;
    Level level2;
    Level level3;
    Level level4;
    Level level5;
    boolean destroy;
    boolean destroyLv3;
    boolean destroyLv4;
    boolean setLevel3;
    boolean setLevel4;

    private float oldNewEnd;


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

        //cams
        //gamecam = new OrthographicCamera(Endless.V_WIDTH / Endless.PPM, Endless.V_HEIGHT / Endless.PPM); used make cam without viewport
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Endless.V_WIDTH / Endless.PPM, Endless.V_HEIGHT / Endless.PPM, gamecam);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -5), true); //lowered gravity from -10 to show effect
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();


        //Level prep
        destroy = true;
        setLevel3 = false;
        setLevel4 = true;
        destroyLv3 = false;
        destroyLv4 = false;

        /*levels.add(level);
        levels.get(0).generateDesign();
        levels.get(0).dispose();

        oldNewEnd = levels.get(0).getNewEnd();
        level = new HoleArea(world);
        level.setNewEnd(oldNewEnd);
        levels.add(level);
        levels.get(0).generateDesign();
        //level.generateDesign();*/

        level0 = new FireArea(world);
        level1 = new FireArea(world);
        level2 = new FireArea(world);
        level3 = new FireArea(world);
        //level4 = new FireArea(world);

        levels.add(level0);
        levels.add(level1);
        levels.add(level2);
        levels.add(level3);
        levels.add(level4);

        levels.get(0).generateDesign();

        oldNewEnd = levels.get(0).getNewEnd();
        levels.get(1).setNewEnd(oldNewEnd);
        levels.get(1).generateDesign();

        oldNewEnd = levels.get(1).getNewEnd();
        levels.get(2).setNewEnd(oldNewEnd);
        levels.get(2).generateDesign();

        oldNewEnd = levels.get(2).getNewEnd();
        levels.get(3).setNewEnd(oldNewEnd);
        levels.get(3).generateDesign();
        //Level prep finished


        player.definePlayer(world);

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

    }




    public void update(float dt) {
        //handle user input first
        handleInput(dt);
        player.update(dt);

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);


        //add code to update player and enemies

        //attach our gamecam to our players.x coordinate
        //if(player.currentState != Player.State.DEAD){
        gamecam.position.x = player.b2body.getPosition().x;
        // }

        gamecam.update();
        //tell our renderer to draw only what our camera can see in our game world.
        //add for loop that will have all of the levels draw themselves in the right range
        //System.out.println(gamecam.position);

       if ( (int) (levels.get(2).getNewEnd() / Endless.PPM) + 4 < (int) player.b2body.getPosition().x && destroy) {
           //if (player.b2body.getPosition().x > 200) {
           levels.get(0).dispose();
           levels.get(1).dispose();
           levels.get(2).dispose();
           if (destroyLv3) {
               levels.get(3).dispose();
               destroyLv3 = false;
           } else if (destroyLv4) {
               levels.get(4).dispose();
               destroyLv4 = false;
           }
           //destroy = false;
           randomizeLevels();
       }

        //System.out.println("Player Position: "+ player.b2body.getPosition().x);
        //System.out.println("End: "+ levels.get(2).getNewEnd() / Endless.PPM);
        System.out.println("End: " + levels.get(1).getNewEnd() / Endless.PPM);

    }


    @Override
    public void render(float delta) {

        //Clear the game screen with Black
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 0);
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

        game.batch.draw(pausebtnInactive, Endless.V_WIDTH / 2 - PAUSE_WIDTH / 2, Endless.V_HEIGHT - 90,
                PAUSE_WIDTH, PAUSE_HEIGHT);

        //game.batch.draw('player', (int)playerX, (int)playerY);
        //player.draw(game.batch);
        game.batch.end();


        //game.batch.setProjectionMatrix(stage.getCamera().combined);


    }

    public void randomizeLevels () {
        randomizeArea(level0);
        randomizeArea(level1);
        randomizeArea(level2);
        randomizeArea(level3);
        randomizeArea(level4);

        levels.set(0, level0);
        levels.set(1, level1);
        levels.set(2, level2);
        if (setLevel3) {
            levels.set(3, level3);
            destroyLv4 = true;
        } else if (setLevel4) {
            levels.set(4, level4);
            destroyLv3 = true;
        }

        levels.get(0).generateDesign();

        oldNewEnd = levels.get(0).getNewEnd();
        levels.get(1).setNewEnd(oldNewEnd);
        levels.get(1).generateDesign();

        oldNewEnd = levels.get(1).getNewEnd();
        levels.get(2).setNewEnd(oldNewEnd);
        levels.get(2).generateDesign();


        if (setLevel3) {
            oldNewEnd = levels.get(2).getNewEnd();
            levels.get(3).setNewEnd(oldNewEnd);
            levels.get(3).generateDesign();
            setLevel3 = false;
            setLevel4 = true;
        } else if (setLevel4) {
            oldNewEnd = levels.get(2).getNewEnd();
            levels.get(4).setNewEnd(oldNewEnd);
            levels.get(4).generateDesign();
            setLevel3 = true;
            setLevel4 = false;
        }
    }

    protected Level randomizeArea (Level level) {
        Random random = new Random();
        int int_random = random.nextInt(5);
        switch (int_random) {
            case 0:
                //level = new Level(world);
                //break;
            case 1:
                level = new FireArea(world);
                break;
            case 2:
                level = new FireHoleArea(world);
                break;
            case 3:
                level = new HoleArea(world);
                break;
            case 4:
                level = new PlatformArea(world);
                break;
        }
        return level;
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
