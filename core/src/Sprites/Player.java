package Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Endless;

import java.util.ArrayList;
import java.util.List;

import Screens.PlayScreen;


public class Player extends Sprite {
    public enum State { FALLING, JUMPING, RUNNING, DEAD }
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    TextureAtlas atlas;

    private Animation playerRun;
    private Animation playerJump;
    private Animation playerFall;
    private Animation playerDead;

    private boolean playerIsDead;
    private float stateTimer;
    List<String> animate = new ArrayList<>();

    private PlayScreen screen;


    public Player(PlayScreen screen){
        this.screen = screen;
        this.world = screen.world;
        currentState = State.RUNNING;
        previousState = State.RUNNING;
        stateTimer = 0;

        atlas = screen.getAtlas();
        animate.add("run1");
        animate.add("run2");
        animate.add("run3");
        animate.add("run4");
        animate.add("run5");
        animate.add("run6");
        animate.add("run7");
        animate.add("run8");

        Array<TextureRegion> frames = new Array<>();
        for(int i = 0; i < 8; i++) {
            frames.add(atlas.findRegion(animate.get(i)));
        }
        System.out.print(animate);
        playerRun = new Animation(0.1f, frames);

        animate.clear();
        frames.clear();

        animate.add("jump1");
        animate.add("jump2");

        for(int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(atlas.findRegion(animate.get(i))));
        }

        playerJump = new Animation(0.1f, frames);

        animate.clear();
        frames.clear();

        animate.add("fall1");
        animate.add("fall2");

        for(int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(atlas.findRegion(animate.get(i))));
        }

        playerFall = new Animation(0.1f, frames);

        animate.clear();
        frames.clear();
        
        animate.add("die1");
        animate.add("die2");
        animate.add("die3");
        animate.add("die4");
        animate.add("die5");

        for(int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(atlas.findRegion(animate.get(i))));
        }
        
        playerDead = new Animation(0.1f, frames);

        definePlayer(screen.world);
    }



    public void update(float dt){


        b2body.applyLinearImpulse(new Vector2(.015f, 0f ) , b2body.getWorldCenter(), true);
        // keep applying movement
        //update our sprite to correspond with the position of our Box2D body
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

        //commented out code that makes it crash. We might need to start multi-threading. I'm not sure why it crashed
        //but from what Nathanial said maybe we need to think about performance more

    }

    public TextureRegion getFrame(float dt){
        //get marios current state. ie. jumping, running, falling, etc.
        currentState = getState();

        TextureRegion region;

        //depending on the state, get corresponding animation keyFrame.
        switch(currentState){
            case DEAD:
                region = (TextureRegion) playerDead.getKeyFrame(stateTimer);
                break;
            case JUMPING:
                region = (TextureRegion) playerJump.getKeyFrame(stateTimer);
                break;
            case FALLING:
                region = (TextureRegion) playerFall.getKeyFrame(stateTimer);
                break;
            default:
                region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                break;
        }


        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;


    }

    public void definePlayer(World world){
        //Body b2body2; needed to be class variable not a seperate one here
        BodyDef bdef2 = new BodyDef();
        bdef2.position.set(-45 / Endless.PPM, -58 / Endless.PPM);
        bdef2.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef2);

        FixtureDef fdef2 = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Endless.PPM);

        fdef2.shape = shape;
        b2body.createFixture(fdef2).setUserData(this);
        //b2body.applyLinearImpulse(new Vector2(.7f, 0f ) , b2body.getWorldCenter(), true); //start movement off for one frame
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Endless.PPM, 6 / Endless.PPM), new Vector2(2 / Endless.PPM, 6 / Endless.PPM));
        fdef2.filter.categoryBits = 512;
        fdef2.shape = head;
        fdef2.isSensor = true;
    }

    public State getState(){
        //Test to Box2D for velocity on the X and Y-Axis
        //if mario is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump state
        if(playerIsDead)
            return State.DEAD;
        else if((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
            //if Y-Axis is negative, player is falling
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
            //if none of these return then he must be running
        else
            return State.RUNNING;
    }

    public void die() {

        if (!isDead()) {

            //screen.manager.get("", Music.class).stop();
            //screen.manager.get("", Sound.class).play();
            playerIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = 0;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
        }
    }

    public boolean isDead(){
        return playerIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void jump(){
        if ( currentState != State.JUMPING ) {
            b2body.applyLinearImpulse(new Vector2(0, 2f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public void draw(Batch batch){
        super.draw(batch);
    }
}
