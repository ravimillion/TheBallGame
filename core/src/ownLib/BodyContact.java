package ownLib;

import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.simplegame.game.levels.LevelScreen;
import com.simplegame.game.userdata.UserData;

import ownLib.listener.OnContactListener;

public class BodyContact implements ContactListener {
    private String TAG = this.getClass().toString();
    private LevelScreen levelScreen = null;
    private OnContactListener contactListener;

    @Override
    public void beginContact(com.badlogic.gdx.physics.box2d.Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
    }

    @Override
    public void endContact(com.badlogic.gdx.physics.box2d.Contact contact) {

    }

    @Override
    public void preSolve(com.badlogic.gdx.physics.box2d.Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(com.badlogic.gdx.physics.box2d.Contact contact, ContactImpulse impulse) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        UserData userDataA = (UserData) fixtureA.getUserData();
        UserData userDataB = (UserData) fixtureB.getUserData();

        if (userDataA != null && userDataB != null) {
            notifyListener(userDataA, userDataB, impulse.getNormalImpulses()[0]);
        }

    }

    public void notifyListener(UserData userDataA, UserData userDataB, float normalImpulse) {
        if (levelScreen != null) levelScreen.contactListener(userDataA, userDataB, normalImpulse);
        if (contactListener != null) contactListener.onContact(userDataA, userDataB, normalImpulse);
    }

    public void setContactListener(LevelScreen screen) {
        levelScreen = screen;
    }

    public void setContactListener(OnContactListener listener) {
        contactListener = listener;
    }
}
