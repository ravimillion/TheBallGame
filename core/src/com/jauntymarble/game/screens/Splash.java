package com.jauntymarble.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.jauntymarble.game.GameData;
import com.jauntymarble.game.SpriteAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import ownLib.Own;

public class Splash implements Screen {
    private SpriteBatch spriteBatch;

    private float LOGO_WIDTH = Own.device.getScreenHeight() / 2.5f;
    private float LOGO_HEIGHT = LOGO_WIDTH * 1.1f;

    private Vector2 LOGO_POSITION = new Vector2(Own.device.getScreenWidth() / 2 - LOGO_WIDTH / 2, Own.device.getScreenHeight() / 2 - LOGO_HEIGHT * 0.3f);
    private TweenManager tweenManager;
    private GameEntry gameEntry;
    private Texture logo;
    private Sprite splashSprite;

    public Splash(GameEntry gameEntry, SpriteBatch spriteBatch) {
        this.gameEntry = gameEntry;
        this.spriteBatch = spriteBatch;
        Own.text.createFontForSplash(Color.FIREBRICK);
        splashSprite = new Sprite(new Texture(Gdx.files.internal("images/splash.png")));
        splashSprite.setSize(LOGO_WIDTH, LOGO_HEIGHT);
        splashSprite.setPosition(LOGO_POSITION.x, LOGO_POSITION.y);
    }

    @Override
    public void show() {
        Own.assets.loadAssetsInMemory();

        // TWEEN MANAGER
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        Tween.set(splashSprite, SpriteAccessor.FADE_IN_OUT).target(0).start(tweenManager);
        Tween.to(splashSprite, SpriteAccessor.FADE_IN_OUT, GameData.getConfigData("SPLASH_SHOW_DELAY")).target(1).start(tweenManager).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        // start loading when the logo is fully shown
                        while (!Own.assets.update()) {
                        }
                        // when loading finishes trigger fade out after creating assets
                        Own.text.createFonts();
                        Own.assets.createImageAssets();
                        Tween.to(splashSprite, SpriteAccessor.FADE_IN_OUT, GameData.getConfigData("SPLASH_HIDE_DELAY")).target(0).start(tweenManager).setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        dispose();
                                        gameEntry.finishLoading();
                                    }
                                });
                            }
                        });

                    }
                });
            }
        });
    }

    @Override
    public void render(float delta) {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 1, 1, 1f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

        spriteBatch.begin();
        splashSprite.draw(spriteBatch);
        spriteBatch.end();
    }

    public boolean isLoading() {
        return !Own.assets.update();
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
        Own.log("Disposing Splash Screen");
    }
}
