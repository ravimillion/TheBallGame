package com.simplegame.game.levels;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.simplegame.game.objects.WorldBoundry;
import com.simplegame.game.screens.GameScreen;
import com.simplegame.game.userdata.UserData;
import ownLib.listener.OnTouchListener;


public abstract class LevelScreen extends GameScreen implements OnTouchListener{
    public abstract void contactListener(UserData bodyA, UserData bodyB, float normalImpulse);

    protected World world;
    protected WorldBoundry worldBoundry = null;
    protected Box2DDebugRenderer debugRenderer;

    protected float WORLD_WIDTH = 0;
    protected float WORLD_HEIGHT = 0;

    protected float gravityX = 0;
    protected float gravityY = 0;

    protected abstract void setupLevel();
    protected void drawBorder() {
        worldBoundry = new WorldBoundry(WORLD_WIDTH, WORLD_HEIGHT);
        worldBoundry.updateWorldBoundry(WorldBoundry.BOTTOM, new Vector2(0, 4.3f), 0);

    }
}
