package ownLib.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import ownLib.Own;

public class Assets extends AssetManager {

    private final String TAG = "Assets";

    private AssetsMap assetMap;
    private JSONObject audioAssets;
    private JSONObject imageAssets;
    private JSONObject fontAssets;
    private JSONObject jsonAssets;
    private HashMap<String, FileHandle> jsonObjects;
    private HashMap<String, Texture> textureObjects;
    private HashMap<String, TextureAtlas> textureAtlasObjects;
    private HashMap<String, TextureRegion> textureRegionObjects;
    private HashMap<String, BitmapFont> fontObjects;

    public Assets() {
        assetMap = new AssetsMap();
        textureAtlasObjects = new HashMap<String, TextureAtlas>();
        textureRegionObjects = new HashMap<String, TextureRegion>();
        textureObjects = new HashMap<String, Texture>();
        fontObjects = new HashMap<String, BitmapFont>();
        jsonObjects = new HashMap<String, FileHandle>();
    }


    public void playSound(String assetKey) {
        String assetURI = null;

        try {
            assetURI = audioAssets.get(assetKey).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (this.isLoaded(assetURI)) {
            Sound sound = this.get(assetURI, Sound.class);
            sound.play();
        } else {
            Own.log(TAG, "Error: " + assetURI + " not loaded");
        }
    }

    public FileHandle getJsonAsset(String key) {
        return jsonObjects.get(key);
    }

    public BitmapFont getFontType(String key) {
        return fontObjects.get(key);
    }

    public TextureRegion getTextureRegion(String key) {
        return textureRegionObjects.get(key);
    }

    public TextureAtlas getTextureAtlas(String key) {
        return textureAtlasObjects.get(key);
    }

    public Texture getTexture(String key) {
        return textureObjects.get(key);
    }

    public FreeTypeFontGenerator getFontGenerator(String key) {
        try {
            return this.get(fontAssets.getString(key));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadAssetsInMemory() {
        // get assets
        audioAssets = assetMap.getAudioAssets();
        imageAssets = assetMap.getImageAssets();
        fontAssets = assetMap.getFontAssets();
        jsonAssets = assetMap.getJsonAssets();

        // load assets
        loadAudioAssets();
        loadImageAssets();
        loadFontAssets();
        loadJsonAssets();
    }

    private void loadFontAssets() {
        // this function only loads the ttf files
        Iterator<String> iterator = fontAssets.keys();
        FileHandleResolver resolver = new InternalFileHandleResolver();
        this.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));

        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                Own.log(TAG, "Loading... " + fontAssets.getString(key));
                this.load(fontAssets.getString(key), FreeTypeFontGenerator.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void createFontAssets(String fontType, String fontName, FreeTypeFontGenerator.FreeTypeFontParameter parameter) {
        // this function creates new fonts
        // with reference to the chart on this link - http://reeddesign.co.uk/test/points-pixels.html
        FreeTypeFontGenerator generator = null;

        Iterator<String> iter = fontAssets.keys();
        while (iter.hasNext()) {
            try {
                String key = iter.next(); // key would be the name of the font in capital letters
                if (fontName.equals(key)) {
                    generator = this.get(fontAssets.getString(key));
                    fontObjects.put(fontType, generator.generateFont(parameter));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void loadJsonAssets() {
        // with reference to the chart on this link - http://reeddesign.co.uk/test/points-pixels.html
        Iterator<String> iter = jsonAssets.keys();
        while (iter.hasNext()) {
            try {
                String key = iter.next(); // key would be the name of the font in capital letters
                jsonObjects.put(key, Gdx.files.internal(jsonAssets.getString(key)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void loadImageAssets() {
        Iterator<String> iterator = imageAssets.keys();

        while (iterator.hasNext()) {
            String key = iterator.next();
            JSONObject obj = imageAssets.getJSONObject(key);
            Own.log(TAG, obj.getString("uri"));
            try {
                if (obj.getInt("type") == AssetsMap.TEXTURE_ATLAS) {
                    this.load(obj.getString("uri"), TextureAtlas.class);
                } else { // texture and texture regions are both loaded by texture.class
                    this.load(obj.getString("uri"), Texture.class);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadAudioAssets() {
        Iterator<String> iterator = audioAssets.keys();

        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                this.load(audioAssets.getString(key), Sound.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void createImageAssets() {
        Iterator<String> iterator = imageAssets.keys();
        JSONObject obj = null;
        while (iterator.hasNext()) {
            String key = iterator.next();

            try {
                obj = imageAssets.getJSONObject(key);
                switch (obj.getInt("type")) {
                    case AssetsMap.TEXTURE_REGION:
                        textureRegionObjects.put(key, new TextureRegion(
                                (Texture) this.get(obj.getString("uri")),
                                obj.getInt("x"),
                                obj.getInt("y"),
                                obj.getInt("w"),
                                obj.getInt("h"))
                        );
                        break;
                    case AssetsMap.TEXTURE:
                        textureObjects.put(key, (Texture) this.get(obj.getString("uri")));
                        break;
                    case AssetsMap.TEXTURE_ATLAS:
                        textureAtlasObjects.put(key, (TextureAtlas) this.get(obj.getString("uri")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
