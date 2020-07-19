package Tools;
import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.physics.box2d.Contact;
        import com.badlogic.gdx.physics.box2d.ContactImpulse;
        import com.badlogic.gdx.physics.box2d.ContactListener;
        import com.badlogic.gdx.physics.box2d.Fixture;
        import com.badlogic.gdx.physics.box2d.Manifold;
        import com.mygdx.game.Endless;

        import LevelGen.Level;
        import Sprites.Player;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        System.out.println("Begin Contact");
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();


        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case Endless.PLAYER_BIT | Endless.COIN_BIT:
                if (fixA.getFilterData().categoryBits == Endless.PLAYER_BIT)
                    ((Level) fixB.getUserData()).coinCollect((Player) fixA.getUserData());
                else
                    ((Level) fixA.getUserData()).coinCollect((Player) fixB.getUserData());
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}