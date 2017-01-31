package com.simplegame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.simplegame.game.GameController;
import com.simplegame.game.SpriteAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import ownLib.Own;

public class Splash implements Screen {
    GameController gameController;
    private String TAG = "Splash";
    private Texture loading = null;
    private Sprite splash = null;
    private float progress = 0;
    private SpriteBatch spriteBatch;

    private float logoWidth = Own.device.getScreenWidth() / 6;
    private float logoHeight = logoWidth + (logoWidth * 0.1f);
    private Vector2 logoPosition = new Vector2(Own.device.getScreenWidth() / 2 - logoWidth / 2, Own.device.getScreenHeight() / 2 - logoHeight * 0.2f);
    private Vector2 loadingMsgPos = new Vector2(Own.device.getScreenWidth() / 2 - 60, logoPosition.y - (logoPosition.y * 0.20f));
    private Vector2 logoSize = new Vector2(logoWidth, logoHeight);
    private TweenManager tweenManager;
    private GameEntry gameEntry;
    private Texture logo;
    private Sprite splashSprite;

    public Splash(GameEntry gameEntry, SpriteBatch spriteBatch) {
        this.gameEntry = gameEntry;
        this.spriteBatch = spriteBatch;
        Own.text.createFontForSplash();

        logo = new Texture(Gdx.files.internal("images/splash.png"));
        splashSprite = new Sprite(logo);
        splashSprite.setSize(logoWidth, logoHeight);
        splashSprite.setPosition(logoPosition.x, logoPosition.y);
    }

    @Override
    public void show() {
        Own.assets.loadAssetsInMemory();

        // TWEEN MANAGER
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        Tween.set(splashSprite, SpriteAccessor.FADE_IN_OUT).target(0).start(tweenManager);
        Tween.to(splashSprite, SpriteAccessor.FADE_IN_OUT, 4).target(1).start(tweenManager);
        Tween.to(splashSprite, SpriteAccessor.FADE_IN_OUT, 2).target(0).delay(3).start(tweenManager).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                Own.log("Finished....");
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            if (!isLoading()) {
                                afterLoad();
                                break;
                            }
                        }
                    }
                });
            }
        }).start(tweenManager);
    }

    @Override
    public void render(float delta) {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 1, 1, 1f);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);
        spriteBatch.begin();

//        Own.text.showProgress(spriteBatch, (int) (progress * 100), loadingMsgPos);
//        spriteBatch.draw(logo, logoPosition.x, logoPosition.y, logoSize.x, logoSize.y);
//        if (!isLoading()) {
//            afterLoad();
//            spriteBatch.end();
//            gl.glClearColor(1, 1, 1, 1f);
//            gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//            spriteBatch.begin();
//            spriteBatch.draw(logo, logoPosition.x, logoPosition.y, logoSize.x, logoSize.y);
//            Own.text.showProgress(spriteBatch, 100, loadingMsgPos);
//        }

//
        splashSprite.draw(spriteBatch);
        spriteBatch.end();
    }

    public boolean isLoading() {
        if (!Own.assets.update()) {
            progress = Own.assets.getProgress();
            Own.log(TAG, "Loading: " + progress);
            return true;
        }

        return false;
    }

    public void afterLoad() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Own.text.createFonts();
                Own.assets.createImageAssets();
                dispose();
                gameEntry.finishLoading();
            }
        });
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
