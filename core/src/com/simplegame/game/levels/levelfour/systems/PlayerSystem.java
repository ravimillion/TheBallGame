package com.simplegame.game.levels.levelfour.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.levels.GameState;
import com.simplegame.game.objects.Ball;
import com.simplegame.game.screens.GameEntry;
import com.simplegame.game.userdata.UserData;

import ownLib.BodyContact;
import ownLib.Own;
import ownLib.listener.OnContactListener;

public class PlayerSystem extends BaseSystem implements AfterSceneInit, OnContactListener {
    public GameState state = GameState.RUNNING;
    public Body body;
    ComponentMapper<PhysicsBody> physicsCm;
    // needed for drawing the ball
    CameraManager cameraManager;
    private PhysicsSystem physicsSystem;
    private ControlsSystem controlsSystem;
    private Ball ball;
    private World physicsWorld;
    private BodyContact bodyContact;  // may be contact system can be created seperately
    private OrthographicCamera camera;


    @Override
    public void afterSceneInit() {
        physicsWorld = physicsSystem.getPhysicsWorld();
        Own.box2d.factory.setWorld(physicsWorld);

        bodyContact = new BodyContact();
        bodyContact.setContactListener(this);
        physicsWorld.setContactListener(bodyContact);

        JsonValue levelData = new JsonReader().parse(Gdx.files.internal("json/leveldata.json")).get("4");
        // create the ball
        ball = new Ball(GameEntry.batch, levelData.get("ball"));
        body = ball.getBody();
        state = GameState.RUNNING;
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    protected void processSystem() {
        if (state == GameState.RUNNING) {
            physicsSystem.setEnabled(true);
            handleInput();

            if (isLevelEnd()) {
                controlsSystem.setState(GameState.LEVEL_END);
            }
        } else {
            physicsSystem.setEnabled(false);
        }
        drawBall();
    }

    private boolean isLevelEnd() {
        return body.getPosition().x > GameEntry.WORLD_WIDTH - 5;
    }

    private void drawBall() {
        GameEntry.batch.setProjectionMatrix(cameraManager.getCamera().combined);
        GameEntry.batch.begin();
        ball.drawGui();
        GameEntry.batch.end();
    }

    private void handleInput() {
        int BALL_FORCE = 1500;

        if (Gdx.input.isTouched()) {
            if (Gdx.input.getX() > Own.device.getScreenWidth() / 2) {
                body.applyTorque(BALL_FORCE * -1, true);
            } else {
                body.applyTorque(BALL_FORCE, true);
            }
        }
    }

    @Override
    public void onContact(UserData userDataA, UserData userDataB, float normalImpulse) {

        String idA = userDataA.getId();
        String idB = userDataB.getId();

        if (idA.startsWith("ball") && idB.equals("spike") || idB.startsWith("ball") && idA.equals("spike")) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    Own.log("Hit the spike");
                }
            });
        }
    }
}
