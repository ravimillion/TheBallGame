package ownLib;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;

public class Audio {
    private String TAG = "Audio";

    public Sound loadSound(String fileName) throws OwnException {
        // iOS doesn't support OGG
        if (Own.device.getPlatformType() == Application.ApplicationType.iOS && fileName.endsWith("ogg")) {
            throw new OwnException(OwnException.OWN_CODE_FILETYPE_NOT_SUPPORTED, OwnException.OWN_MSG_FILETYPE_NOT_SUPPORTED);
        }

        File file = new File(fileName);
        Own.log(TAG, "Add check for size");
        Own.log(TAG, file.getAbsolutePath() + " " + file.canRead());

        if (!file.canRead()) {
            throw new OwnException(OwnException.OWN_CODE_FILE_NOT_READABLE, OwnException.OWN_MSG_FILE_NOT_READABLE);
        }

        if (file.exists()) {
            FileHandle fileHandle = new FileHandle(file);
            return Gdx.audio.newSound(Gdx.files.internal(fileName));
        } else {
            throw new OwnException(OwnException.OWN_CODE_FILE_NOT_FOUND, OwnException.OWN_MSG_FILE_NOT_FOUND);
        }

    }

    public void loadMusic(String fileName) throws OwnException {
        // iOS doesn't support OGG
        if (Own.device.getPlatformType() == Application.ApplicationType.iOS && fileName.endsWith("ogg")) {
            throw new OwnException(OwnException.OWN_CODE_FILETYPE_NOT_SUPPORTED, OwnException.OWN_MSG_FILETYPE_NOT_SUPPORTED);
        }

        File file = new File(fileName);
        Own.log(TAG, "Add check for size");
        if (file.exists()) {
            FileHandle fileHandle = new FileHandle(file);
            Music music = Gdx.audio.newMusic(fileHandle);

        } else {
            throw new OwnException(OwnException.OWN_CODE_FILE_NOT_FOUND, OwnException.OWN_MSG_FILE_NOT_FOUND);
        }
    }
}
