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
import com.simplegame.game.components.Timeout;

import ownLib.Own;

public class PlayerSystem extends BaseSystem implements AfterSceneInit {
    public Body body;
    public int state = GameData.RUNNING;
    // ball parameters
    float radius = 2f, restitution = 0.3f, density = 1f, friction = 2.5f;
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
    private GameSaverSystem gameSaverSystem;
    private PhysicsBodyContactSystem physicsBodyContactSystem;

    // variable cache
    private Entity ballEntity;
    private Transform transform;
    private Origin origin;
    private World physicsWorld;

    private int BALL_FORCE = 1500;
    private float TOP_LIN_VELOCITY = 35f;
    private float TOP_ANG_VELOCITY = 20f;

    public PlayerSystem(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void afterSceneInit() {
        physicsWorld = physicsSystem.getPhysicsWorld();
        physicsWorld.setContactListener(physicsBodyContactSystem);

        // get the ball sprite info and hide the entity
        ballEntity = idManager.get("ball");

        origin = originCm.get(this.ballEntity);
        float scaleFactor = (2.0f * radius) / (2 * origin.getOriginX());

        transform = transformCm.get(ballEntity);
        transform.setScale(scaleFactor, scaleFactor);

        // create ball
        Own.box2d.factory.setWorld(physicsWorld);
        body = Own.box2d.factory.getCircleBody(BodyDef.BodyType.DynamicBody, loadPlayerPosition(), 0, radius, density, friction, restitution, "ball");
        body.setUserData("ball");


        state = GameData.RUNNING;
        gameSaverSystem.updateLevelStatus(gameController.CURRENT_LEVEL, GameData.LEVEL_IN_PROGRESS);
    }

    public Vector2 loadPlayerPosition() {
        // load game state
        Vector2 position = new Vector2();

        if (gameSaverSystem.isStateFound() == false || gameSaverSystem.getStatusForLevel(gameController.CURRENT_LEVEL) == GameData.LEVEL_NOT_YET_PLAYED) {
            Own.log("No game state found, falling back to default");
            position.set(transform.getX(), transform.getY());
        } else {
            Own.log("Game state found, loading from saved state for level");
            position.set(gameSaverSystem.getPlayerPosition().x, GameData.WORLD_HEIGHT - radius + 1);
        }

        return position;
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
            saveGame();

            if (isLevelEnd()) {
                gameSaverSystem.updateLevelStatus(gameController.CURRENT_LEVEL, GameData.LEVEL_ALREADY_FINISHED);
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
        return body.getPosition().x > GameData.WORLD_WIDTH - 15;
    }

    private void drawBall() {
        transform.setPosition(body.getPosition().x - origin.getOriginX(), body.getPosition().y - origin.getOriginY());
        transform.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
    }

    private void saveGame() {
        int x = (int) body.getPosition().x;
        if (x % 20 == 0) {
            gameSaverSystem.saveGame();
        }
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

    public void changeBallSize(int size) {
        body.getFixtureList().get(0).getShape().setRadius(size);
        transform.setScale(size, size);
    }

    public void applyPower(String powerID) {
        switch (powerID) {
            case "idPowerSize":
                changeBallSize(5);
                // stop power
                new Timeout(new Runnable() {
                    @Override
                    public void run() {
                        changeBallSize(2);
                    }
                }, 10000);
                break;
            case "idPowerBounce":
                Own.log("Apply bounce");
//                body.getFixtureList().get(0).setRestitution(0.8f);
                // stop power
//                new Timeout(new Runnable() {
//                    @Override
//                    public void run() {
//                        Own.log("Removing power");
//                        body.getFixtureList().get(0).setRestitution(0.3f);
//                    }
//                }, 10000);
                break;
            case "idPowerGravity":
                Own.log("Apply Gravity");
                final Vector2 gravity = physicsWorld.getGravity();
                physicsWorld.setGravity(new Vector2(0, gravity.y / 4));
                // stop power
                new Timeout(new Runnable() {
                    @Override
                    public void run() {
                        physicsWorld.setGravity(gravity);
                    }
                }, 10000);
                break;
        }
    }
}





































