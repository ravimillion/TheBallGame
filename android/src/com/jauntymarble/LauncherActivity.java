package com.jauntymarble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jauntymarble.game.AndroidLauncher;
import com.jauntymarble.game.R;
import com.jauntymarble.gdpr.GDPR;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        GDPR gdpr = new GDPR();
        gdpr.OnGDPRProvided(() -> {
            Intent myIntent = new Intent(LauncherActivity.this, AndroidLauncher.class);
            LauncherActivity.this.startActivity(myIntent);
        });

        gdpr.init(this);
    }
}