package com.simplegame.game.systems;

import com.badlogic.gdx.Gdx;
import com.simplegame.game.GameData;
import com.simplegame.game.userdata.UserData;

import ownLib.Own;
import ownLib.listener.OnContactListener;

/**
 * Created by ravi on 09.01.17.
 */

public class ContactListenerSystem implements OnContactListener {
    ControlsSystem controlsSystem;
    public ContactListenerSystem(ControlsSystem controlsSystem) {
        this.controlsSystem = controlsSystem;
    }

    private boolean exactMatch(UserData userDataA, UserData userDataB, String idA, String idB) {
        if (userDataA.getId().equals(idA) && userDataB.getId().equals(idB)
                || userDataB.getId().equals(idA) && userDataA.getId().equals(idB)) {
            return true;
        }
        return false;
    }

    private boolean startMatch(UserData userDataA, UserData userDataB, String idA, String idB) {
        if (userDataA.getId().startsWith(idA) && userDataB.getId().startsWith(idB)
                || userDataB.getId().startsWith(idA) && userDataA.getId().startsWith(idB)) {
            return true;
        }
        return false;
    }

    @Override
    public void onContact(UserData userDataA, UserData userDataB, float normalImpulse) {
        if (exactMatch(userDataA, userDataB, "ball", "spike")) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    Own.log("Hit the spike");
//                    controlsSystem.setState(GameData.LEVEL_END);
                }
            });
        }

        if (exactMatch(userDataA, userDataB, "ball", "dboxvert")) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    Own.log("Hit the spike");
//                    controlsSystem.setState(GameData.LEVEL_END);
                }
            });
        }

        if (startMatch(userDataA, userDataB, "ball", "fireball")) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    Own.log("Hit the fireball");
                    controlsSystem.setState(GameData.LEVEL_END);
                }
            });
        }

    }
}