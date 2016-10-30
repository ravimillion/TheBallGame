package com.simplegame.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonValue;
import com.simplegame.game.MainMenuScreen;
import com.simplegame.game.objects.Ball;
import com.simplegame.game.objects.JsonGameObject;
import com.simplegame.game.objects.WorldBoundry;
import com.simplegame.game.screens.GameEntry;
import com.simplegame.game.userdata.UserData;

import ownLib.BodyContact;
import ownLib.Own;
import ownLib.controls.ControlsLayer;

public class LevelOne extends LevelScreen implements InputProcessor {
    private String TAG = "LevelOne";
    private BodyContact bodyContact;
    private float BALL_FORCE = 20;

    private Ball ball;
    private JsonGameObject treeStump;
    private JsonGameObject horiPlatform;
    private JsonGameObject vertPlatform;
    private JsonGameObject boxLeft;
    private JsonGameObject curveLeft;
    private JsonGameObject topWoodBox;
    private JsonGameObject woodBox;
    private ControlsLayer controlsLayer;
    private JsonValue levelData;

    private float ballPosMaxX;
    private boolean isTouchHold;

    public LevelOne(GameEntry gameEntry) {
        this.game = gameEntry;

        gravityX = 0;
        gravityY = -98.0f;

        setScreenResolution();
        setupLevel();
    }

    @Override
    protected void setScreenResolution() {
        ORTHO_WIDTH = Own.device.getScreenWidth();
        ORTHO_HEIGHT = Own.device.getScreenHeight();
        setCamera();
    }

    protected void setCamera() {
        WORLD_HEIGHT = 32f;
        WORLD_WIDTH = 480f;

        orthoCam = new OrthographicCamera(ORTHO_WIDTH, ORTHO_HEIGHT);
        orthoCam.position.set(orthoCam.viewportWidth / 2, orthoCam.viewportHeight / 2, 0);
        orthoCam.update();

        box2DCam = new OrthographicCamera(WORLD_HEIGHT/Own.device.getScreenRatio(), WORLD_HEIGHT);
        box2DCam.position.set(box2DCam.viewportWidth / 2, box2DCam.viewportHeight / 2 + 1, 0);
        box2DCam.update();
    }


    @Override
    public void contactListener(UserData userDataA, UserData userDataB, float normalImpulse) {
        if (normalImpulse > 450) {
            setGameState(GameState.GAME_OVER);
        } else {
//            Own.log(TAG, "Impulse: " + normalImpulse);
        }

        if (userDataA.getId().equals("ball") && userDataB.getId().equals("right")
                || userDataA.getId().equals("right") && userDataB.getId().equals("ball")) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    setGameState(GameState.LEVEL_END);
//                    dispose();
//                    game.setScreen(new MainMenuScreen(game));
                }
            });
        }
    }

    private void createGameObject() {
        levelData = store.get("1");
        BALL_FORCE = levelData.getFloat("force");
        ball = new Ball(world, game.batch, levelData.get("ball"));
        boxLeft = new JsonGameObject(world, game, levelData.get("boxleft"));
        horiPlatform= new JsonGameObject(world, game, levelData.get("horiplatform"));
        vertPlatform= new JsonGameObject(world, game, levelData.get("vertplatform"));
        curveLeft = new JsonGameObject(world, game, levelData.get("curveleft"));
        topWoodBox = new JsonGameObject(world, game, levelData.get("topwoodbox"));
        topWoodBox.getBody().setType(BodyDef.BodyType.DynamicBody);
        woodBox = new JsonGameObject(world, game, levelData.get("woodbox"));
        woodBox.getBody().setType(BodyDef.BodyType.DynamicBody);
        drawBorder();
    }

    @Override
    protected void setupLevel() {
        bodyContact = new BodyContact();
        bodyContact.setContactListener(this);

        world = new World(new Vector2(gravityX, gravityY), true);
        world.setContactListener(bodyContact);

        controlsLayer = new ControlsLayer(game.batch, this);
        debugRenderer = new Box2DDebugRenderer();

        Own.bodyContact.setContactListener(this);
        Own.io.addProcessor(this);

        createGameObject();
    }

    @Override
    protected void renderLevel() {
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(box2DCam.combined);
        game.batch.begin();
//        game.batch.draw(Own.assets.getTexture("BGL1"), box2DCam.position.x - box2DCam.viewportWidth / 2, 0, WORLD_WIDTH / 10, WORLD_HEIGHT + 1);

        if (treeStump != null) treeStump.drawGui();
        if (boxLeft != null) boxLeft.drawGui();
        if (curveLeft != null) curveLeft.drawGui();
        if (topWoodBox != null) topWoodBox.drawGui();
        if (woodBox != null) woodBox.drawGui();
        if (horiPlatform != null) horiPlatform.drawGui();
        if (vertPlatform != null) vertPlatform.drawGui();
        drawGround(levelData.getString("ground").toUpperCase());
        if (ball != null) ball.drawGui();

        game.batch.end();

        game.batch.setProjectionMatrix(orthoCam.combined);
        game.batch.begin();
        Own.text.draw(game.batch, "Score: 0000", new Vector2(0, ORTHO_HEIGHT - 20), Own.text.SCORE);
        orthoCam.update();
        game.batch.end();

        controlsLayer.draw(Gdx.graphics.getDeltaTime());
    }

    @Override
    protected void updateWorld() {
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (getGameState()) {
            case READY:
                break;
            case RUNNING:
                followCamera();
                world.step(Gdx.graphics.getDeltaTime(), 8, 2);
                debugRenderer.render(world, box2DCam.combined);
                break;
            case GAME_OVER:
                this.dispose();
                game.setScreen(new MainMenuScreen(game));
                break;
            case PAUSED:
                break;
            case LEVEL_END:
                this.dispose();
                game.setScreen(new MainMenuScreen(game));
                break;
            default:
                break;
        }
    }

    private void followCamera() {
//        box2DCam.position.set(ball.getPosition().x, box2DCam.viewportHeight / 2 + 1, 0);
//        box2DCam.update();
        if (ball.getPosition().x > box2DCam.viewportWidth / 2 &&
                ball.getPosition().x < WORLD_WIDTH - box2DCam.viewportWidth / 2 &&
                ball.getPosition().x > ballPosMaxX) {
            ballPosMaxX = ball.getPosition().x;
//            worldBoundry.updateWorldBoundry(WorldBoundry.LEFT, new Vector2(ballPosMaxX - box2DCam.viewportWidth / 2, 0), 0);
            box2DCam.position.set(ball.getPosition().x, box2DCam.viewportHeight / 2 + 1, 0);
            box2DCam.update();
        }
    }

    @Override
    public void show() {
    }

    public void handleInput() {
        if (Gdx.input.justTouched()) {
        }

        if (isTouchHold) {
            if (Gdx.input.getX() > Own.device.getScreenWidth() / 2) {
                ball.getBody().applyTorque(BALL_FORCE * -1, true);
            } else {
                ball.getBody().applyTorque(BALL_FORCE, true);
            }
        }
    }

    @Override
    public void render(float delta) {
        handleInput();
        updateWorld();
        renderLevel();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        Own.log("Disposing LevelScreen");
        world.dispose();
        debugRenderer.dispose();
        controlsLayer.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        isTouchHold = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isTouchHold = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
}
