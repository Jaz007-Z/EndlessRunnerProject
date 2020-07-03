package LevelGen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Endless;

public class Level {
    private float spacing;
    private float previousVerticality;
    private float newVerticality;
    private float previousEnd;
    private float newEnd;

    private World world;

    private final static float LOWFLOOR = 0 / Endless.PPM; //will provide for set verticality levels later
    private final static float MEDIUMFLOOR = 0 / Endless.PPM;
    private final static float HIGHFLOOR = 0 / Endless.PPM;


    public Level (World world) {
        this.world = world;
        newVerticality = -140;
        spacing = 20;


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
        for (int i = 0; i < 3; i++) {
            bdef.position.set(newVerticality / Endless.PPM, -60 / Endless.PPM); //position of the polygon
            bdef.type = BodyDef.BodyType.StaticBody;
            b2body = world.createBody(bdef);

            //makes a box. It can be a straight line like now or vertical. hx is length, hy is height. vector2's x sets new center for box relative to position.

            groundShape.setAsBox(25 / Endless.PPM, 0 / Endless.PPM, new Vector2(25 / Endless.PPM, 0 / Endless.PPM), 0 / Endless.PPM);

            fdef.shape = groundShape;
            b2body.createFixture(fdef).setUserData(this);

            previousVerticality = newVerticality;
            newVerticality = previousVerticality + 50 + spacing;




       }
    }




}
