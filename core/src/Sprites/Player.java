package Sprites;

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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Endless;


public class Player extends Sprite{
    public enum State { FALLING, JUMPING, STANDING, RUNNING, HIT, ATTACK, DEAD };
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;
    private TextureRegion playerStand;
    private Animation playerRun;
    private TextureRegion playerJump;
    private TextureRegion playerDead;

    private boolean playerIsDead;
    private boolean timeToRedefinePlayer;
    private float stateTimer;
    private boolean runningRight;

    //private PlayScreen screen;


    public Player(){
        //initialize default values
        //this.screen = screen;
        //this.world = screen.getWorld();
        /*Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));*/

        //define mario in Box2d
        //definePlayer();

        //setBounds(0, 0, 16 / Endless.PPM, 16 / Endless.PPM);
        //setRegion(playerStand);
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

    public TextureRegion getFrame(float dt){
        //get marios current state. ie. jumping, running, standing...
        currentState = getState();

        TextureRegion region = new TextureRegion();

        //depending on the state, get corresponding animation keyFrame.
        switch(currentState){
            case DEAD:
                region = playerDead;
                break;
            case JUMPING:
                region = playerJump;
                break;
            case RUNNING:
                //region = playerRun.getKeyFrame(stateTimer, true);
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

    public void update(float dt){


        b2body.applyLinearImpulse(new Vector2(.015f, 0f ) , b2body.getWorldCenter(), true); // keep applying movement
        //update our sprite to correspond with the position of our Box2D body
        /*setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        //update sprite with the correct frame depending on marios current action
        setRegion(getFrame(dt));
        if(timeToRedefinePlayer)
            redefinePlayer();*/
        //commented out code that makes it crash. We might need to start multi-threading. I'm not sure why it crashed
        //but from what Nathanial said maybe we need to think about performance more

    }

    public void redefinePlayer(){
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef2 = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Endless.PPM);

        fdef2.shape = shape;
        b2body.createFixture(fdef2).setUserData(this);

        timeToRedefinePlayer = false;

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
    
    
    public void setPlayerIsDead() {playerIsDead = true;}

    public void draw(Batch batch){
        super.draw(batch);
    }

}
