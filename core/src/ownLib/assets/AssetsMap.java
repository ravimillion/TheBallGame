package ownLib.assets;

import java.util.HashMap;

public class AssetsMap {
    public static final int TEXTURE_REGION = 1;
    public static final int TEXTURE = 2;
    public static final int TEXTURE_ATLAS = 3;

    HashMap<String, ImageAsset> imageAssets = new HashMap<>();
    HashMap<String, FontAsset> fontAssets = new HashMap<>();

    public AssetsMap() {
        createImageAssets();
        createFontAssets();
    }

    public HashMap<String, ImageAsset> getImageAssets() {
        return imageAssets;
    }

    public HashMap<String, FontAsset> getFontAssets() {
        return fontAssets;
    }

    private void createFontAssets() {
        fontAssets.put("DESYREL", new FontAsset("font/desyrel.ttf"));
        fontAssets.put("GOUDY", new FontAsset("font/goudy.ttf"));
        fontAssets.put("ACTOR", new FontAsset("font/actor.ttf"));
    }

    private void createImageAssets() {
        imageAssets.put("TRANSBALL", new ImageAsset("images/transball.png", TEXTURE, 0, 0, 0, 0));
    }
}
