package ownLib;

/**
 * Created by ravi on 10.07.16.
 */
public class OwnException extends Exception{
    private final String TAG = "OwnException";
    public int code;
    public String message;

    public OwnException(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public void showException() {
        Own.log(TAG, "Code: " + this.code + " " + "MSG: " + this.message);
    }
    public static final int  OWN_CODE_FILE_NOT_FOUND = -1000;
    public static final String OWN_MSG_FILE_NOT_FOUND = "File not found";

    public static final int  OWN_CODE_FILETYPE_NOT_SUPPORTED = -1001;
    public static final String OWN_MSG_FILETYPE_NOT_SUPPORTED = "Filetype not supported";

    public static final int  OWN_CODE_NO_ACCELEROMETER_FOUND = -1002;
    public static final String OWN_MSG_NO_ACCELEROMETER_FOUND = "No accelerometer found";

    public static final int  OWN_CODE_FILE_NOT_READABLE = -1002;
    public static final String OWN_MSG_FILE_NOT_READABLE = "Can not read the file";
}
