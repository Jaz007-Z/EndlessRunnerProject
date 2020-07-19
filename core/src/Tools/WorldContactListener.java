package Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import Scenes.Hud;
import Screens.PlayScreen;
import Sprites.Player;

public class WorldContactListener implements ContactListener {
    Player player;
    @Override
    public void beginContact(Contact contact) {
        System.out.println("Begin Contact");

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == "PlayerBody" || fixB.getUserData() == "PlayerBody"){
            System.out.println("I AM A SPRITE!!!!!!!");
            //screen.coinCollect();

        }

        /*if(fixA.getUserData() == "playerBody" || fixB.getUserData() == "playerBody"){
            Fixture player = fixA.getUserData() == "playerBody" ? fixA : fixB;
            Fixture object = player == fixA ? fixB : fixA;

        }*/


    }

    @Override
    public void endContact(Contact contact) {
        System.out.println("End Contact");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
