package com.jauntymarble.game.userdata;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by ravi on 29.08.16.
 */
public class UserData {
    protected String id = null;
    protected String type = "";
    protected Vector2 pos;

    public UserData(String id, String type, Vector2 pos) {
        this.id = id;
        this.type = type;
        this.pos = pos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
