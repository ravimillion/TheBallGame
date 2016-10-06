package com.simplegame.game.levels;

import com.badlogic.gdx.Gdx;
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

public class LevelOne extends LevelScreen {
    private String TAG = "LevelOne";
    private BodyContact bodyContact = null;
    private ArrayList<Stone> obstacleArray = null;
    private String msg1 = null;
    private String msg2 = null;
    private String msg3 = null;

    private Ball ball = null;
    private TreeStump treeStump = null;

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

    private void gameOver() {
        isGameOver = true;
    }

    @Override
    public void contactListener(UserData userDataA, UserData userDataB, float normalImpulse) {
        if (normalImpulse > 100) Own.log(TAG, "Impulse: " + normalImpulse);
        if (normalImpulse > 450) {
            gameOver();
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
        debugRenderer = new Box2DDebugRenderer();

        HashMap<String, JsonValue> levelObjects = loadLevelData();
        createBall(levelObjects);
        createStones(levelObjects);
        createTreeStump(levelObjects);
        drawBorder();

//        int numOfPoints = 100;
//        Vector2[] vectorPoints = new Vector2[numOfPoints];
//        int widthFactor = (int) WORLD_WIDTH / numOfPoints;
//        for (int i = 0; i < numOfPoints; i++) {
//            if (i == 0 || i == numOfPoints - 1) {
//                vectorPoints[i] = new Vector2(i * widthFactor, 0);
//                continue;
//            }
//            vectorPoints[i] = new Vector2(i * widthFactor, Own.rand.nextInt(10));
//        }
//        Own.factory.getChainBody(BodyType.StaticBody, vectorPoints, radius * 4, radius, 0, 1.0f, 1.0f, 0, "Platform");

//        Vector2[] vertices = new Vector2[5];
//        vertices[0] = new Vector2(2f, 2f);
//        vertices[1] = new Vector2(6f, 4f);
//        vertices[2] = new Vector2(4f, 5f);
//        vertices[3] = new Vector2(2f, 8f);
//        vertices[4] = new Vector2(0f, 10f);

        Own.bodyContact.setContactListener(this);
        Own.io.setOnTouchListener(this);
    }

    @Override
    protected void levelEnd() {

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

    private void drawBox2DGui() {
        game.batch.setProjectionMatrix(box2DCam.combined);
        game.batch.begin();
        game.batch.draw(Own.assets.getTexture("BGL1"), box2DCam.position.x - box2DCam.viewportWidth / 2, 0, WORLD_WIDTH / 10, WORLD_HEIGHT + 1);

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

        // Ortho
        game.batch.setProjectionMatrix(orthoCam.combined);
        game.batch.begin();
        Own.text.draw(game.batch, msg1, new Vector2(0, ORTHO_HEIGHT - 20), Own.text.SCORE);
        Own.text.draw(game.batch, msg2, new Vector2(500, ORTHO_HEIGHT - 20), Own.text.SCORE);
        Own.text.draw(game.batch, msg3, new Vector2(1000, ORTHO_HEIGHT - 20), Own.text.SCORE);
        orthoCam.update();
        game.batch.end();
    }

    public void drawBox2DWorld() {
        if (isGameOver) return;

        try {
            Vector3 values = Own.device.getAccMeter();
            gravityX = (values.y * 10);
//            gravityY = -values.x * 10;

            Vector2 gX = new Vector2(gravityX, 0);
            Vector2 gY = new Vector2(gravityY, 0);


            msg1 = "x: " + gravityX;
            msg2 = " y: " + gravityY;
            msg3 = " XX: " + (gravityY + gravityX);
//            gravityY = -values.x * 10;
//            float velocity = ball.getVelocity().len();
//            if (velocity > 15000000) {
//                gl.glClearColor(1, 0, 0, 1);
//                gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//            }

//            if (gravityY > -9.8) gravityY = world.getGravity().y;
            world.setGravity(new Vector2(gravityX, gravityY + gravityX));
        } catch (OwnException e) {
            e.printStackTrace();
        }

//        follow the camera
        if (ball.getPosition().x > box2DCam.viewportWidth / 2 && ball.getPosition().x < WORLD_WIDTH - box2DCam.viewportWidth / 2) {
            worldBoundry.updateWorldBoundry(WorldBoundry.LEFT, new Vector2(ball.getPosition().x - box2DCam.viewportWidth / 2, 0), 0);
            box2DCam.position.set(ball.getPosition().x, box2DCam.viewportHeight / 2 + 1, 0);
            box2DCam.update();
        }

//      advance the world
        world.step(Gdx.graphics.getDeltaTime(), 8, 2);
//         debug renderer
        debugRenderer.render(world, box2DCam.combined);
    }

    @Override
    public void render(float delta) {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 0, 1f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawBox2DWorld();
        drawBox2DGui();
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
    public void touchDown(int screenX, int screenY, int pointer) {
        ball.getBody().setLinearVelocity(new Vector2(0, 30f));
    }

    @Override
    public void touchUp(int screenX, int screenY, int pointer) {

    }

    @Override
    public void touchDragged(int screenX, int screenY, int pointer) {

    }
}
