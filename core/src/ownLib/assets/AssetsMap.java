package ownLib.assets;

import org.json.JSONException;
import org.json.JSONObject;

public class AssetsMap {
    public static final int TEXTURE_REGION = 1;
    public static final int TEXTURE = 2;


    JSONObject audioAssets = new JSONObject();
    JSONObject imageAssets = new JSONObject();
    JSONObject fontAssets = new JSONObject();
    JSONObject jsonAssets = new JSONObject();

    public AssetsMap() {
        createAudioAssets();
        createImageAssets();
        createFontAssets();
        createJsonAssets();
    }

    public JSONObject getAudioAssets() {
        return audioAssets;
    }

    public JSONObject getImageAssets() {
        return imageAssets;
    }

    public JSONObject getFontAssets() {
        return fontAssets;
    }

    public JSONObject getJsonAssets() {
        return jsonAssets;
    }

    private void createAudioAssets() {
        try {
            audioAssets.put("GLASS_ROLLING", "sounds/glassRolling.mp3");
            audioAssets.put("GLASS_BREAK", "sounds/glassBreak.wav");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void createJsonAssets() {
        try {
            jsonAssets.put("BALLOON", "json/balloon.json");
            jsonAssets.put("BOTTLE", "json/bottle.json");
            jsonAssets.put("SQUARE", "json/square.json");
            jsonAssets.put("TREESTUMP", "json/treestump.json");
            jsonAssets.put("ICICLE", "json/icicle.json");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createFontAssets() {
        try {
            fontAssets.put("DESYREL", "fonts/desyrel.ttf");
            fontAssets.put("GOUDY", "fonts/goudy.ttf");
            fontAssets.put("ACTOR", "fonts/actor.ttf");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createImageAssets() {
        try {
            imageAssets.put("BALLOON", createJSONObject("images/balloon.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("SQUARE", createJSONObject("images/square.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("TREESTUMP", createJSONObject("images/treestump.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("BOTTLE", createJSONObject("images/bottle.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("CARTTILE", createJSONObject("images/carttile.jpg", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("ICICLE", createJSONObject("images/icicle.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("GROUND", createJSONObject("images/ground.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("TRANSBALL", createJSONObject("images/transball.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("BGMM", createJSONObject("images/bgmm.jpg", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("LOADING", createJSONObject("images/loading.gif", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("BGL1", createJSONObject("images/background.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("BGL2", createJSONObject("images/background.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("BGL3", createJSONObject("images/background.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("BGL4", createJSONObject("images/background.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("BGL5", createJSONObject("images/background.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("BGL6", createJSONObject("images/background.png", TEXTURE, 0, 0, 0, 0));

            for (int i = 0; i < 6; i++) {
                imageAssets.put(String.valueOf(i + 1), createJSONObject("images/numbers.jpg", TEXTURE_REGION, i * 56, 0, 55, 55));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject createJSONObject(String uri, int type, int x, int y, int width, int height) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uri", uri);
            jsonObject.put("type", type);
            jsonObject.put("x", x);
            jsonObject.put("y", y);
            jsonObject.put("w", width);
            jsonObject.put("h", height);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}
