//package ownLib.controls;
//
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.scenes.scene2d.Event;
//import com.badlogic.gdx.scenes.scene2d.EventListener;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.InputListener;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Button;
//import com.badlogic.gdx.utils.viewport.ScreenViewport;
//import com.simplegame.game.GameData;
//import com.simplegame.game.levels.LevelScreen;
//
//import ownLib.Own;
//import ownLib.controls.ControlsFactory.ButtonType;
//
///**
// * Created by ravi on 13.10.16.
// */
//
//public class ControlsLayer {
//    private Stage stage;
//    private SpriteBatch gameBatch;
//    private LevelScreen levelScreen;
//    private OrthographicCamera orthoCam;
//    private ControlsFactory controlsFactory = new ControlsFactory();
//
//    private float buttonWidth = Own.device.getScreenWidth() * .10f;
//    private float buttonHeight = buttonWidth / 2;
//    private float screenWidth = Own.device.getScreenWidth();
//    private float screenHeight = Own.device.getScreenHeight();
//    private float centerX = Own.device.getScreenWidth() / 2;
//    private float centerY = Own.device.getScreenHeight() / 2;
//
//    private Button pauseButton;
//    private Button resumeButton;
//    private Button quitButton;
//    private Button readyButton;
//    private Button restartButton;
//
//    public ControlsLayer(SpriteBatch batch, LevelScreen levelScreen) {
//        this.levelScreen = levelScreen;
//        this.orthoCam = levelScreen.getOrthoCam();
//        this.gameBatch = batch;
//
//        stage = new Stage(new ScreenViewport(), batch);
//        stage.setDebugAll(true);
//        Own.io.addProcessor(stage);
//
//        createControls();
//        levelScreen.setGameState(GameData.READY);
//        setState(GameData.READY);
//    }
//
//    private void createControls() {
//        pauseButton = controlsFactory.getImageButton(ButtonType.PAUSE,
//                screenWidth - buttonWidth,
//                screenHeight - buttonHeight,
//                buttonWidth, buttonHeight);
//        pauseButton.addListener(new InputListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                return true;
//            }
//
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                levelScreen.setGameState(GameData.PAUSED);
//                setState(GameData.PAUSED);
//            }
//        });
//
//        resumeButton = controlsFactory.getImageButton(ButtonType.RESUME,
//                centerX - buttonWidth / 2,
//                centerY + buttonHeight * .20f,
//                buttonWidth,
//                buttonHeight);
//        resumeButton.addListener(new InputListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                return true;
//            }
//
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                levelScreen.setGameState(GameData.RUNNING);
//                setState(GameData.RUNNING);
//            }
//        });
//
//        quitButton = controlsFactory.getImageButton(ButtonType.QUIT,
//                centerX - buttonWidth / 2,
//                centerY - (buttonHeight + buttonHeight * .20f),
//                buttonWidth,
//                buttonHeight);
//        quitButton.addListener(new InputListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                return true;
//            }
//
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                levelScreen.setGameState(GameData.GAME_OVER);
//                setState(GameData.GAME_OVER);
//            }
//        });
//
//        readyButton = controlsFactory.getImageButton(ButtonType.READY,
//                centerX - buttonWidth / 2,
//                centerY - buttonHeight / 2,
//                buttonWidth,
//                buttonHeight);
//        readyButton.addListener(new InputListener() {
//            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                return true;
//            }
//
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                levelScreen.setGameState(GameData.RUNNING);
//                setState(GameData.RUNNING);
//            }
//        });
//
//        restartButton = controlsFactory.getImageButton(ButtonType.RESTART,
//                centerX - buttonWidth / 2,
//                centerY + buttonHeight * .40f,
//                buttonWidth,
//                buttonHeight);
//        restartButton.addListener(new EventListener() {
//            @Override
//            public boolean handle(Event event) {
//                levelScreen.setGameState(GameData.RUNNING);
//                setState(GameData.RUNNING);
//                return true;
//            }
//        });
//
//        stage.addActor(resumeButton);
//        stage.addActor(quitButton);
//        stage.addActor(pauseButton);
//        stage.addActor(readyButton);
//        stage.addActor(restartButton);
//    }
//
//    private void setAllHidden() {
//        pauseButton.setVisible(false);
//        readyButton.setVisible(false);
//        resumeButton.setVisible(false);
//        quitButton.setVisible(false);
//        restartButton.setVisible(false);
//    }
//
//    private void setState(GameData state) {
//        setAllHidden();
//
//        switch (state) {
//            case READY:
//                readyButton.setVisible(true);
//                break;
//            case RUNNING:
//                pauseButton.setVisible(true);
//                break;
//            case PAUSED:
//                resumeButton.setVisible(true);
//                quitButton.setVisible(true);
//                break;
//            case GAME_OVER:
//                restartButton.setVisible(true);
//                quitButton.setVisible(true);
//                break;
//        }
//    }
//
//    public void draw(float delta) {
//        setState(levelScreen.getGameState());
//        stage.act();
//        stage.draw();
//    }
//
//    public void dispose() {
//        stage.dispose();
//    }
//}
