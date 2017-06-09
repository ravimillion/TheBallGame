package ownLib.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;

import java.util.HashMap;

import ownLib.Own;
public class Assets extends AssetManager {

    private final String TAG = "Assets";

    private AssetsMap assetMap;
    private HashMap<String, ImageAsset> imageAssets;
    private HashMap<String, FontAsset> fontAssets;
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
        return this.get(fontAssets.get(key).uri);
    }

    public void loadAssetsInMemory() {
        imageAssets = assetMap.getImageAssets();
        fontAssets = assetMap.getFontAssets();
        loadImageAssets();
        loadFontAssets();
    }

    private void loadFontAssets() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        this.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));

        for (String key : fontAssets.keySet()) {
            Own.log(TAG, "Loading... " + fontAssets.get(key).uri);
            this.load(fontAssets.get(key).uri, FreeTypeFontGenerator.class);
        }
    }

    public void createFontAssets(String fontType, String fontName, FreeTypeFontGenerator.FreeTypeFontParameter parameter) {
        FreeTypeFontGenerator generator = null;

        for (String key : fontAssets.keySet()) {
            generator = this.get(fontAssets.get(key).uri);
            fontObjects.put(fontType, generator.generateFont(parameter));
        }
    }

    private void loadImageAssets() {
        for (String key : imageAssets.keySet()) {
            ImageAsset imageAsset = imageAssets.get(key);
            if (imageAsset.type == AssetsMap.TEXTURE_ATLAS) {
                this.load(imageAsset.uri, TextureAtlas.class);
            } else { // texture and texture regions are both loaded by texture.class
                this.load(imageAsset.uri, Texture.class);
            }
        }
    }

    public void createImageAssets() {
        for (String key : imageAssets.keySet()) {
            ImageAsset imageAsset = imageAssets.get(key);
            switch (imageAsset.type) {
                case AssetsMap.TEXTURE_REGION:
                    textureRegionObjects.put(key, new TextureRegion((Texture) this.get(imageAsset.uri), imageAsset.x, imageAsset.y, imageAsset.width, imageAsset.height));
                    break;
                case AssetsMap.TEXTURE:
                    textureObjects.put(key, (Texture) this.get(imageAsset.uri));
                    break;
                case AssetsMap.TEXTURE_ATLAS:
                    textureAtlasObjects.put(key, (TextureAtlas) this.get(imageAsset.uri));
            }
        }
    }
}
