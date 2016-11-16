package ownLib.listener;

import com.simplegame.game.userdata.UserData;

/**
 * Created by ravi on 09.11.16.
 */

public interface OnContactListener {
    public void onContact(UserData userDataA, UserData userDataB, float normalImpulse);
}
