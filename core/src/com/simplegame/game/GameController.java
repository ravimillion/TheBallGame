package com.simplegame.game;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.simplegame.game.levels.levelfour.systems.ParticleSystem;
import com.simplegame.game.levels.levelfour.systems.PlayerSystem;
import com.simplegame.game.levels.levelfour.systems.SpriteBoundsCreator;
import com.simplegame.game.levels.levelfour.systems.SpriteBoundsUpdater;
import com.simplegame.game.screens.GameEntry;
import com.simplegame.game.screens.MenuScreen;

public class GameController implements Screen {
    public static int WORLD_WIDTH = 573;
    public static int WORLD_HEIGHT = 32;

    public SpriteBatch spriteBatch;
    GameEntry game;
    private Scene scene = null;
    private VisAssetManager manager;
    private String scenePath;

    public GameController(GameEntry game, SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
        this.game = game;

        manager = new VisAssetManager(spriteBatch);
        manager.getLogger().setLevel(Logger.ERROR);
    }

    private void unloadPreviousScene() {
        if (scenePath != null) {
            manager.unload(scenePath);
            scenePath = null;
            scene = null;
        }
    }

    public void loadMenuScene() {
        unloadPreviousScene();
        SceneLoader.SceneParameter levelParams = new SceneLoader.SceneParameter();

        levelParams.config.addSystem(SpriteBoundsCreator.class);
        levelParams.config.addSystem(SpriteBoundsUpdater.class);

        levelParams.config.addSystem(new SystemProvider() {
            @Override
            public BaseSystem create(EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
                return new MenuScreen(GameController.this);
            }
        });

        scenePath = "scene/menu.scene";
        scene = manager.loadSceneNow(scenePath, levelParams);
    }

    public void loadLevelOneScene() {
        unloadPreviousScene();

        SceneLoader.SceneParameter levelParams = new SceneLoader.SceneParameter();
//        levelParams.config.enable(SceneFeature.BOX2D_DEBUG_RENDER_SYSTEM);
        levelParams.config.addSystem(SpriteBoundsCreator.class);
        levelParams.config.addSystem(SpriteBoundsUpdater.class);
        levelParams.config.addSystem(InitSystem.class);
        levelParams.config.addSystem(ParticleSystem.class);

        levelParams.config.addSystem(new SystemProvider() {
            @Override
            public BaseSystem create(EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
                return new CameraControllerSystem(GameController.this);
            }
        });

        levelParams.config.addSystem(new SystemProvider() {
            @Override
            public BaseSystem create(EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
                return new PlayerSystem(GameController.this);
            }
        });

        levelParams.config.addSystem(new SystemProvider() {
            @Override
            public BaseSystem create(EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
                return new ControlsSystem(GameController.this);
            }
        });

        scenePath = "scene/levelone.scene";
        scene = manager.loadSceneNow(scenePath, levelParams);

    }

    public void loadLevelTwoScene() {
        unloadPreviousScene();

        SceneLoader.SceneParameter levelParams = new SceneLoader.SceneParameter();
        levelParams.config.enable(SceneFeature.BOX2D_DEBUG_RENDER_SYSTEM);
        levelParams.config.addSystem(SpriteBoundsCreator.class);
        levelParams.config.addSystem(SpriteBoundsUpdater.class);
        levelParams.config.addSystem(InitSystem.class);
        levelParams.config.addSystem(ParticleSystem.class);

        levelParams.config.addSystem(new SystemProvider() {
            @Override
            public BaseSystem create(EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
                return new CameraControllerSystem(GameController.this);
            }
        });

        levelParams.config.addSystem(new SystemProvider() {
            @Override
            public BaseSystem create(EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
                return new PlayerSystem(GameController.this);
            }
        });

        levelParams.config.addSystem(new SystemProvider() {
            @Override
            public BaseSystem create(EntityEngineConfiguration config, RuntimeContext context, SceneData data) {
                return new ControlsSystem(GameController.this);
            }
        });

        scenePath = "scene/leveltwo.scene";
        scene = manager.loadSceneNow(scenePath, levelParams);

    }

    public void loadLevelThreeScene() {

    }

    @Override
    public void show() {
        loadMenuScene();
//        loadLevelOneScene();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (scene != null) scene.render();
    }

    @Override
    public void resize(int width, int height) {
        if (scene != null) scene.render();
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

    public void dispose() {
        spriteBatch.dispose();
    }

    public void notify(GameState gameState) {
        loadMenuScene();
    }
}
