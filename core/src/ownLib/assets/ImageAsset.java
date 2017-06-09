package ownLib.assets;

/**
 * Created by ravi on 16.07.16.
 */
public class ImageAsset {
    String uri;
    int type;
    int x;
    int y;
    int width;
    int height;

    public ImageAsset(String uri, int type, int x, int y, int width, int height) {
        this.uri = uri;
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
