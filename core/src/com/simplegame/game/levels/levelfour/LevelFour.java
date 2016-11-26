package com.simplegame.game.levels.levelfour;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Logger;
import com.kotcrab.vis.runtime.RuntimeContext;
import com.kotcrab.vis.runtime.data.SceneData;
import com.kotcrab.vis.runtime.scene.Scene;
import com.kotcrab.vis.runtime.scene.SceneFeature;
import com.kotcrab.vis.runtime.scene.SceneLoader;
import com.kotcrab.vis.runtime.scene.SystemProvider;
import com.kotcrab.vis.runtime.scene.VisAssetManager;
import com.kotcrab.vis.runtime.util.EntityEngineConfiguration;
import com.simplegame.game.levels.GameState;
import com.simplegame.game.levels.levelfour.systems.CameraControllerSystem;
import com.simplegame.game.levels.levelfour.systems.ControlsSystem;
import com.simplegame.game.levels.levelfour.systems.InitSystem;
import com.simplegame.game.levels.levelfour.systems.PhysicsParticleSystem;
import com.simplegame.game.levels.levelfour.systems.PlayerSystem;
import com.simplegame.game.levels.levelfour.systems.SpriteBoundsCreator;
import com.simplegame.game.levels.levelfour.systems.SpriteBoundsUpdater;
import com.simplegame.game.screens.GameEntry;
import com.simplegame.game.screens.MainMenuScreen;

public class LevelFour implements Screen {


    public GameEntry gameEntry;
    private Scene scene;
    private Scene controlsScene;
    private VisAssetManager manager;

    public LevelFour(GameEntry gameEntry) {
        this.gameEntry = gameEntry;
        setupLevel();
    }


    private void setupLevel() {
        manager = new VisAssetManager(GameEntry.batch);
        manager.getLogger().setLevel(Logger.ERROR);
        loadGameScene();


//        loadMenuScene();
    }

    public void loadGameScene() {
        SceneLoader.SceneParameter levelParams = new SceneLoader.SceneParameter();
//        parameter.config.addSystem(ControlsLayerSystem.class);
        levelParams.config.enable(SceneFeature.BOX2D_DEBUG_RENDER_SYSTEM);
        levelParams.config.addSystem(SpriteBoundsCreator.class);
        levelParams.config.addSystem(SpriteBoundsUpdater.class);
        levelParams.config.addSystem(CameraControllerSystem.class);
        levelParams.config.addSystem(PlayerSystem.class);
        levelParams.config.addSystem(InitSystem.class);
        levelParams.config.addSystem(PhysicsParticleSystem.class);

        levelParams.config.addSystem(new SystemProvider() {
            @Override
            public BaseSystem create(EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
                return new ControlsSystem(LevelFour.this);
            }
        });

        scene = manager.loadSceneNow("scene/levelfour.scene", levelParams);
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

    public void notify(GameState state) {
        switch (state) {
            case LEVEL_END:
                this.dispose();
                gameEntry.setScreen(new MainMenuScreen(gameEntry));
                break;
            case RESTART_LEVEL:
                this.dispose();
                gameEntry.setScreen(new LevelFour(gameEntry));
                break;
            case QUIT:
                this.dispose();
                this.gameEntry.dispose();
                System.exit(0);
            default:
                break;
        }
    }
}
