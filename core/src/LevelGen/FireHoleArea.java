package LevelGen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Endless;

public class FireHoleArea extends Level  {
    public FireHoleArea(World world) {
        super(world);
    }


    @Override
    public void generateDesign() {
        //setting loop up
        Body b2body;
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape groundShape = new PolygonShape();
        //making in a loop for real procedural generation
        for (int i = 0; i < areaSize; i++) {
            bdef.position.set(newEnd / Endless.PPM, -60 / Endless.PPM); //position of the polygon
            bdef.type = BodyDef.BodyType.StaticBody;
            b2body = world.createBody(bdef);

            //makes a box. It can be a straight line like now or vertical. hx is length, hy is height. vector2's x sets new center for box relative to position.

            groundShape.setAsBox(groundLengthD2 / Endless.PPM, 1 / Endless.PPM, new Vector2(groundLengthD2 / Endless.PPM, 0 / Endless.PPM), 0 / Endless.PPM);

            fdef.shape = groundShape;
            b2body.createFixture(fdef).setUserData(this);




            holeSpacing = (float) (Math.random() * (holeMax - holeMin + 1) + holeMin); //makes the space between holes random within set bounds

            previousEnd = newEnd;
            newEnd = previousEnd + (groundLengthD2 * 2) + holeSpacing;

            //if (i == 0 || i == 1)
                generateFire(previousEnd, newEnd - holeSpacing);
        }
    }


    public void generateFire(float previousEnd, float newEnd) {

        Body b2Fire;
        BodyDef bdefFire = new BodyDef();
        bdefFire.gravityScale = 0;
        FixtureDef fdefFire = new FixtureDef();
        fdefFire.isSensor = true;
        CircleShape shape = new CircleShape();
        fireLocation = previousEnd;

        for (int i = 0; i < areaSize; i++) {
            fireSpacing = (float) (Math.random() * (fireMax - fireMin + 1) + fireMin);
            fireLocation += fireSpacing; //make fireSpacing random later on
            if (fireLocation > newEnd - fireBufferSpace) {//make new minus the spacing if there are holes in the level area
                break;
            }

            bdefFire.position.set(fireLocation / Endless.PPM, -60 / Endless.PPM); //position of the polygon
            bdefFire.type = BodyDef.BodyType.DynamicBody;
            b2Fire = world.createBody(bdefFire);
            shape.setRadius(5 / Endless.PPM);
            fdefFire.shape = shape;
            b2Fire.createFixture(fdefFire).setUserData(this);
        }
    }


}
