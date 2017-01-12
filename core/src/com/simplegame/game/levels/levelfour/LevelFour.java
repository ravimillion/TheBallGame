package com.simplegame.game.levels.levelfour;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.kotcrab.vis.runtime.scene.Scene;
import com.kotcrab.vis.runtime.scene.SceneLoader;
import com.kotcrab.vis.runtime.scene.VisAssetManager;
import com.simplegame.game.GameController;

public class LevelFour implements Screen {

    GameController gameController;
    private Scene scene;
    private Scene controlsScene;
    private VisAssetManager manager;

    public LevelFour(GameController gameController) {
        this.gameController = gameController;
        setupLevel();
    }


    private void setupLevel() {
//        manager = new VisAssetManager(GameEntry.batch);
//        manager.getLogger().setLevel(Logger.ERROR);
//        loadGameScene();


//        loadMenuScene();
    }

    public void loadGameScene() {
        SceneLoader.SceneParameter levelParams = new SceneLoader.SceneParameter();
//        parameter.config.addSystem(ControlsLayerSystem.class);
//        levelParams.config.enable(SceneFeature.BOX2D_DEBUG_RENDER_SYSTEM);
//        levelParams.config.addSystem(SpriteBoundsCreator.class);
//        levelParams.config.addSystem(SpriteBoundsUpdater.class);
//        levelParams.config.addSystem(CameraControllerSystem.class);
//        levelParams.config.addSystem(PlayerSystem.class);
//        levelParams.config.addSystem(InitSystem.class);
//        levelParams.config.addSystem(ParticleSystem.class);
//
//        levelParams.config.addSystem(new SystemProvider() {
//            @Override
//            public BaseSystem create(EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
//                return new ControlsSystem(GameController.this);
//            }
//        });
//
//        scene = manager.loadSceneNow("scene/levelfour.scene", levelParams);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        scene.render();
    }

    @Override
    public void resize(int width, int height) {
        scene.resize(width, height);
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
        manager.dispose();
    }

//    public void notify(GameData state) {
//        switch (state) {
//            case LEVEL_END:
//                this.dispose();
////                gameEntry.setScreen(new MenuScreen(gameEntry));
//                break;
//            case RESTART_LEVEL:
//                this.dispose();
////                gameEntry.setScreen(new LevelFour(gameEntry));
//                break;
//            case QUIT:
//                this.dispose();
////                this.gameEntry.dispose();
//                System.exit(0);
//            default:
//                break;
//        }
//    }
}
