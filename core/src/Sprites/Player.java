package Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Endless;

import Screens.PlayScreen;

import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP;
import static com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP_PINGPONG;


public class Player extends Sprite {

    public enum State { FALLING, JUMPING, RUNNING, DEAD}
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    AssetManager manager;

    private boolean playerIsDead;
    private float stateTimer;

    private PlayScreen screen;


    public Player(PlayScreen screen, AssetManager manager){
        this.screen = screen;
        this.world = screen.world;
        this.manager = manager;
        currentState = State.RUNNING;
        previousState = State.RUNNING;
        stateTimer = 0;

        Array<TextureRegion> frames = new Array<>();


        frames.add(new TextureRegion(screen.getAtlas().findRegion("Run1m"), 0,0,32,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("Run2m"), 0,32,32,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("Run3m"),0,64,32,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("Run4m"),0,96,32,32));

        screen.playerRun = new Animation(0.1f, frames);

        frames.clear();

        screen.playerJump = new TextureRegion(new Texture("jump1.png"));

        screen.playerFall = new TextureRegion(new Texture("fall1.png"));


        definePlayer(world);
        setBounds(0, 1, 32 / Endless.PPM, 32 / Endless.PPM);
        setRegion(screen.playerFall);
    }



    public void update(float dt){

        //if(currentState == State.RUNNING) {
        //    b2body.applyLinearImpulse(new Vector2(.04f, 0f), b2body.getWorldCenter(), true);
        //}
       //b2body.applyLinearImpulse(new Vector2(.015f, 0f ) , b2body.getWorldCenter(), true); // keep applying movement
            // keep applying movement
        //update our sprite to correspond with the position of our Box2D body
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

    }


    public State getState(){
        //Test to Box2D for velocity on the X and Y-Axis
        //if player is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump state
        if(playerIsDead)
            return State.DEAD;
            //if Y-Axis is negative, player is falling
        else if(0 > b2body.getLinearVelocity().y)
            return State.FALLING;
        else if((b2body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
            //if none of these return then he must be running
        else //if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        //if none of these return then he must be standing
        /*else
            return State.RUNNING;*/
    }


    public TextureRegion getFrame(float dt){
        //get player current state. ie. jumping, running, falling, etc.
        currentState = getState();

        TextureRegion region;

        //depending on the state, get corresponding animation keyFrame.
        switch(currentState){

            case FALLING:
                region = (TextureRegion) screen.playerFall;
                break;
            case JUMPING:
                region = (TextureRegion) screen.playerJump;
                break;
            /*case DEAD:
                region = (TextureRegion) screen.playerDead.getKeyFrame(getStateTimer());
                break;*/
            case RUNNING:
            default:
                region = (TextureRegion) screen.playerRun.getKeyFrame(stateTimer, true);
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
        b2body.applyLinearImpulse(new Vector2(.7f, 0f ) , b2body.getWorldCenter(), true); //start movement off for one frame


    }


    public void jump(){
        if ( currentState != State.JUMPING ) {
            b2body.applyLinearImpulse(new Vector2(0, 2f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public float getStateTimer(){
        return stateTimer;
    }


    public void setPlayerIsDead() {playerIsDead = true;}

    public void draw(Batch batch){
        super.draw(batch);
    }
}
