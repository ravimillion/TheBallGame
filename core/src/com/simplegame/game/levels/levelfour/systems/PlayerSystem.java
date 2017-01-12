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
import com.simplegame.game.GameController;
import com.simplegame.game.levels.GameData;
import com.simplegame.game.objects.Ball;

import ownLib.BodyContact;
import ownLib.Own;

public class PlayerSystem extends BaseSystem implements AfterSceneInit {
    public int state = GameData.RUNNING;
    public Body body;
    ComponentMapper<PhysicsBody> physicsCm;
    //     needed for drawing the ball
    CameraManager cameraManager;
    private PhysicsSystem physicsSystem;
    private ControlsSystem controlsSystem;
    private Ball ball;
    private World physicsWorld;
    private BodyContact bodyContact;
    private OrthographicCamera camera;
    private GameController gameController;


    public PlayerSystem(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void afterSceneInit() {
        physicsWorld = physicsSystem.getPhysicsWorld();
        Own.box2d.factory.setWorld(physicsWorld);

        bodyContact = new BodyContact();
        bodyContact.setContactListener(new ContactListenerSystem(controlsSystem));
        physicsWorld.setContactListener(bodyContact);

        JsonValue levelData = new JsonReader().parse(Gdx.files.internal("json/leveldata.json")).get("4");
        // create the ball
        ball = new Ball(this.gameController.spriteBatch, levelData.get("ball"));
        body = ball.getBody();
        state = GameData.RUNNING;
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    protected void processSystem() {
        if (state == GameData.RUNNING) {
            physicsSystem.setEnabled(true);
            handleInput();

            if (isLevelEnd()) {
                controlsSystem.setState(GameData.LEVEL_END);
            }
        } else {
            physicsSystem.setEnabled(false);
        }
        drawBall();
    }

    private boolean isLevelEnd() {
        return body.getPosition().x > this.gameController.WORLD_WIDTH - 5;
    }

    private void drawBall() {
        gameController.spriteBatch.setProjectionMatrix(cameraManager.getCamera().combined);
        gameController.spriteBatch.begin();
        ball.drawGui();
        gameController.spriteBatch.end();
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
}





































