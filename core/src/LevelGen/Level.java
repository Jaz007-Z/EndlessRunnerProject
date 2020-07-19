package LevelGen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Endless;

import java.util.ArrayList;

import javax.swing.plaf.nimbus.State;

import Sprites.Player;

public class Level {

    //variables for keeping track of general level generation
    protected float spacing;
    protected float previousVerticality;
    protected float newVerticality;
    protected float previousEnd;
    protected float newEnd;


    //textures



    public TextureRegion spike = new TextureRegion(new Texture("spike.png"));
    protected Texture groundTex = new Texture("ground.png");
    public TextureRegion ground = new TextureRegion(groundTex);
    protected Texture fire;
    protected Texture platform;



    //area size(s)
    int areaSize = 5;
    int areaSizePlatform = 7;
    protected float groundLengthD2 = 90;


    //body array for disposal
    ArrayList<Body> bodiesGround;
    ArrayList<Body> bodiesFire;
    ArrayList<Body> bodiesCoin;
    ArrayList<Body> bodiesPlatform;

    //coinVariables
    float coinLocation;
    float coinSpacing;
    float coinHeight = -15;
    float coinMax = 120; //maximum coin spacing
    float coinMin = 10;; //minimum coin spacing
    float coinBufferSpace = 0;; //space before end of a ground that coin can't appear


    //fireVariables
    float fireLocation;
    float fireSpacing;
    float fireMax = 120;; //maximum fire spacing
    float fireMin = 60;; //minimum fire spacing
    float fireBufferSpace = 40;; //space before end of a ground that fire can't appear


    //holeVariables
    float holeLocation;
    float holeSpacing = 30;
    float holeMax = 50; //maximum hole spacing
    float holeMin = 20; //minimum hole spacing

    //platformVariables
    float platformWidthD2 = 15; //because hx for a polygon acts like a radius, this is the platform width divided by 2 "D2"
    float platformHeight = 6;
    float platformSpacing;
    float platformLocation;
    float platformMax = 50; //maximum platform spacing
    float platformMin = 20; //minimum platform spacing



    protected World world;

    protected final static float LOWFLOOR = 0 / Endless.PPM; //will provide for set verticality levels later
    protected final static float MEDIUMFLOOR = 0 / Endless.PPM;
    protected final static float HIGHFLOOR = 0 / Endless.PPM;




    public Level (World world) {
        //regular variables
        this.world = world;
        newEnd = -70;
        spacing = 10;
        //fire variables
        fireSpacing = 30;
        bodiesGround = new ArrayList<Body>();
        bodiesFire = new ArrayList<Body>();
        bodiesCoin = new ArrayList<Body>();
        bodiesPlatform = new ArrayList<Body>();



    }
    public Level (World world, float newEnd) {
        this.world = world;
        this.newEnd = newEnd;
        spacing = 10;
        groundLengthD2 = 50;
        //fire variables
        fireSpacing = 30;


    }

    public float getGroundLengthD2() {
        return groundLengthD2;
    }

    public ArrayList<Body> getBodiesGround() {
        return bodiesGround;
    }
    public ArrayList<Body> getBodiesCoin() {
        return bodiesCoin;
    }
    public ArrayList<Body> getBodiesFire() {
        return bodiesFire;
    }
    public float getNewEnd() {
        return newEnd;
    }

    public void setNewEnd(float newEnd) {
        this.newEnd = newEnd;
    }



    public void generateDesign() {

        //static creation for testing
        /*Body b2bodyT;
        BodyDef bdefT = new BodyDef();
        bdefT.position.set(0 / Endless.PPM, -60 / Endless.PPM); //position of the polygon
        bdefT.type = BodyDef.BodyType.StaticBody;
        b2bodyT = world.createBody(bdefT);
        FixtureDef fdefT = new FixtureDef();
        //makes a box. It can be a straight line like now or vertical. hx is length, hy is height. vector2's x sets new center for box relative to position.
        PolygonShape groundShapeT = new PolygonShape();
        groundShapeT.setAsBox(50 / Endless.PPM, 0 / Endless.PPM, new Vector2(50 / Endless.PPM, 0 / Endless.PPM), 0 / Endless.PPM);
        fdefT.shape = groundShapeT;
        b2bodyT.createFixture(fdefT).setUserData(this);*/

        //setting loop up
        Body b2body;
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape groundShape = new PolygonShape();
        //fdef.friction = 0.3f;
        //making in a loop for real procedural generation
        for (int i = 0; i < areaSize; i++) {
            bdef.position.set(newEnd / Endless.PPM, -60 / Endless.PPM); //position of the polygon
            bdef.type = BodyDef.BodyType.StaticBody;
            b2body = world.createBody(bdef);

            //makes a box. It can be a straight line like now or vertical. hx is length, hy is height. vector2's x sets new center for box relative to position.

            groundShape.setAsBox(groundLengthD2 / Endless.PPM, 1 / Endless.PPM, new Vector2(groundLengthD2 / Endless.PPM, 0 / Endless.PPM), 0 / Endless.PPM);

            fdef.shape = groundShape;
            b2body.createFixture(fdef).setUserData(this);
            bodiesGround.add(b2body);

            previousEnd = newEnd;

            newEnd = previousEnd + (groundLengthD2 * 2);

            generateCoin(previousEnd, newEnd);
        }
    }

    public void generateCoin(float previousEnd, float newEnd) {

        Body b2Coin;
        BodyDef bdefCoin = new BodyDef();
        bdefCoin.gravityScale = 0;
        FixtureDef fdefCoin = new FixtureDef();
        fdefCoin.isSensor = true;
        CircleShape shape = new CircleShape();
        coinLocation = previousEnd;

        for (int i = 0; i < areaSize; i++) {
            coinSpacing = (float) (Math.random() * (coinMax - coinMin + 1) + coinMin);
            coinLocation += coinSpacing; //make fireSpacing random later on
            if (coinLocation > newEnd - coinBufferSpace) {//make new minus the spacing if there are holes in the level area
                break;
            }

            bdefCoin.position.set(coinLocation / Endless.PPM, coinHeight / Endless.PPM); //position of the polygon
            bdefCoin.type = BodyDef.BodyType.DynamicBody;
            b2Coin = world.createBody(bdefCoin);
            shape.setRadius(3 / Endless.PPM);
            fdefCoin.shape = shape;
            b2Coin.createFixture(fdefCoin).setUserData(this);
            bodiesCoin.add(b2Coin);
        }
    }



    public void dispose() {
        for (Body b : bodiesGround) {
            world.destroyBody(b);
        }
        bodiesGround.clear();

        for (Body b : bodiesFire) {
            world.destroyBody(b);
        }
        bodiesFire.clear();

        for (Body b : bodiesCoin) {
            world.destroyBody(b);
        }
        bodiesCoin.clear();

        for (Body b : bodiesPlatform) {
            world.destroyBody(b);
        }
        bodiesPlatform.clear();

    }

}