package com.jauntymarble.game.userdata;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ravi on 13.09.16.
 */
public class JsonUserData extends UserData {
    private Vector2 origin;
    private String name;
    private float width;
    private float height;
    private String jsonFileName;

    public JsonUserData(String id, String type, Vector2 pos, Vector2 origin, String name, String jsonFileName, float width, float height) {
        super(id, type, pos);

        this.origin = origin;
        this.name = name;
        this.width = width;
        this.height = height;
        this.jsonFileName = jsonFileName;
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public String getName() {
        return name;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public String getJsonFileName() {
        return jsonFileName;
    }
}
