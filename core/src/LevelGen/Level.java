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

    //variables for keeping track of general level generation
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

    //area size(s)
    int areaSize = 10;


    //fireVariables
    float fireLocation;
    float fireSpacing;
    float fireMax = 65; //maximum fire spacing
    float fireMin = 25; //minimum fire spacing
    float fireBufferSpace = 15;; //space before end of a ground that fire can't appear


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
        groundLengthD2 = 50;
        //fire variables
        fireSpacing = 30;


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

            groundShape.setAsBox(/*groundLengthD2 / Endless.PPM*/Endless.V_WIDTH, 1 / Endless.PPM, new Vector2(groundLengthD2 / Endless.PPM, 0 / Endless.PPM), 0 / Endless.PPM);

            fdef.shape = groundShape;
            b2body.createFixture(fdef).setUserData(this);

            previousEnd = newEnd;

            newEnd = previousEnd + (groundLengthD2 * 2) + spacing;
            //newEnd = previousEnd + (groundLengthD2 * 2);


            //maybe have it return world to keep it as one world, or have multiple worlds so disposal is easy if it causes no issues
            //return world;
        }
    }

    public void dispose() {
        this.world.dispose();
    }





}
