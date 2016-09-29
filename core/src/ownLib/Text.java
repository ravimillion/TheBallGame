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
    private String TAG = "Text";
    private BitmapFont font;
    public String GOUDY_PAR = "GOUDY_PAR";
    public String DESYREL_PAR = "DESYREL_PAR";
    public String SCORE = "SCORE";

    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;

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
    }
    public void draw(SpriteBatch batch, String msg, Vector2 pos, String fontType) {
        font = Own.assets.getFontType(fontType);
        font.draw(batch, msg, pos.x, pos.y);
    }
}
