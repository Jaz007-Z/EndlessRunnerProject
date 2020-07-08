package LevelGen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Endless;

public class Level {

    //variables for keeping track of level generation
    protected float spacing;
    protected float previousVerticality;
    protected float newVerticality;
    protected float previousEnd;
    protected float newEnd;
    protected float groundLengthD2; //because hx for a polygon acts like a radius, this is the ground length divided by 2 "D2"

    //textures
    protected Texture ground;
    protected Texture fire;
    protected Texture platform;


    //fireVariables
    float fireLocation;
    float fireSpacing;
    float fireMax; //maximum fire spacing
    float fireMin; //mimumum fire spacing
    float fireBufferSpace; //space before end of a ground that fire can't appear


    protected World world;

    protected final static float LOWFLOOR = 0 / Endless.PPM; //will provide for set verticality levels later
    protected final static float MEDIUMFLOOR = 0 / Endless.PPM;
    protected final static float HIGHFLOOR = 0 / Endless.PPM;




    public Level (World world) {
        //regular variables
        this.world = world;
        newEnd = -45;
        spacing = 10;
        groundLengthD2 = 50;
        //fire variables
        fireSpacing = 30;
        fireMin = 25;
        fireMax = 65;
        fireBufferSpace = 15;


    }
    public Level (World world, float newEnd) {
        this.world = world;
        this.newEnd = newEnd;
        spacing = 10;
        groundLengthD2 = 50;
        //fire variables
        fireSpacing = 30;
        fireMin = 25;
        fireMax = 70;

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
        //making in a loop for real procedural generation
        for (int i = 0; i < 30; i++) {
            bdef.position.set(newEnd / Endless.PPM, -60 / Endless.PPM); //position of the polygon
            bdef.type = BodyDef.BodyType.StaticBody;
            b2body = world.createBody(bdef);

            //makes a box. It can be a straight line like now or vertical. hx is length, hy is height. vector2's x sets new center for box relative to position.

            groundShape.setAsBox(groundLengthD2 / Endless.PPM, 1 / Endless.PPM, new Vector2(25 / Endless.PPM, 0 / Endless.PPM), 0 / Endless.PPM);

            fdef.shape = groundShape;
            b2body.createFixture(fdef).setUserData(this);

            previousEnd = newEnd;

            newEnd = previousEnd + (groundLengthD2 * 2) + spacing;
            //newEnd = previousEnd + (groundLengthD2 * 2);


            //maybe have it return world to keep it as one world, or have multiple worlds so disposal is easy if it causes no issues
            //return world;
        }
    }

    public void dispose(World world) {
        world.dispose();
        this.world.dispose();
    }





}
