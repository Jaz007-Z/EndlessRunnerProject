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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
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
import Scenes.Hud;
import Sprites.Player;
import Tools.WorldContactListener;

/**
 * @author      Jimmy Zimsky, Dallas Eaton, Elias Moreira, Nathaniel Snow 
 * @version     1.0                     
 */

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

    //player texture/animation assets
    private TextureAtlas atlas = new TextureAtlas("Run.atlas");
    public Animation playerRun;
    public TextureRegion playerJump;
    public TextureRegion playerFall;

    //coins
    public Array<TextureRegion> frames = new Array<>();
    public Texture coinTex = new Texture("coins.png");
    public Animation coinsAnimation;
    private float stateTimer;
    private boolean coinIsDead;

    public enum State { COLLECTED, ALIVE }
    public State coinState;
    public State oldCoinState;


    //private Texture ground;
    public TextureRegionDrawable background = new TextureRegionDrawable((new TextureRegion(new Texture("playscreen_background.jpg"))));
    public TextureRegionDrawable ground;
    private Texture pausebtnActive;
    private Texture pausebtnInactive;
    private Texture fullHeart, midHeart, emptyHeart, coinHudIcon;
    private Texture healthBarContainer;
    private Texture menuContainer;
    private static final float PAUSE_WIDTH = 0.3f;
    private static final float PAUSE_HEIGHT = 0.3f;

    //HUD AND HEALTH BAR
    private Hud hud;
    Texture blank;
    Texture blank2;
    float health = 0.95f;
    private final Vector2 mouseInWorld2D = new Vector2();
    private final Vector3 mouseInWorld3D = new Vector3();
    private boolean isPaused = false;

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
        pausebtnActive = new Texture("Button_62.png");
        pausebtnInactive = new Texture("Button_63.png");
        menuContainer = new Texture("Windows_07.png");
        fullHeart = new Texture("full-heart.png");
        midHeart = new Texture("mid-heart.png");
        emptyHeart = new Texture("empty-heart.png");
        coinHudIcon = new Texture("coin-alone.png");

        this.healthBarContainer = new Texture("Windows_52.png");
        this.menuContainer = new Texture("Windows_07.png");
        blank = new Texture("Windows_50.png");
        blank2 = new Texture("blank.png");

        hud = new Hud(game.batch, this);
        //cams
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(Endless.V_WIDTH / Endless.PPM, Endless.V_HEIGHT / Endless.PPM, gamecam);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0.0f, -5.0f), true); //lowered gravity from -10 to show effect
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        world.setContactListener(new WorldContactListener());

        //Level prep
        destroy = true;
        setLevel3 = false;
        setLevel4 = true;
        destroyLv3 = false;
        destroyLv4 = false;
        useTimer = false;

        level0 = new Level(world);
        level1 = new FireArea(world);
        level2 = new FireArea(world);
        level3 = new FireArea(world);
        level4 = new FireArea(world);

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
        world.setContactListener(new WorldContactListener());//feel free to move this if needed

        //coins
        frames.add(new TextureRegion(coinTex, 0, 0, 16, 16));
        frames.add(new TextureRegion(coinTex, 0, 16, 16, 16));
        frames.add(new TextureRegion(coinTex, 0, 32, 16, 16));
        frames.add(new TextureRegion(coinTex, 0, 48, 16, 16));
        frames.add(new TextureRegion(coinTex, 0, 64, 16, 16));
        frames.add(new TextureRegion(coinTex, 0, 80, 16, 16));
        coinsAnimation = new Animation(2.5f, frames);
        stateTimer = 0;
        coinState = State.ALIVE;
        oldCoinState = State.COLLECTED;

        viewport = new FitViewport(Endless.V_WIDTH, Endless.V_HEIGHT, new OrthographicCamera());

        stage = new Stage(viewport, game.batch);

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
    
    /**
    * Handles movement for the player. 
    * Uses Gdx.input for both arrow keys and touch screen
    */
    public void handleInput(float dt){
        //control our player using immediate impulses
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.b2body.getPosition().y < -30 / Endless.PPM && (player.b2body.getLinearVelocity().x >= -2 || player.b2body.getLinearVelocity().x >= 2))
            if (player.getState() != Player.State.FALLING) {
                player.b2body.applyForceToCenter(0, 30f, true);
            }


        if (Gdx.input.isTouched() && player.b2body.getPosition().y < -30 / Endless.PPM && (player.b2body.getLinearVelocity().x >= -2 || player.b2body.getLinearVelocity().x >= 2)) {

            if (player.getState() != Player.State.FALLING) {
                player.b2body.applyForceToCenter(0, 30f, true);
            }

        }

    }

    public void update(float dt){
        //handle user input first
        handleInput(dt);
        player.update(dt);

        if(player.b2body.getPosition().y <= -1){
            player.setPlayerIsDead();
        }

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        //update the HUD
        hud.update(dt);


        //attach our gamecam to our players.x coordinate
        gamecam.position.x = player.b2body.getPosition().x;

        if (player.b2body.getLinearVelocity().x <= 1.7f) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        //update our gamecam with correct coordinates after changes
        gamecam.update();

        //tell our renderer to draw only what our camera can see in our game world.
        //add for loop that will have all of the levels draw themselves in the right range
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
        if (useTimer) {
            timer = timer + dt;
        }
        if (timer >= 4) {
            destroy = true;
            timer = 0;
            useTimer = false;
        }


        //PAUSE CODE
        if(isPaused){
            game.pause();
            world.step(0, 0, 0);
        }
        else{
            game.resume();
            world.step(1 / 60f, 6, 2);
            hud.update(dt);
        }
    }


    @Override
    public void render(float delta) {

        //Clear the game screen with Black
        update(delta);

        Gdx.gl.glClearColor(0.13f, 0.14f, 0.19f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        hud.stage.draw();

        game.batch.setProjectionMatrix(gamecam.combined);

        mouseInWorld3D.x = Gdx.input.getX();
        mouseInWorld3D.y = Gdx.input.getY();
        mouseInWorld3D.z = 0;
        gamecam.unproject(mouseInWorld3D);
        mouseInWorld2D.x = mouseInWorld3D.x;
        mouseInWorld2D.y = mouseInWorld3D.y;

        //renderer our Box2DDebugLines

        game.batch.begin();


        for (Level level : levels) {
            for (Body body : level.getBodiesGround()) {
                game.batch.draw(level.ground, body.getPosition().x, body.getPosition().y - (10 / Endless.PPM),
                        level.getGroundLengthD2() * 2 / Endless.PPM, 13 / Endless.PPM);
            }
            for (Body body : level.getBodiesFire()) {
                game.batch.draw(level.spike, body.getPosition().x, body.getPosition().y,
                        16 / Endless.PPM, 16 / Endless.PPM);
            }
            for (Body body : level.getBodiesCoin()) {
                game.batch.draw(getCoinRegion(delta), body.getPosition().x, body.getPosition().y,
                        16 / Endless.PPM, 16 / Endless.PPM);
            }
            for (Body body : level.getBodiesPlatform()) {
                game.batch.draw(level.ground, body.getPosition().x, body.getPosition().y - (10 / Endless.PPM),
                        level.getPlatformWidthD2() * 2 / Endless.PPM, 13 / Endless.PPM);
            }
        }



        player.draw(game.batch);

        //PAUSE BUTTON HANDLING
        if((mouseInWorld2D.x > gamecam.position.x - 0.1f && mouseInWorld2D.x < gamecam.position.x - 0.1f + PAUSE_WIDTH)&&
                (mouseInWorld2D.y > gamecam.position.y + 0.6f && mouseInWorld2D.y < gamecam.position.y + 0.6f + PAUSE_HEIGHT)){
            isPaused = true;
        }

        //PAUSE MENU
        if(isPaused){
            game.batch.draw(pausebtnActive, gamecam.position.x - 0.1f, gamecam.position.y + 0.7f,
                    PAUSE_WIDTH, PAUSE_HEIGHT);
            game.batch.draw(menuContainer, gamecam.position.x - 1f, gamecam.position.y - 1f,
                    2f, 2f);

            // RESUME BUTTON
            if((mouseInWorld2D.x > gamecam.position.x - 0.5f && mouseInWorld2D.x < gamecam.position.x - 0.5f +(1)) &&
                    (mouseInWorld2D.y > gamecam.position.y + 0.36f && mouseInWorld2D.y < gamecam.position.y + 0.36f + 0.3f)
            ){
                isPaused = false;
            }
            // RESTART BUTTON
            if((mouseInWorld2D.x > gamecam.position.x - 0.5f && mouseInWorld2D.x < gamecam.position.x - 0.5f +(1)) &&
                    (mouseInWorld2D.y > gamecam.position.y && mouseInWorld2D.y < gamecam.position.y + 0.3f)
            ){
                game.batch.end();
                this.game.setScreen(new PlayScreen(this.game, this.manager));
                return;
            }
            //EXIT BUTTON
            if((mouseInWorld2D.x > gamecam.position.x - 0.5f && mouseInWorld2D.x < gamecam.position.x - 0.5f +(1)) &&
                    (mouseInWorld2D.y > gamecam.position.y - 0.76f && mouseInWorld2D.y < gamecam.position.y - 0.76f + 0.3f)
            ){
                Gdx.app.exit();
            }

        }
        else{
            game.batch.draw(pausebtnInactive, gamecam.position.x - 0.1f, gamecam.position.y + 0.7f,
                    PAUSE_WIDTH, PAUSE_HEIGHT);
            isPaused = false;
        }
        

        game.batch.draw(coinHudIcon, gamecam.position.x + 2f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);

        if(player.health == 3){
            game.batch.draw(fullHeart, gamecam.position.x - 2f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
            game.batch.draw(fullHeart, gamecam.position.x - 1.7f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
            game.batch.draw(fullHeart, gamecam.position.x - 1.4f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
        }
        else if(player.health == 2.5){
            game.batch.draw(fullHeart, gamecam.position.x - 2f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
            game.batch.draw(fullHeart, gamecam.position.x - 1.7f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
            game.batch.draw(midHeart, gamecam.position.x - 1.4f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
        }
        else if(player.health == 2) {
            game.batch.draw(fullHeart, gamecam.position.x - 2f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
            game.batch.draw(fullHeart, gamecam.position.x - 1.7f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
            game.batch.draw(emptyHeart, gamecam.position.x - 1.4f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
        }
        else if(player.health == 1.5) {
            game.batch.draw(fullHeart, gamecam.position.x - 2f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
            game.batch.draw(midHeart, gamecam.position.x - 1.7f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
            game.batch.draw(emptyHeart, gamecam.position.x - 1.4f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
        }
        else if(player.health == 1) {
            game.batch.draw(fullHeart, gamecam.position.x - 2f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
            game.batch.draw(emptyHeart, gamecam.position.x - 1.7f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
            game.batch.draw(emptyHeart, gamecam.position.x - 1.4f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
        }
        else if(player.health == 0.5) {
            game.batch.draw(midHeart, gamecam.position.x - 2f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
            game.batch.draw(emptyHeart, gamecam.position.x - 1.7f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
            game.batch.draw(emptyHeart, gamecam.position.x - 1.4f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
        }
        else{
            game.batch.draw(emptyHeart, gamecam.position.x - 2f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
            game.batch.draw(emptyHeart, gamecam.position.x - 1.7f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
            game.batch.draw(emptyHeart, gamecam.position.x - 1.4f, gamecam.position.y + 1f, PAUSE_WIDTH, PAUSE_HEIGHT);
        }


        // Conditions to GAME OVER:
        // 1 - the player falls,
        // 2 - the health goes 0.
        // feel free to add what you want in this IF STATEMENT.
        if(player.getState().toString() == Player.State.DEAD.toString() || health <= 0){
            game.batch.end();
            game.setScreen(new GameOverScreen(this.game, this.manager, hud.score, game.batch));
            return;
        }

        game.batch.end();

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
    
    /**
    * Creates random levels 
    */

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

    public TextureAtlas getAtlas() {return atlas;}


    public TextureRegion getCoinRegion(float dt){

        TextureRegion region;
        switch(coinState) {

            case COLLECTED:
                region = null;
                break;
            case ALIVE:
            default:
                region = (TextureRegion) coinsAnimation.getKeyFrame(stateTimer, true);
                break;
        }

        stateTimer =  coinState == oldCoinState ? stateTimer + dt : 0;
        oldCoinState = coinState;
        return region;
    }

}
