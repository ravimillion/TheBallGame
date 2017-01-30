package ownLib.assets;

import org.json.JSONException;
import org.json.JSONObject;

public class AssetsMap {
    public static final int TEXTURE_REGION = 1;
    public static final int TEXTURE = 2;
    public static final int TEXTURE_ATLAS = 3;


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
            audioAssets.put("GLASS_ROLLING", "sound/glassRolling.mp3");
            audioAssets.put("GLASS_BREAK", "sound/glassBreak.wav");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void createJsonAssets() {
        try {
            jsonAssets.put("BOTTLE", "json/bottle.json");
            jsonAssets.put("TOPWOODBOX", "json/topwoodbox.json");
            jsonAssets.put("WOODBOX", "json/woodbox.json");
            jsonAssets.put("BOXLEFT", "json/boxleft.json");
            jsonAssets.put("BOXRIGHT", "json/boxright.json");
            jsonAssets.put("HORIPLATFORM", "json/horiplatform.json");
            jsonAssets.put("VERTPLATFORM", "json/vertplatform.json");
            jsonAssets.put("CURVELEFT", "json/curveleft.json");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createFontAssets() {
        try {
            fontAssets.put("DESYREL", "font/desyrel.ttf");
            fontAssets.put("GOUDY", "font/goudy.ttf");
            fontAssets.put("ACTOR", "font/actor.ttf");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createImageAssets() {
        try {
            // texture
            imageAssets.put("BOTTLE", createJSONObject("images/bottle.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("GRASS", createJSONObject("images/ground.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("TRANSBALL", createJSONObject("images/transball.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("CURVELEFT", createJSONObject("images/curveleft.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("TOPWOODBOX", createJSONObject("images/topwoodbox.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("WOODBOX", createJSONObject("images/woodbox.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("WOODFLOOR", createJSONObject("images/woodfloor.png", TEXTURE, 0, 0, 0, 0));

            imageAssets.put("BOXLEFT", createJSONObject("images/boxleft.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("HORIPLATFORM", createJSONObject("images/horiplatform.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("VERTPLATFORM", createJSONObject("images/vertplatform.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("BOXRIGHT", createJSONObject("images/boxright.png", TEXTURE, 0, 0, 0, 0));
            imageAssets.put("BGMM", createJSONObject("images/bgmm.jpg", TEXTURE, 0, 0, 0, 0));

//            // texture atlas files are pack files
//            imageAssets.put("CONTROL_BUTTONS", createJSONObject("controls/imagebuttons.pack", TEXTURE_ATLAS, 0, 0, 0, 0));

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
