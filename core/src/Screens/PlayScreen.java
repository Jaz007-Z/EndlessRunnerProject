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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Endless;
import Scenes.Hud;
import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.ArrayList;
import java.util.Random;

import LevelGen.FireArea;
import LevelGen.FireHoleArea;
import LevelGen.HoleArea;
import LevelGen.Level;
import LevelGen.PlatformArea;
import Sprites.Player;
import Tools.WorldContactListener;

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
    private TextureAtlas atlas = new TextureAtlas("Run.pack");
    public Animation playerRun;
    public TextureRegion playerJump;
    public TextureRegion playerFall;
    //public Animation playerDead;


    //private Texture ground;
    public TextureRegionDrawable background = new TextureRegionDrawable((new TextureRegion(new Texture("playscreen_background.jpg"))));
    private TextureRegionDrawable ground;
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
        /*ground = new Texture("groundTestPNG.png");
        textureRegion = new TextureRegion(ground);*/
        ground = new TextureRegionDrawable(new Texture("ground.png"));
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
        world = new World(new Vector2(0, -5), true); //lowered gravity from -10 to show effect
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        //add contact listener
        world.setContactListener(new WorldContactListener());

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
                //player.b2body.applyLinearImpulse(new Vector2(0, 0.4f), player.b2body.getWorldCenter(), true);
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
        //Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        hud.stage.draw();

        game.batch.setProjectionMatrix(gamecam.combined);

        mouseInWorld3D.x = Gdx.input.getX();
        mouseInWorld3D.y = Gdx.input.getY();
        mouseInWorld3D.z = 0;
        gamecam.unproject(mouseInWorld3D);
        mouseInWorld2D.x = mouseInWorld3D.x;
        mouseInWorld2D.y = mouseInWorld3D.y;

        //gamecam.update();

        //renderer our Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        game.batch.begin();

        for (Level level : levels) {
            for (Body body : level.getBodiesGround()) {
                game.batch.draw(level.ground, body.getPosition().x, body.getPosition().y - (10 / Endless.PPM),
                        level.getGroundLengthD2() * 2 / Endless.PPM, 13 / Endless.PPM);
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
            //health -= 0.01f;
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
                    (mouseInWorld2D.y > gamecam.position.y && mouseInWorld2D.y < gamecam.position.y + 0.1f)
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

        // Conditions to GAME OVER:
        // 1 - the player falls,
        // 2 - the health goes 0.
        // feel free to add what you want in this IF STATEMENT.
        if(player.health == 0){

        }
        if(player.getState().toString() == Player.State.DEAD.toString() || health <= 0){

            game.batch.end();
            game.setScreen(new GameOverScreen(this.game, this.manager, hud.score, game.batch));
            return;
        }

        //game.batch.draw('player', (int)playerX, (int)playerY);
        //player.draw(game.batch);
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

    public Screen getScreen() {return this; }

    public TextureAtlas getAtlas() {return atlas;}
}
