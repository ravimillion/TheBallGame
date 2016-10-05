package ownLib;

import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.simplegame.game.levels.LevelScreen;
import com.simplegame.game.userdata.UserData;

public class BodyContact implements ContactListener {
    private String TAG = this.getClass().toString();
    private LevelScreen levelScreen = null;

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

        float[] impulseArray = impulse.getNormalImpulses();
        notifyListener((UserData) fixtureA.getBody().getUserData(), (UserData) fixtureB.getBody().getUserData(), impulseArray[0]);
    }

    public void notifyListener(UserData userDataA, UserData userDataB, float normalImpulse) {
        levelScreen.contactListener(userDataA, userDataB, normalImpulse);
    }

    public void setContactListener(LevelScreen screen) {
        levelScreen = screen;
    }
}
