package com.jauntymarble.game;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.jauntymarble.game.screens.LoadingScreen;
import com.kotcrab.vis.runtime.scene.Scene;
import com.kotcrab.vis.runtime.scene.SceneLoader.SceneParameter;
import com.kotcrab.vis.runtime.scene.VisAssetManager;

public class OwnSceneLoader extends VisAssetManager {
    private String scenePath;
    private LoadingScreen loadingScreen;

    public OwnSceneLoader(Batch batch) {
        super(batch);
    }

    public OwnSceneLoader(FileHandleResolver resolver, Batch batch) {
        super(resolver, batch);
    }

    @Override
    public Scene loadSceneNow(String scenePath, SceneParameter parameter) {
        this.scenePath = scenePath;
        load(scenePath, Scene.class, parameter);
        return null;
    }

    public Scene getScene() {
        Scene scene = get(scenePath, Scene.class);
        scene.init();
        return scene;
    }
}
