package com.simplegame.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.simplegame.game.screens.GameEntry;

public abstract class GameObject {
    protected World world;
    protected GameEntry gameEntry;

    protected String id;
    protected Body body;
    protected float angle;
    protected float width;
    protected float height;
    protected Vector2 position;

    public abstract Vector2 getPosition();

    public abstract void setPosition(Vector2 pos);

    public abstract void handleInput(Vector3 touchPoint);

    public abstract void update(float deltaTime);

    public abstract void drawGui();

    public abstract void create();

    public abstract float getWidth();

    public abstract float getHeight();
}
