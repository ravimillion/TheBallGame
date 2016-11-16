package ownLib.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import ownLib.Own;

/**
 * Created by ravi on 13.10.16.
 */

public class ControlsFactory {
    private TextureAtlas buttonAtlas = Own.assets.getTextureAtlas("CONTROL_BUTTONS");

    public TextButton getTextButton(float posX, float posY, float width, float height) {
        Skin skin = new Skin(Gdx.files.internal("controls/uiskin.json"));
        TextButton textButton = new TextButton("Pause", skin);
        textButton.setSize(width, height);
        textButton.setPosition(posX, posY);
        return textButton;
    }

    public ImageButton getImageButton(ButtonType type, float posX, float posY, float width, float height) {
        ImageButtonStyle style = new ImageButtonStyle();
        String upId = "play";
        String downId = "play";

        switch (type) {
            case RUNNING:
                upId = "play";
                downId = "play";
                break;
            case PAUSE:
                upId = "pause";
                downId = "pause";
                break;
            case QUIT:
                upId = "quit";
                downId = "quit";
                break;
            case RESUME:
                upId = "resume";
                downId = "resume";
                break;
            case RESTART:
                upId = "restart";
                downId = "restart";
                break;
            case READY:
                upId = "ready";
                downId = "ready";
                break;
        }

        style.imageUp = new TextureRegionDrawable(buttonAtlas.findRegion(upId));
        style.imageDown = new TextureRegionDrawable(buttonAtlas.findRegion(downId));

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

    public enum ButtonType {RUNNING, PAUSE, QUIT, RESUME, READY, RESTART}
}

