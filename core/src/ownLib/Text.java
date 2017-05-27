package ownLib;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by ravi on 11.09.16.
 */
public class Text {
    public String GOUDY_PAR = "GOUDY_PAR";
    public String DESYREL_PAR = "DESYREL_PAR";
    public String SCORE = "SCORE";
    public String MENU = "SCENE_MENU";
    private String TAG = "Text";
    private BitmapFont font;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    private BitmapFont splashFont;

    public void showProgress(SpriteBatch batch, String progressMessage, Vector2 pos) {
        splashFont.draw(batch, progressMessage, pos.x, pos.y);
    }

    public void createFontForSplash(Color color) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font/SF Atarian System.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.color = color;
        splashFont = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();
    }

    public void createFonts() {
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Normal description fonts
        parameter.size = (int) (16 * Gdx.graphics.getDensity());
        parameter.color = Color.BLACK;
        Own.assets.createFontAssets(DESYREL_PAR, "DESYREL", parameter);
        Own.assets.createFontAssets(GOUDY_PAR, "GOUDY", parameter);

        // Score fonts
        parameter.size = (int) (24 * Gdx.graphics.getDensity());
        parameter.color = Color.FIREBRICK;
        Own.assets.createFontAssets(SCORE, "ACTOR", parameter);

        // Menu fonts
        parameter.size = (int) (48 * Gdx.graphics.getDensity());
        parameter.color = Color.WHITE;
        Own.assets.createFontAssets(MENU, "ACTOR", parameter);
    }

    public void draw(SpriteBatch batch, String msg, Vector2 pos, String fontType) {
        font = Own.assets.getFontType(fontType);
        font.draw(batch, msg, pos.x, pos.y);
    }
}
