package ownLib.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by ravi on 13.10.16.
 */

public class ControlsFactory {
    public enum ButtonType {
        PLAY, PAUSE, QUIT, RESUME, READY
    }

    private TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("controls/imagebuttons.pack"));

    public TextButton getTextButton(float posX, float posY, float width, float height) {
        Skin skin = new Skin(Gdx.files.internal("controls/uiskin.json"));
        TextButton textButton = new TextButton("Pause", skin);
        textButton.setSize(width, height);
        textButton.setPosition(posX, posY);
        return textButton;
    }

    public ImageButton getImageButton(ButtonType type, float posX, float posY, float width, float height) {
        TextureRegionDrawable buttonUp = null;
        TextureRegionDrawable buttonDown = null;

        ImageButtonStyle style = new ImageButtonStyle();

        switch(type) {
            case PLAY:
                buttonUp = new TextureRegionDrawable(buttonAtlas.findRegion("play"));
                buttonDown = new TextureRegionDrawable(buttonAtlas.findRegion("play"));
                break;
            case PAUSE:
                buttonUp = new TextureRegionDrawable(buttonAtlas.findRegion("pause"));
                buttonDown = new TextureRegionDrawable(buttonAtlas.findRegion("pause"));
                break;
            case QUIT:
                buttonUp = new TextureRegionDrawable(buttonAtlas.findRegion("quit"));
                buttonDown = new TextureRegionDrawable(buttonAtlas.findRegion("quit"));
                break;
            case RESUME:
                buttonUp = new TextureRegionDrawable(buttonAtlas.findRegion("resume"));
                buttonDown = new TextureRegionDrawable(buttonAtlas.findRegion("resume"));
                break;
            case READY:
                buttonUp = new TextureRegionDrawable(buttonAtlas.findRegion("ready"));
                buttonDown = new TextureRegionDrawable(buttonAtlas.findRegion("ready"));
                break;
        }

        style.imageUp = buttonUp;
        style.imageDown = buttonDown;

        ImageButton imageButton = new ImageButton(style);
        imageButton.setPosition(posX, posY);
        imageButton.setSize(width, height);

        // container for transformation
        /*
        Container container = new Container(imageButton);
        container.setTransform(true);
        container.setOrigin(container.getPrefWidth() / 2, container.getPrefHeight() / 2);
        container.setRotation(45);
        container.setScaleY(2.0f);
        */

        return imageButton;
    }
}

