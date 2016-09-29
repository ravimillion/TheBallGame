package ownLib.assets;

/**
 * Created by ravi on 16.07.16.
 */
public class AudioAsset {
    private String key;
    private String uri;

    public AudioAsset(String key, String uri) {
        this.key = key;
        this.uri = uri;
    }
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }



}
