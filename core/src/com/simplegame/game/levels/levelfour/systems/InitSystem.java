package com.simplegame.game.levels.levelfour.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.userdata.UserData;

/**
 * Created by ravi on 10.11.16.
 */

public class InitSystem extends BaseSystem implements AfterSceneInit {
    ComponentMapper<PhysicsBody> physicsCm;
    PhysicsSystem physicsSystem;
    VisIDManager idManager;

    @Override
    public void afterSceneInit() {
        Entity entity = idManager.get("boundary");
        addUserData(entity, "boundary", "polygon");

        entity = idManager.get("upslopebox");
        addUserData(entity, "upslopebox", "polygon");

        entity = idManager.get("downslopebox");
        addUserData(entity, "downslopebox", "polygon");

        entity = idManager.get("topbox");
        addUserData(entity, "topbox", "polygon");

        entity = idManager.get("fourbox");
        addUserData(entity, "fourbox", "polygon");

        entity = idManager.get("spike");
        addUserData(entity, "spike", "polygon");
    }

    private void addUserData(Entity entity, String id, String type) {
        Body body = physicsCm.get(entity).body;
        Array<Fixture> fixtureArray = body.getFixtureList();
        for (int i = 0; i < fixtureArray.size; i++) {
            fixtureArray.get(i).setUserData(new UserData(id, type, body.getPosition()));
        }
    }

    @Override
    protected void processSystem() {

    }


}
