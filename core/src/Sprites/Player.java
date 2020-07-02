/*package com.mygdx.game.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Endless;
import com.mygdx.game.Sprites.Other.FireBall;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.Sprites.Enemies.*;

public class Player extends Sprite {


    public enum State { FALLING, JUMPING, STANDING, RUNNING, ATTACK, HIT, DEAD };
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private TextureRegion playerStand;
    private Animation playerRun;
    private Animation playerAttack;
    private TextureRegion playerHit;
    private TextureRegion playerJump;
    private TextureRegion playerDead;

    private float stateTimer;
    private boolean runningRight;
    private boolean attackAnimation;
    private boolean timeToRedefinePlayer;
    private boolean playerIsHit;
    private boolean playerIsDead;
    private PlayScreen screen;

    public Player(PlayScreen screen){
        //initialize default values
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //get run animation frames and add them to marioRun Animation
        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("hero_example"), i * 16, 0, 16, 16));
        playerRun = new Animation(0.1f, frames);

        frames.clear();


        //get jump animation frames and add them to marioJump Animation
        playerJump = new TextureRegion(screen.getAtlas().findRegion("hero_example"), 80, 0, 16, 16);

        //create texture region for mario standing
        playerStand = new TextureRegion(screen.getAtlas().findRegion("hero_example"), 0, 0, 16, 16);

        //create dead mario texture region
        playerDead = new TextureRegion(screen.getAtlas().findRegion("hero_example"), 96, 0, 16, 16);

        //define mario in Box2d
        definePlayer();

        //set initial values for marios location, width and height. And initial frame as marioStand.
        setBounds(0, 0, 16 / Endless.PPM, 16 / Endless.PPM);
        setRegion(playerStand);

        fireballs = new Array<FireBall>();

    }

    public void update(float dt){

        // time is up : too late mario dies T_T
        // the !isDead() method is used to prevent multiple invocation
        // of "die music" and jumping
        // there is probably better ways to do that but it works for now.
        if (screen.getHud().isTimeUp() && !isDead()) {
            die();
        }

    }

    public TextureRegion getFrame(float dt){
        //get marios current state. ie. jumping, running, standing...
        currentState = getState();

        TextureRegion region;

        //depending on the state, get corresponding animation keyFrame.
        switch(currentState){
            case DEAD:
                region = playerDead;
                break;
            case JUMPING:
                region = playerJump;
                break;
            case RUNNING:
                region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = playerStand;
                break;
        }

        //if mario is running left and the texture isnt facing left... flip it.
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }

        //if mario is running right and the texture isnt facing right... flip it.
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;

    }

    public State getState(){
        //Test to Box2D for velocity on the X and Y-Axis
        //if mario is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump state
        if(playerIsDead)
            return State.DEAD;
        else if((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
            //if negative in Y-Axis mario is falling
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
            //if mario is positive or negative in the X axis he is running
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
            //if none of these return then he must be standing
        else
            return State.STANDING;
    }


    public void die() {

        if (!isDead()) {

            Endless.manager.get("audio/music/mario_music.ogg", Music.class).stop();
            Endless.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
            playerIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = Endless.NOTHING_BIT;

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
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public void hit(Enemy enemy){
        if(enemy instanceof Monster){
            die();
        }
    }

    public void redefinePlayer(){
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Endless.PPM);
        fdef.filter.categoryBits = Endless.MARIO_BIT;
        fdef.filter.maskBits = Endless.GROUND_BIT |
                Endless.COIN_BIT |
                Endless.BRICK_BIT |
                Endless.ENEMY_BIT |
                Endless.OBJECT_BIT |
                Endless.ENEMY_HEAD_BIT |
                Endless.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Endless.PPM, 6 / Endless.PPM), new Vector2(2 / Endless.PPM, 6 / Endless.PPM));
        fdef.filter.categoryBits = Endless.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);

        timeToRedefinePlayer = false;

    }


    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / Endless.PPM, 32 / Endless.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Endless.PPM);
        fdef.filter.categoryBits = Endless.MARIO_BIT;
        fdef.filter.maskBits = Endless.GROUND_BIT |
                Endless.COIN_BIT |
                Endless.BRICK_BIT |
                Endless.ENEMY_BIT |
                Endless.OBJECT_BIT |
                Endless.ENEMY_HEAD_BIT |
                Endless.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Endless.PPM, 6 / Endless.PPM), new Vector2(2 / Endless.PPM, 6 / Endless.PPM));
        fdef.filter.categoryBits = Endless.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void fire(){
        fireballs.add(new FireBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
    }

    public void draw(Batch batch){
        super.draw(batch);
        for(FireBall ball : fireballs)
            ball.draw(batch);
    }
}
*/