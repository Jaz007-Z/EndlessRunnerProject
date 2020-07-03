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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Endless;
import Screens.PlayScreen;

public class Player extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING, HIT, ATTACK, DEAD };
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private PlayScreen screen;
  
    public Player(PlayScreen screen){
        //initialize default values
        //this.screen = screen;
        //this.world = screen.getWorld();


        //define mario in Box2d
        definePlayer();


    }
  
    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(0 / Endless.PPM, -60 / Endless.PPM); //position of the chain
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        //makes a box. It can be a straight line like now or vertical. hx is length, hy is height. vector2's x sets new center for box relative to position.
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(50 / Endless.PPM ,0 / Endless.PPM, new Vector2(50 / Endless.PPM ,0 / Endless.PPM), 0 / Endless.PPM );


        fdef.shape = groundShape;
        b2body.createFixture(fdef).setUserData(this);
    }
  
}
