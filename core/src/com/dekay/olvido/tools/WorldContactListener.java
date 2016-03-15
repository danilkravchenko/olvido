package com.dekay.olvido.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.dekay.olvido.Constants;
import com.dekay.olvido.objects.Ball;
import com.dekay.olvido.objects.Player;

/**
 * Created by Крава on 05.02.2016.
 */
public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();
        int cDef = fA.getFilterData().categoryBits | fB.getFilterData().categoryBits;
        switch (cDef) {
            case Constants.BALL_BIT | Constants.OBJECT_BIT:
                if (fA.getFilterData().categoryBits == Constants.BALL_BIT)
                    ((Ball) fA.getUserData()).onObjectHit(contact);
                else if (fB.getFilterData().categoryBits == Constants.BALL_BIT)
                    ((Ball) fB.getUserData()).onObjectHit(contact);
                break;
            case Constants.PLAYER_BIT | Constants.BALL_BIT:
                if (fA.getFilterData().categoryBits == Constants.PLAYER_BIT)
                    ((Player) fA.getUserData()).onBallHit();
                else if (fB.getFilterData().categoryBits == Constants.PLAYER_BIT)
                    ((Player) fB.getUserData()).onBallHit();
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

