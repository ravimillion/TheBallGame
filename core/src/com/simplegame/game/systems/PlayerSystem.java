package com.simplegame.game.systems;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.kotcrab.vis.runtime.component.Origin;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisID;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;
import com.simplegame.game.GameController;
import com.simplegame.game.GameData;

import ownLib.Own;

public class PlayerSystem extends BaseSystem implements AfterSceneInit {
    public Body body;
    public int state = GameData.RUNNING;
    // ball parameters
    float radius = 2f, restitution = 0.5f, density = 1f, friction = 2.5f;
    private ComponentMapper<VisID> visIDCm;
    private ComponentMapper<PhysicsBody> physicsCm;
    private ComponentMapper<Transform> transformCm;
    private ComponentMapper<VisSprite> visSpriteCm;
    private ComponentMapper<Origin> originCm;
    private VisIDManager idManager;
    private CameraManager cameraManager;
    private PhysicsSystem physicsSystem;
    private ControlsSystem controlsSystem;
    private GameController gameController;
    private PhysicsBodyContactSystem physicsBodyContactSystem;

    // variable cache
    private Entity ballEntity;
    private Transform transform;
    private Origin origin;

    private int BALL_FORCE = 1500;
    private  float TOP_LIN_VELOCITY = 50f;
    private float TOP_ANG_VELOCITY = 25f;

    public PlayerSystem(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void afterSceneInit() {
        World physicsWorld = physicsSystem.getPhysicsWorld();
        physicsWorld.setContactListener(physicsBodyContactSystem);

        // get the ball sprite info and hide the entity
        ballEntity = idManager.get("ball");

        transform = transformCm.get(ballEntity);
        origin = originCm.get(this.ballEntity);
        float scaleFactor = (2.0f * radius) / (2 * origin.getOriginX());
        transform.setScale(scaleFactor, scaleFactor);
        // create ball
        Own.box2d.factory.setWorld(physicsWorld);
        body = Own.box2d.factory.getCircleBody(BodyDef.BodyType.DynamicBody, new Vector2(transform.getX(), transform.getY()), 0, radius, density, friction, restitution, "ball");
        body.setUserData("ball");

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

        if (angularVelocity > TOP_ANG_VELOCITY) body.setAngularVelocity(TOP_ANG_VELOCITY);
        if (angularVelocity < -TOP_ANG_VELOCITY) body.setAngularVelocity(-TOP_ANG_VELOCITY);

        // limit horizontal velocity
        Vector2 velocity = body.getLinearVelocity();
        // moving right
        if (velocity.x > TOP_LIN_VELOCITY) body.setLinearVelocity(TOP_LIN_VELOCITY, velocity.y);
        // moving left
        if (velocity.x < -TOP_LIN_VELOCITY) body.setLinearVelocity(-TOP_LIN_VELOCITY, velocity.y);

        // limit vertical velocity
        // moving up
        if (velocity.y > TOP_LIN_VELOCITY) body.setLinearVelocity(velocity.x, TOP_LIN_VELOCITY);
        // moving down
        if (velocity.y < -TOP_LIN_VELOCITY) body.setLinearVelocity(velocity.x, -TOP_LIN_VELOCITY);
    }

    private boolean isLevelEnd() {
        return body.getPosition().x > GameController.WORLD_WIDTH - 5;
    }

    private void drawBall() {
        transform.setPosition(body.getPosition().x - origin.getOriginX(), body.getPosition().y - origin.getOriginY());
        transform.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            if (Gdx.input.getX() > Own.device.getScreenWidth() / 2) {
                body.applyTorque(BALL_FORCE * -1, true);
            } else {
                body.applyTorque(BALL_FORCE, true);
            }
        }

        // for pc mode
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            body.applyTorque(BALL_FORCE * -1, true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            body.applyTorque(BALL_FORCE, true);
        }
    }
}





































