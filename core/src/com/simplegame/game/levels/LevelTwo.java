package com.simplegame.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.simplegame.game.MainMenuScreen;
import com.simplegame.game.objects.Ball;
import com.simplegame.game.objects.Balloon;
import com.simplegame.game.objects.Icicle;
import com.simplegame.game.objects.Stone;
import com.simplegame.game.objects.WorldBoundry;
import com.simplegame.game.screens.GameEntry;
import com.simplegame.game.userdata.UserData;

import java.util.ArrayList;
import java.util.HashMap;

import ownLib.BodyContact;
import ownLib.Own;

public class LevelTwo extends LevelScreen {
    private String TAG = "LevelTwo";
    private Balloon balloon;
    private Ball ball;
    private Icicle icicle;
    private Icicle icicle2;

    private ArrayList<Stone> obstacleArray = null;
    private BitmapFont font;

    private BodyContact bodyContact = null;
    private float ballPosMaxX;

    public LevelTwo(GameEntry gameEntry) {
        this.game = gameEntry;

        gravityX = 0;
        gravityY = -98f;
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
        String idA = userDataA.getId();
        String idB = userDataB.getId();

//        level complete
        if (idA.equals("ball") && idB.equals("right") || idA.equals("right") && idB.equals("ball")) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    levelComplete();
                }
            });
        }
//        game over
        if (idA.equals("balloon") && idB.equals("icicle") || idA.equals("icicle") && idB.equals("balloon")) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    gameOver();
                }
            });
        }
    }

    private HashMap<String, JsonValue> loadLevelData() {
        HashMap<String, JsonValue> levelObjects = new HashMap<String, JsonValue>();

        JsonReader jsonReader = new JsonReader();
        JsonValue store = jsonReader.parse(Gdx.files.internal("json/leveldata.json"));

        JsonValue levelData = store.get("2");
        levelObjects.put("ball", levelData.get("ball"));
        levelObjects.put("balloon", levelData.get("balloon"));
        levelObjects.put("icicle", levelData.get("icicle"));
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
        createBalloon(levelObjects);
        createIcicle(levelObjects.get("icicle"));

//        createPrismaticJoint();
        createRopeJoint();
        drawBorder();
        Own.io.setOnTouchListener(this);
    }

    @Override
    protected void levelComplete() {
        game.setScreen(new MainMenuScreen(game));
    }

    private void gameOver() {
        isGameOver = true;
    }

    private void createIcicle(JsonValue objectData) {
        icicle = new Icicle(world, game, objectData);
        icicle.create();
        icicle.setPosition(new Vector2(10f, WORLD_HEIGHT - 3f));

        icicle2 = new Icicle(world, game, objectData);
        icicle2.create();
        icicle2.setPosition(new Vector2(box2DCam.viewportWidth / 2 + 10, WORLD_HEIGHT - 3f));
    }

    private void createRopeJoint() {
        float linkLength = 1f;
        int linkCount = 3;
        float gap = 1f;
        Array<Body> chainLinks = Own.box2d.factory.createRopeJoint(.1f, linkLength, gap, linkCount);

        RopeJointDef ropeJointDef = new RopeJointDef();
        ropeJointDef.bodyA = balloon.getBody();
        ropeJointDef.bodyB = chainLinks.get(0);
        ropeJointDef.collideConnected = false;
        ropeJointDef.maxLength = gap;
        ropeJointDef.localAnchorA.set(balloon.getWidth() / 2, 0);
        ropeJointDef.localAnchorB.set(0, linkLength);
        world.createJoint(ropeJointDef);

        ropeJointDef = new RopeJointDef();
        ropeJointDef.bodyA = chainLinks.get(chainLinks.size - 1);
        ropeJointDef.bodyB = ball.getBody();
        ropeJointDef.collideConnected = false;
        ropeJointDef.maxLength = gap;
        ropeJointDef.localAnchorA.set(0, -1 * linkLength);
        ropeJointDef.localAnchorB.set(0, 0);
        world.createJoint(ropeJointDef);
    }

    private void createPrismaticJoint() {
        PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
        prismaticJointDef.bodyA = ball.getBody();
        prismaticJointDef.bodyB = balloon.getBody();
        prismaticJointDef.enableLimit = true;
        prismaticJointDef.collideConnected = false;
        prismaticJointDef.upperTranslation = 5;
        world.createJoint(prismaticJointDef);
    }

    private void createBalloon(HashMap<String, JsonValue> levelObjects) {
        balloon = new Balloon(world, game, levelObjects.get("balloon"));
        balloon.create();
        balloon.setPosition(new Vector2(10f, 12f));
    }

    private void createBall(HashMap<String, JsonValue> levelObjects) {
        ball = new Ball(world, game, levelObjects.get("ball"));
        ball.create();
        ball.setPosition(new Vector2(5f, 10f));
        ball.setDamping(1f);
    }

    @Override
    public void show() {
    }

    private void drawBox2DGUI() {
        game.batch.setProjectionMatrix(box2DCam.combined);
        game.batch.begin();
        game.batch.draw(Own.assets.getTexture("BGL2"), box2DCam.position.x - box2DCam.viewportWidth / 2, 0, WORLD_WIDTH / 10, WORLD_HEIGHT + 1);

        for (int i = 0; i < WORLD_WIDTH; i += box2DCam.viewportWidth) {
            game.batch.draw(Own.assets.getTexture("GROUND"), i, 0, WORLD_WIDTH / 10, 4.3f);
        }

        if (ball != null) ball.drawGui();
        if (balloon != null) balloon.drawGui();

        if (icicle != null) icicle.drawGui();
        if (icicle2 != null) icicle2.drawGui();

        game.batch.end();

        game.batch.setProjectionMatrix(orthoCam.combined);
        game.batch.begin();
        Own.text.draw(game.batch, "Score: 00000", new Vector2(10, ORTHO_HEIGHT - 20), Own.text.SCORE);
        orthoCam.update();
        game.batch.end();

    }

    public void drawBox2DWorld() {
        if (isGameOver) return;
        updateCamera();
        if (icicle != null) Own.box2d.gui.putOffScreen(icicle, box2DCam);
        if (icicle2 != null) Own.box2d.gui.putOffScreen(icicle2, box2DCam);

//      advance the world
        world.step(Gdx.graphics.getDeltaTime(), 8, 2);
//         debug renderer
        debugRenderer.render(world, box2DCam.combined);
    }

    private void updateCamera() {
        if (ball.getPosition().x > box2DCam.viewportWidth / 2 &&
                ball.getPosition().x < WORLD_WIDTH - box2DCam.viewportWidth / 2 &&
                ball.getPosition().x > ballPosMaxX) {
            ballPosMaxX = ball.getPosition().x;
            worldBoundry.updateWorldBoundry(WorldBoundry.LEFT, new Vector2(ballPosMaxX - box2DCam.viewportWidth / 2, 0), 0);
            box2DCam.position.set(ball.getPosition().x, box2DCam.viewportHeight / 2 + 1, 0);
            box2DCam.update();
        }
    }


    public void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touchPoint = box2DCam.unproject(new Vector3(new Vector2(Gdx.input.getX(), Gdx.input.getY()), 0));
            ball.handleInput(touchPoint);
        }
    }

    @Override
    public void render(float delta) {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 0, 1f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();
        drawBox2DWorld();
        drawBox2DGUI();
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
        Own.log(TAG, "Disposing levelTwo");
        world.dispose();
        debugRenderer.dispose();
    }

    @Override
    public void touchDown(int screenX, int screenY, int pointer) {
        Own.box2d.gui.applyForceFromSource(200f, balloon.getBody(), box2DCam.unproject(new Vector3(screenX, screenY, 0)), true);
    }

    @Override
    public void touchUp(int screenX, int screenY, int pointer) {

    }

    @Override
    public void touchDragged(int screenX, int screenY, int pointer) {

    }
}
