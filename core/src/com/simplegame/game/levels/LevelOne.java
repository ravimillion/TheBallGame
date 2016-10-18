package com.simplegame.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.simplegame.game.MainMenuScreen;
import com.simplegame.game.objects.Ball;
import com.simplegame.game.objects.Stone;
import com.simplegame.game.objects.TreeStump;
import com.simplegame.game.objects.WorldBoundry;
import com.simplegame.game.screens.GameEntry;
import com.simplegame.game.userdata.UserData;

import java.util.ArrayList;
import java.util.HashMap;

import ownLib.BodyContact;
import ownLib.Own;
import ownLib.OwnException;
import ownLib.controls.ControlsLayer;

public class LevelOne extends LevelScreen implements InputProcessor {
    private String TAG = "LevelOne";
    private BodyContact bodyContact;
    private ArrayList<Stone> obstacleArray;

    private String msg1;
    private String msg2;
    private String msg3;

    private Ball ball;
    private TreeStump treeStump;
    private ControlsLayer controlsLayer;

    private float ballPosMaxX;

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
        int scaleFactor = 10;
        WORLD_WIDTH = 480f;
        WORLD_HEIGHT = Own.device.getScreenRatio() * WORLD_WIDTH / scaleFactor;

        orthoCam = new OrthographicCamera(ORTHO_WIDTH, ORTHO_HEIGHT);
        orthoCam.position.set(orthoCam.viewportWidth / 2, orthoCam.viewportHeight / 2, 0);
        orthoCam.update();

        box2DCam = new OrthographicCamera(WORLD_WIDTH / scaleFactor, WORLD_HEIGHT);
        box2DCam.position.set(box2DCam.viewportWidth / 2, box2DCam.viewportHeight / 2 + 1, 0);
        box2DCam.update();
    }


    @Override
    public void contactListener(UserData userDataA, UserData userDataB, float normalImpulse) {
        if (normalImpulse > 100) Own.log(TAG, "Impulse: " + normalImpulse);
        if (normalImpulse > 450) {
            setGameState(GameState.GAME_OVER);
        }

        if (userDataA.getId().equals("ball") && userDataB.getId().equals("right")
                || userDataA.getId().equals("right") && userDataB.getId().equals("ball")) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new MainMenuScreen(game));
                }
            });
        }
    }

    private HashMap<String, JsonValue> loadLevelData() {
        HashMap<String, JsonValue> levelObjects = new HashMap<String, JsonValue>();

        JsonReader jsonReader = new JsonReader();
        JsonValue store = jsonReader.parse(Gdx.files.internal("json/leveldata.json"));

        JsonValue levelData = store.get("1");
        levelObjects.put("ball", levelData.get("ball"));
        levelObjects.put("stone", levelData.get("stone"));
        levelObjects.put("treestump", levelData.get("treestump"));

        return levelObjects;
    }

    @Override
    protected void setupLevel() {
        bodyContact = new BodyContact();
        bodyContact.setContactListener(this);

        world = new World(new Vector2(gravityX, gravityY), true);
        world.setContactListener(bodyContact);

        controlsLayer = new ControlsLayer(game.batch, this);
        debugRenderer = new Box2DDebugRenderer();

        HashMap<String, JsonValue> levelObjects = loadLevelData();
        createBall(levelObjects);
        createStones(levelObjects);
        createTreeStump(levelObjects);
        drawBorder();

        Own.bodyContact.setContactListener(this);
        Own.io.addProcessor(this);
    }

    @Override
    protected void renderLevel() {
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(box2DCam.combined);
        game.batch.begin();
//        game.batch.draw(Own.assets.getTexture("BGL1"), box2DCam.position.x - box2DCam.viewportWidth / 2, 0, WORLD_WIDTH / 10, WORLD_HEIGHT + 1);

//        send update to stones
        for (int i = 0; i < obstacleArray.size(); i++) {
            obstacleArray.get(i).drawGui();
        }

        if (treeStump != null) treeStump.drawGui();
        if (ball != null) ball.drawGui();

//        update the ground
        for (int i = 0; i < WORLD_WIDTH; i += box2DCam.viewportWidth) {
            game.batch.draw(Own.assets.getTexture("GROUND"), i, 0, WORLD_WIDTH / 10, 4.3f);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(orthoCam.combined);
        controlsLayer.draw(Gdx.graphics.getDeltaTime());

        game.batch.begin();
        Own.text.draw(game.batch, "Score: 0000", new Vector2(0, ORTHO_HEIGHT - 20), Own.text.SCORE);
        orthoCam.update();
        game.batch.end();
    }

    @Override
    protected void updateWorld() {
        switch (getGameState()) {
            case READY:
                Own.log(TAG, "Game ready");
                break;
            case RUNNING:
                followCamera();

                try {
                    Vector3 values = Own.device.getAccMeter();
                    gravityX = (values.y * 10);
                    msg1 = "x: " + gravityX;
                    msg2 = " y: " + gravityY;
                    msg3 = " XX: " + (gravityY + gravityX);

                    world.setGravity(new Vector2(gravityX, gravityY + gravityX));
                } catch (OwnException e) {
                    e.printStackTrace();
                }

                world.step(Gdx.graphics.getDeltaTime(), 8, 2);
                debugRenderer.render(world, box2DCam.combined);
                break;
            case GAME_OVER:
                if (Gdx.input.justTouched()) {
                    game.setScreen(new MainMenuScreen(game));
                }
                break;
            case PAUSED:
                Own.log(TAG, "Game paused");
                break;
            case LEVEL_END:
                game.setScreen(new MainMenuScreen(game));
                break;
            default:
                break;
        }
    }

    private void followCamera() {
        if (ball.getPosition().x > box2DCam.viewportWidth / 2 &&
                ball.getPosition().x < WORLD_WIDTH - box2DCam.viewportWidth / 2 &&
                ball.getPosition().x > ballPosMaxX) {
            ballPosMaxX = ball.getPosition().x;
            worldBoundry.updateWorldBoundry(WorldBoundry.LEFT, new Vector2(ballPosMaxX - box2DCam.viewportWidth / 2, 0), 0);
            box2DCam.position.set(ball.getPosition().x, box2DCam.viewportHeight / 2 + 1, 0);
            box2DCam.update();
        }
    }

    private void createTreeStump(HashMap<String, JsonValue> levelObjects) {
        treeStump = new TreeStump(world, game, levelObjects.get("treestump"));
        treeStump.create();
    }

    private void createBall(HashMap<String, JsonValue> levelObjects) {
        ball = new Ball(world, game, levelObjects.get("ball"));
        ball.create();
    }

    private void createStones(HashMap<String, JsonValue> levelObjects) {
        float[] angles = {-0.2f, -0.3f, -0.4f, -0.5f};

        obstacleArray = new ArrayList<Stone>();
        Stone stone = null;
        for (int i = 2; i < WORLD_WIDTH / 15; i++) {
            stone = new Stone(world, game, levelObjects.get("stone"));
            stone.setAngle(angles[Own.rand.nextInt(4)]);
            stone.setPosition(new Vector2((i * 15) + 25, 5f));
            if (i % 2 == 0) {
                stone.setSize(5f, 5f);
            } else {
                stone.setSize(3f, 3f);
            }
            stone.create();
            obstacleArray.add(stone);
        }
    }

    @Override
    public void show() {
    }

    public void handleInput() {
        if (Gdx.input.justTouched()) {
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
        Own.log(TAG, "Disposing levelOne");
        world.dispose();
        debugRenderer.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
}
