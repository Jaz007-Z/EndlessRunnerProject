package LevelGen;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Endless;

public class HoleArea extends Level {
    public HoleArea(World world) {
        super(world);
    }

    @Override
    public void generateDesign() {

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

            previousEnd = newEnd;

            holeSpacing = (float) (Math.random() * (holeMax - holeMin + 1) + holeMin);
            holeLocation += holeSpacing; //makes the space between holes random within set bounds

            newEnd = previousEnd + (groundLengthD2 * 2) + holeSpacing;
            //newEnd = previousEnd + (groundLengthD2 * 2);

        }
    }

}
