package ownLib.box2d;

import com.simplegame.game.userdata.UserData;

/**
 * Created by ravi on 08.08.16.
 */
public class Box2d {
    public static Factory factory = new Factory();
    public static GUI gui = new GUI();

    public static boolean exactMatch(UserData userDataA, UserData userDataB, String idA, String idB) {
        if (userDataA.getId().equals(idA) && userDataB.getId().equals(idB)
                || userDataB.getId().equals(idA) && userDataA.getId().equals(idB)) {
            return true;
        }
        return false;
    }

    public static boolean startMatch(UserData userDataA, UserData userDataB, String idA, String idB) {
        if (userDataA.getId().startsWith(idA) && userDataB.getId().startsWith(idB)
                || userDataB.getId().startsWith(idA) && userDataA.getId().startsWith(idB)) {
            return true;
        }
        return false;
    }

}
