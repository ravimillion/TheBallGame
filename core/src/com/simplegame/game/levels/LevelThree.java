package com.simplegame.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.simplegame.game.objects.Ball;
import com.simplegame.game.objects.FireBall;
import com.simplegame.game.objects.WorldBoundry;
import com.simplegame.game.screens.GameEntry;
import com.simplegame.game.userdata.UserData;
import java.util.HashMap;
import ownLib.BodyContact;
import ownLib.Own;

public class LevelThree extends LevelScreen{
    private String TAG = "LevelThree";
    private Ball ball;
    private Array<FireBall> fireballsArray;
    private BodyContact bodyContact = null;
    private boolean isTouchAndHold = false;
    private float ballPosMaxX;



    public LevelThree(GameEntry gameEntry) {
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
    public void contactListener(final UserData userDataA, UserData userDataB, float normalImpulse) {

        if (userDataA.getId().startsWith("fireball") && userDataB.getId().equals("bottom") ||
                userDataB.getId().startsWith("fireball") && userDataA.getId().equals("bottom")) {
            Own.log(TAG, userDataA.getId() + " + " + userDataB.getId());
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    for (FireBall fireBall: fireballsArray) {
                        if (fireBall.getId().equals(userDataA.getId())) {
                            fireBall.setPosition(new Vector2(fireBall.getPosition().x, WORLD_HEIGHT - 2));
                        }
                    }

                }
            });
        }
//
//        if (normalImpulse > 500) {
//            GL20 gl = Gdx.gl;
//            gl.glClearColor(1, 0, 0, 1);
//            gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        }
//
//        if (userDataA.getId().equals("ball") && userDataB.getId().equals("right") || userDataA.getId().equals("right") && userDataB.getId().equals("ball")) {
//            Gdx.app.postRunnable(new Runnable() {
//                @Override
//                public void run() {
//                    game.setScreen(new MainMenuScreen(game));
//                }
//            });
//        }
    }

    private JsonValue loadLevelData() {
        JsonReader jsonReader = new JsonReader();
        JsonValue store = jsonReader.parse(Gdx.files.internal("json/leveldata.json"));
        JsonValue levelData = store.get("3");
        return levelData;
    }

    @Override
    protected void setupLevel() {
        bodyContact = new BodyContact();
        bodyContact.setContactListener(this);

        world = new World(new Vector2(gravityX, gravityY), true);
        world.setContactListener(bodyContact);

        debugRenderer = new Box2DDebugRenderer();
        JsonValue levelData = loadLevelData();
        createBall(levelData);
        createFireballs(levelData);

        drawBorder();
        Own.io.setOnTouchListener(this);
    }

    private void createFireballs(JsonValue levelData) {
        JsonValue fireballs = levelData.get("fireballs");
        fireballsArray = new Array<FireBall>();
        for (int i = 0; i < fireballs.size; i++) {
            FireBall fireBall = new FireBall(world, game, fireballs.get(i), box2DCam);

            fireBall.setId("fireball");
            fireBall.setRadius(fireballs.get(i).getInt("radius"));
            fireBall.create();
            fireBall.setPosition(new Vector2(fireballs.get(i).getFloat("x"), fireballs.get(i).getFloat("y")));
            fireBall.setDamping(5f);
            fireballsArray.add(fireBall);
        }
    }

    private void createBall(JsonValue levelData) {
        ball = new Ball(world, game, levelData.get("ball"));
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
//        game.batch.draw(Own.assets.getTexture("BGL3"), box2DCam.position.x - box2DCam.viewportWidth / 2, 0, WORLD_WIDTH / 10, WORLD_HEIGHT + 1);

        for (int i = 0; i < WORLD_WIDTH; i += box2DCam.viewportWidth) {
            game.batch.draw(Own.assets.getTexture("GROUND"), i, 0, WORLD_WIDTH / 10, 4.3f);
        }

        if (ball != null) ball.drawGui();
        game.batch.end();

        game.batch.setProjectionMatrix(orthoCam.combined);
        game.batch.begin();
        Own.text.draw(game.batch, "Score: 00000", new Vector2(10, ORTHO_HEIGHT - 20), Own.text.SCORE);
        for (FireBall fireBall: fireballsArray) {
            fireBall.drawGui();
        }


        orthoCam.update();
        game.batch.end();

    }

    public void drawBox2DWorld() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0, 0, 0, 1f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        updateCamera();

        ball.update(Gdx.graphics.getDeltaTime());

        for (FireBall fireBall: fireballsArray) {
            if (fireBall.getPosition().y < 4) {
                fireBall.setPosition(new Vector2(fireBall.getPosition().x, WORLD_HEIGHT - fireBall.getRadius()));
            }
            Own.box2d.gui.putOffScreen(fireBall, box2DCam);
        }

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
        Vector3 touchPoint = box2DCam.unproject(new Vector3(new Vector2(Gdx.input.getX(), Gdx.input.getY()), 0));
        if (Gdx.input.justTouched()) {
            ball.handleInput(touchPoint);
        }

        if (isTouchAndHold) {
            if (touchPoint.x > ball.getPosition().x) {
                ball.getBody().applyTorque(5, true);
            } else {
                ball.getBody().applyTorque(-5, true);
            }
        }
    }

    @Override
    public void render(float delta) {
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

        isTouchAndHold = true;
    }

    @Override
    public void touchUp(int screenX, int screenY, int pointer) {
        isTouchAndHold = false;
    }

    @Override
    public void touchDragged(int screenX, int screenY, int pointer) {

    }

    @Override
    public void touchHold(int screenX, int screenY, int pointer) {

    }
}
