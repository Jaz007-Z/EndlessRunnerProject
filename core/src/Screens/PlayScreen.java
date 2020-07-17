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
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Endless;
import com.mygdx.game.Scenes.Hud;
import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.ArrayList;
import java.util.Random;

import LevelGen.FireArea;
import LevelGen.FireHoleArea;
import LevelGen.HoleArea;
import LevelGen.Level;
import LevelGen.PlatformArea;
import Sprites.Player;

import static Sprites.Player.State.DEAD;

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
    boolean useTimer;
    float timer;
    float area2NewEnd;

    private float oldNewEnd;


    //Box2d variables
    public World world;
    private Box2DDebugRenderer b2dr;
    private Body b2body;
    float playerSpeed = 10.0f; // 10 pixels per second. May be too fast
    float playerX;
    float playerY;

    //sprites
    private Player player;

    private TextureAtlas atlas = new TextureAtlas("Run.pack");
    public Animation playerRun;
    public TextureRegion playerJump;
    public TextureRegion playerFall;
    public Animation playerDead;



    public TextureRegionDrawable background = new TextureRegionDrawable((new TextureRegion(new Texture("playscreen_background.jpg"))));
    private TextureRegionDrawable ground;
    private final Texture pausebtnActive;
    private final Texture pausebtnInactive;
    //private static final float PAUSE_WIDTH = 0.3f;
    //private static final float PAUSE_HEIGHT = 0.3f;

    //HUD AND HEALTH BAR
    private Hud hud;
    Texture blank;
    float damage = 0;
    float health = 0.7f;
    private static final int PAUSE_WIDTH = 50;
    private static final int PAUSE_HEIGHT = 50;

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

        //textures
        ground = new TextureRegionDrawable(new Texture("groundTestPNG.png"));
        pausebtnActive = new Texture("Button_62.png");
        pausebtnInactive = new Texture("Button_63.png");
        blank = new Texture("blank.png");

        hud = new Hud(game.batch, this);
        //cams
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Endless.V_WIDTH / Endless.PPM, Endless.V_HEIGHT / Endless.PPM, gamecam);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(1, -5), true); //lowered gravity from -10 to show effect
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        //Level prep
        destroy = true;
        setLevel3 = false;
        setLevel4 = true;
        destroyLv3 = false;
        destroyLv4 = false;
        useTimer = false;

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
        area2NewEnd = levels.get(2).getNewEnd() / Endless.PPM;

        player = new Player(this, manager);

        viewport = new FitViewport(Endless.V_WIDTH, Endless.V_HEIGHT, new OrthographicCamera());

        stage = new Stage(viewport, game.batch);

        //gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        music = manager.get("music/main.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();


        Gdx.app.log(TAG, "rendered");
    }


    /** Called when this screen becomes the current screen for a {@link Game}. */
    @Override
    public void show() {

    }

    public void handleInput(float dt){
        //control our player using immediate impulses
        if(player.currentState != DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                player.jump();
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isTouched()) {
                if (Gdx.input.getX() < Gdx.graphics.getWidth() / 2){
                    player.b2body.applyLinearImpulse(new Vector2(0, 0.15f), player.b2body.getWorldCenter(), true);
                }
            }
        }
    }




    public void update(float dt){
        //handle user input first
        handleInput(dt);
        player.update(dt);

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        //update the HUD
        hud.update(dt);

        //THIS IS THE LOGIC TO "DAMAGE" THE HERO.
        // add some number to var damage, in the example above
        // the hero's life will decrease a little every 3 seconds.
        //if(hud.scoreImplement == 1){
        //    damage += 0.005f;
        //}

        if(player.b2body.getPosition().y <= -1){
            player.setPlayerIsDead();
        }

        //attach our gamecam to our players.x coordinate
        //if(player.currentState != DEAD) {
        gamecam.position.x = player.b2body.getPosition().x;
        //}

        //update our gamecam with correct coordinates after changes

        gamecam.update();

        //tell our renderer to draw only what our camera can see in our game world.
        //add for loop that will have all of the levels draw themselves in the right range
        //System.out.println(gamecam.position);
        System.out.println(player.currentState);

        if ( (int) (area2NewEnd)  < (int) player.b2body.getPosition().x && destroy) {
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

            destroy = false;
            useTimer = true;
            randomizeLevels();
            area2NewEnd = levels.get(2).getNewEnd() / Endless.PPM;
        }
        if (useTimer == true) {
            timer = timer + dt;
        }
        if (timer >= 4) {
            destroy = true;
            timer = 0;
            useTimer = false;
        }
    }


    @Override
    public void render(float delta) {

        //Clear the game screen with Black
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        hud.stage.draw();

        //renderer our Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);

        //gamecam.update();

        game.batch.begin();

        player.draw(game.batch);

        game.batch.draw(pausebtnInactive, gamecam.position.x - 0.1f, gamecam.position.y + 0.7f,
                PAUSE_WIDTH, PAUSE_HEIGHT);

        game.batch.draw(blank, gamecam.position.x - 1.1f, gamecam.position.y + 0.82f,
                health - damage, 0.15f);

        // Conditions to GAME OVER:
        // 1 - the player falls,
        // 2 - the health goes 0.
        // feel free to add what you want in this IF STATEMENT.
        if(player.currentState == DEAD || damage >= 0.7f){
            game.batch.end();
            pause();
            game.setScreen(new GameOverScreen(this.game, this.manager, getHud(), game.batch));
            return;
        }

        game.batch.end();

        //game.batch.setProjectionMatrix(stage.getCamera().combined);

    }

    public void randomizeLevels () {
        level0 = randomizeArea(level0);
        level1 = randomizeArea(level1);
        level2 = randomizeArea(level2);
        level3 = randomizeArea(level3);
        level4 = randomizeArea(level4);


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

        if (setLevel3) {
            oldNewEnd = levels.get(4).getNewEnd();
            levels.get(0).setNewEnd(oldNewEnd);
        } else {
            oldNewEnd = levels.get(3).getNewEnd();
            levels.get(0).setNewEnd(oldNewEnd);
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
                level = new Level(world);
                break;
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
        game.batch.dispose();
        manager.dispose();
    }

    public World getWorld() {
        return world;
    }

    public Hud getHud(){return hud;}

    public Screen getScreen() {return this; }

    public TextureAtlas getAtlas() {return atlas;}
}
