package com.simplegame.game.components;

/**
 * Created by ravi on 30.04.17.
 */

public class Timeout extends Thread {
    Runnable runnable;
    int delayInMilli = 0;

    public Timeout(Runnable runnable, int delayInMilli) {
        this.runnable = runnable;
        this.delayInMilli = delayInMilli;
        this.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(this.delayInMilli);
            this.runnable.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
