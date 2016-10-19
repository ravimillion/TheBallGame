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
import com.simplegame.game.MainMenuScreen;
import com.simplegame.game.objects.Ball;
import com.simplegame.game.objects.FireBall;
import com.simplegame.game.objects.WorldBoundry;
import com.simplegame.game.screens.GameEntry;
import com.simplegame.game.userdata.UserData;

import ownLib.BodyContact;
import ownLib.Own;
import ownLib.controls.ControlsLayer;

import static com.simplegame.game.levels.GameState.GAME_OVER;
import static com.simplegame.game.levels.GameState.LEVEL_END;

public class LevelThree extends LevelScreen {
    private String TAG = "LevelThree";

    private Ball ball;
    private Array<FireBall> fireballsArray;
    private BodyContact bodyContact = null;

    private float ballPosMaxX;
    private boolean isTouchAndHold = false;

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
    protected void setupLevel() {
        bodyContact = new BodyContact();
        bodyContact.setContactListener(this);

        world = new World(new Vector2(gravityX, gravityY), true);
        world.setContactListener(bodyContact);

        controlsLayer = new ControlsLayer(game.batch, this);
        debugRenderer = new Box2DDebugRenderer();

        // Load json level data
        JsonReader jsonReader = new JsonReader();
        JsonValue store = jsonReader.parse(Gdx.files.internal("json/leveldata.json"));
        JsonValue levelData = store.get("3");

        // create objects
        createBall(levelData);
        createFireballs(levelData);

        drawBorder();
        Own.io.addProcessor(this);

    }

    @Override
    public void contactListener(final UserData userDataA, UserData userDataB, float normalImpulse) {
        String idA = userDataA.getId();
        String idB = userDataB.getId();

        if (idA.startsWith("fireball") && idB.equals("bottom") || idB.startsWith("fireball") && idA.equals("bottom")) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    for (FireBall fireBall : fireballsArray) {
                        if (fireBall.getId().equals(userDataA.getId())) {
                            fireBall.setPosition(new Vector2(fireBall.getPosition().x, WORLD_HEIGHT - 2));
                        }
                    }
                }
            });
        }

        if (idA.startsWith("fireball") && idB.equals("ball") || idB.startsWith("fireball") && idA.equals("ball")) {
            Own.log(TAG, idA + " " + idB);
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    setGameState(GAME_OVER);
                }
            });
        }

        if (idA.startsWith("ball") && idB.equals("right") || idB.startsWith("ball") && idA.equals("right")) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    setGameState(LEVEL_END);
                }
            });
        }
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

    @Override
    protected void renderLevel() {
        GL20 gl = Gdx.gl;
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(box2DCam.combined);
        game.batch.begin();
//        game.batch.draw(Own.assets.getTexture("BGL3"), box2DCam.position.x - box2DCam.viewportWidth / 2, 0, WORLD_WIDTH / 10, WORLD_HEIGHT + 1);

        for (int i = 0; i < WORLD_WIDTH; i += box2DCam.viewportWidth) {
            game.batch.draw(Own.assets.getTexture("GROUND"), i, 0, WORLD_WIDTH / 10, 4.3f);
        }

        if (ball != null) ball.drawGui();
        game.batch.end();


        game.batch.setProjectionMatrix(orthoCam.combined);
        controlsLayer.draw(Gdx.graphics.getDeltaTime());
        game.batch.begin();
        Own.text.draw(game.batch, "Score: 00000", new Vector2(10, ORTHO_HEIGHT - 20), Own.text.SCORE);
        for (FireBall fireBall : fireballsArray) {
            fireBall.drawGui();
        }

        orthoCam.update();
        game.batch.end();
    }

    public void updateWorld() {
        switch (getGameState()) {
            case READY:
                Own.log(TAG, "Game ready");
                break;
            case RUNNING:
                followCamera();
                ball.update(Gdx.graphics.getDeltaTime());

                for (FireBall fireBall : fireballsArray) {
                    if (fireBall.getPosition().y < 4) {
                        fireBall.setPosition(new Vector2(fireBall.getPosition().x, WORLD_HEIGHT - fireBall.getRadius()));
                    }
                    Own.box2d.gui.putOffScreen(fireBall, box2DCam);
                }

                world.step(Gdx.graphics.getDeltaTime(), 8, 2);
                debugRenderer.render(world, box2DCam.combined);
                break;
            case GAME_OVER:
                this.dispose();
                game.setScreen(new MainMenuScreen(game));

                break;
            case PAUSED:
//                what to do when the game is paused
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
            if (Gdx.input.getX() > Own.device.getScreenWidth() / 2) {
                ball.getBody().applyTorque(5, true);
            } else {
                ball.getBody().applyTorque(-5, true);
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
        Own.log(TAG, "Disposing levelTwo");
        world.dispose();
        debugRenderer.dispose();
        controlsLayer.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        isTouchAndHold = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isTouchAndHold = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
}
