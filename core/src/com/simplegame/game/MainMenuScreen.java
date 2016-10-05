package com.simplegame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.simplegame.game.levels.LevelOne;
import com.simplegame.game.levels.LevelThree;
import com.simplegame.game.levels.LevelTwo;
import com.simplegame.game.screens.GameScreen;

import java.util.ArrayList;

import ownLib.Own;

public class MainMenuScreen extends GameScreen {
    private String TAG = "MainMenuScreen";
    private com.simplegame.game.screens.GameEntry game;
    private Vector3 touchPoint;
    private OrthographicCamera guiCam;
    private ArrayList<Rectangle> levelGrid = null;
    private int tileWidth = 200;
    private int tileHeight = 200;

    public MainMenuScreen(com.simplegame.game.screens.GameEntry gameEntry) {
        this.game = gameEntry;
        setScreenResolution();
        calculateLevelGrid();
        touchPoint = new Vector3();
    }


    @Override
    public void setScreenResolution() {
        ORTHO_WIDTH = Own.device.getScreenWidth();
        ORTHO_HEIGHT = Own.device.getScreenHeight();
        Own.log(TAG, "ORTHO Dim: W: " + ORTHO_WIDTH + " H: " + ORTHO_HEIGHT);
        setCamera();
    }

    private ArrayList<Rectangle> calculateLevelGrid() {
        int numOfRow = 2;
        int numOfCol = 3;

        int horiCellSpacing = 50;
        int vertCellSpacing = 50;

        int paddingBottom = 100;
        int paddingLeft = 0;

        float gridWidth = (numOfCol * tileWidth) + (horiCellSpacing * (numOfCol - 1));
        float gridHeight = (numOfRow * tileHeight) + (vertCellSpacing * (numOfRow - 1));

        levelGrid = new ArrayList<Rectangle>();
        Rectangle tile = null;

        Own.log(TAG, "GridWidth: " + gridWidth + " GridX: " + (ORTHO_WIDTH - gridWidth) / 2);
        for (int row = numOfRow - 1; row >= 0; row--) {
            for (int col = 0; col < numOfCol; col++) {
                float tilePosInGridX = col * (tileWidth + horiCellSpacing);
                float tilePosInGridY = row * (tileHeight + vertCellSpacing);

                float gridPosX = (ORTHO_WIDTH - gridWidth) / 2;
                float gridPosY = (ORTHO_HEIGHT - gridHeight) / 2;

                tile = new Rectangle(paddingLeft + gridPosX + tilePosInGridX, paddingBottom + gridPosY + tilePosInGridY, tileWidth, tileHeight);
                levelGrid.add(tile);
            }
        }

        Own.log(TAG, "Size: " + levelGrid.size());
        return levelGrid;
    }

    public void setCamera() {
        guiCam = new OrthographicCamera(ORTHO_WIDTH, ORTHO_HEIGHT);
        guiCam.position.set(guiCam.viewportWidth / 2f, guiCam.viewportHeight / 2f, 0);
        guiCam.update();
    }

    @Override
    public void show() {
    }

    public void handleInput() {
        if (Gdx.input.justTouched()) {
            guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            for (int i = 0; i < levelGrid.size(); i++) {
                if (levelGrid.get(i).contains(touchPoint.x, touchPoint.y)) {
                    switch (i) {
                        case 0:
                            game.setScreen(new LevelOne(game));
                            break;
                        case 1:
                            game.setScreen(new LevelTwo(game));
                            break;
                        case 2:
                            game.setScreen(new LevelThree(game));
                            break;
//                        case 3:
//                            game.setScreen(new LevelFour(game));
//                            break;
//                        case 4:
//                            game.setScreen(new LevelFive(game));
//                            break;
//                        case 5:
//                            game.setScreen(new LevelSix(game));
//                            break;
                    }
                }
            }
            Own.log(TAG, "MainMenu touched");
        }
    }

    public void drawGui() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(0f, 0f, 0f, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(guiCam.combined);
        game.batch.begin();
        game.batch.draw(Own.assets.getTexture("BGMM"), 0, 0, ORTHO_WIDTH, ORTHO_HEIGHT);
        for (int i = 0; i < levelGrid.size(); i++) {
            Rectangle rect = levelGrid.get(i);
            game.batch.draw(Own.assets.getTextureRegion(String.valueOf(i + 1)), rect.getX(), rect.getY(), tileWidth, tileHeight);
        }

        game.batch.end();
    }

    @Override
    public void render(float delta) {
        drawGui();
        handleInput();
//        if (Gdx.input.justTouched()) {
//            Own.log("Setting playScreen: " + Own.device.getUsageHeapSize());
////            Own.assets.playSound("GLASS_BREAK");
//            game.setScreen(new LevelOne(game));
//            this.dispose();
//        }
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        game.batch.begin();
//        game.batch.draw(Own.assets.getTexture("BGMM"), 0, 0, ORTHO_WIDTH, ORTHO_HEIGHT);
//        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        Own.log(TAG, "Resizing");
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
        Own.log(TAG, "Disposing main menu");
    }
}
