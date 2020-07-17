package LevelGen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Endless;

import java.util.ArrayList;

public class Level {

    //variables for keeping track of general level generation
    protected float spacing;
    protected float previousVerticality;
    protected float newVerticality;
    protected float previousEnd;
    protected float newEnd;


    //textures
    protected Texture ground;
    protected Texture fire;
    protected Texture platform;

    //area size(s)
    int areaSize = 5;
    int areaSizePlatform = 7;
    protected float groundLengthD2 = 75;


    //body array for disposal
    ArrayList<Body> bodies;

    //fireVariables
    float fireLocation;
    float fireSpacing;
    float fireMax = 75;; //maximum fire spacing
    float fireMin = 40;; //minimum fire spacing
    float fireBufferSpace = 20;; //space before end of a ground that fire can't appear


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
        bodies = new ArrayList<Body>();


    }
    public Level (World world, float newEnd) {
        this.world = world;
        this.newEnd = newEnd;
        spacing = 10;
        groundLengthD2 = 50;
        //fire variables
        fireSpacing = 30;

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
        for (int i = 0; i < 30; i++) {
            bdef.position.set(newEnd / Endless.PPM, -60 / Endless.PPM); //position of the polygon
            bdef.type = BodyDef.BodyType.StaticBody;
            b2body = world.createBody(bdef);

            //makes a box. It can be a straight line like now or vertical. hx is length, hy is height. vector2's x sets new center for box relative to position.

            groundShape.setAsBox(groundLengthD2 / Endless.PPM, 1 / Endless.PPM, new Vector2(groundLengthD2 / Endless.PPM, 0 / Endless.PPM), 0 / Endless.PPM);

            fdef.shape = groundShape;
            b2body.createFixture(fdef).setUserData(this);
            bodies.add(b2body);

            previousEnd = newEnd;

            newEnd = previousEnd + (groundLengthD2 * 2) ;
            //newEnd = previousEnd + (groundLengthD2 * 2);
        }
    }



    public void dispose() {
        for (Body b : bodies) {
            world.destroyBody(b);
        }
        bodies.clear();
        System.out.println("bodies dispose");
    }





}