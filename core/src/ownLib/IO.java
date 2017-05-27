package ownLib;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

public class IO extends InputMultiplexer {
    private String TAG = "IO";

    public IO() {
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setCatchMenuKey(true);
        if (Own.device.getPlatformType() == Application.ApplicationType.Desktop) {
            Gdx.input.setCursorCatched(true);
        }
    }

    @Override
    public void addProcessor(InputProcessor processor) {
        boolean alreadyAdded = false;

        for (InputProcessor inputProcessor : this.getProcessors()) {
            if (processor.equals(inputProcessor)) {
                alreadyAdded = true;
            }
        }

        if (!alreadyAdded) super.addProcessor(processor);
    }
}
