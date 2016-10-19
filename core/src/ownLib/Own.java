package ownLib;

import java.util.Random;

import ownLib.assets.Assets;
import ownLib.box2d.Box2d;

public class Own {

    public static Random rand = new Random();
    public static Device device = new Device();
    //    public static Audio audio = new Audio();
    public static Logger logger = new Logger();
    public static Storage storage = new Storage();
    public static Assets assets = new Assets();
    public static IO io = new IO();
    public static Text text = new Text();
    public static Box2d box2d = new Box2d();
    public static BodyContact bodyContact = new BodyContact();

    public static void dispose() {
        assets.dispose();
    }

    public static int getRandomInRange(int lowerBound, int upperBound) {
        Random rand = new Random();
        return rand.nextInt(upperBound - lowerBound) + lowerBound;
    }

    public static void log(String TAG, String msg) {
        Own.logger.log(TAG, msg);
    }

    public static void log(String msg) {
        Own.logger.log(msg);
    }
}
