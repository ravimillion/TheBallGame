package com.simplegame.game.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.GameController;
import com.simplegame.game.GameData;
import com.simplegame.game.objects.Ball;

import ownLib.BodyContact;
import ownLib.Own;

public class PlayerSystem extends BaseSystem implements AfterSceneInit {
    public int state = GameData.RUNNING;
    public Body body;
    private ComponentMapper<PhysicsBody> physicsCm;
    private ComponentMapper<VisID> visIDCm;
    private CameraManager cameraManager;
    private ComponentMapper<Transform> transformCm;
    private VisIDManager idManager;
    private PhysicsSystem physicsSystem;
    private ControlsSystem controlsSystem;
    private ContactListenerSystem contactListenerSystem;
    private Ball ball;
    private World physicsWorld;
    private BodyContact bodyContact;
    private OrthographicCamera camera;
    private GameController gameController;

    private float TOP_ANGULAR_VELOCITY = 20;
    private float TOP_LINEAR_VELOCITY = 45;

    public PlayerSystem(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void afterSceneInit() {
        physicsWorld = physicsSystem.getPhysicsWorld();
        Own.box2d.factory.setWorld(physicsWorld);

        bodyContact = new BodyContact();
        bodyContact.setContactListener(contactListenerSystem);
        physicsWorld.setContactListener(bodyContact);

        JsonValue levelData = new JsonReader().parse(Gdx.files.internal("json/leveldata.json")).get("4");
        // create the ball
        ball = new Ball(this.gameController.spriteBatch, levelData.get("ball"));
        body = ball.getBody();
        Transform transform = transformCm.get(idManager.get("ballPosition"));
        body.setTransform(transform.getX(), transform.getY(), body.getAngle());
        state = GameData.RUNNING;
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    protected void processSystem() {
        if (state == GameData.RUNNING) {
            if (!physicsSystem.isEnabled()) physicsSystem.setEnabled(true);
            limitVelocity();
            handleInput();

            if (isLevelEnd()) {
                controlsSystem.setState(GameData.LEVEL_END);
            }
        } else {
            physicsSystem.setEnabled(false);
        }
        drawBall();
    }

    private void limitVelocity() {
        float angularVelocity = body.getAngularVelocity();

        if (angularVelocity > TOP_ANGULAR_VELOCITY) body.setAngularVelocity(TOP_ANGULAR_VELOCITY);
        if (angularVelocity < -TOP_ANGULAR_VELOCITY) body.setAngularVelocity(-TOP_ANGULAR_VELOCITY);

        // limit horizontal velocity
        Vector2 velocity = body.getLinearVelocity();
        // moving right
        if (velocity.x > TOP_LINEAR_VELOCITY) body.setLinearVelocity(TOP_LINEAR_VELOCITY, velocity.y);
        // moving left
        if (velocity.x < -TOP_LINEAR_VELOCITY) body.setLinearVelocity(-TOP_LINEAR_VELOCITY, velocity.y);

        // limit vertical velocity
        // moving up
        if (velocity.y > TOP_LINEAR_VELOCITY) body.setLinearVelocity(velocity.x, TOP_LINEAR_VELOCITY);
        // moving down
        if (velocity.y < -TOP_LINEAR_VELOCITY) body.setLinearVelocity(velocity.x, -TOP_LINEAR_VELOCITY);
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





































