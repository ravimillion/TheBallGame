package com.jauntymarble.game;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.jauntymarble.game.screens.GameEntry;

public class AndroidLauncher extends AndroidApplication implements AdHandler {
    private static final String TAG = "AndroidLauncher";
    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    protected AdView adView = null;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_ADS:
                    adView.setVisibility(View.VISIBLE);
                    break;
                case HIDE_ADS:
                    adView.setVisibility(View.GONE);
                    break;
            }
        }
    };
    private InterstitialAd mInterstitialAd;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String TEST_DEVICE_ID = "AD7F868BA605FAB451A8B4D9C9C1D5F5";  // Mobile
//    private String TEST_DEVICE_ID = "BE2F070AF06CBB8EBA25EEEA6B740174";  // Tablet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout layout = new RelativeLayout(this);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        View gameView = initializeForView(new GameEntry(this), config);
        layout.addView(gameView);

        // interstitial ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-4120235782147855/9080079921");
        final AdRequest.Builder interstitialAdBuilder = new AdRequest.Builder();
        interstitialAdBuilder.addTestDevice(TEST_DEVICE_ID);
        mInterstitialAd.loadAd(interstitialAdBuilder.build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                interstitialAdBuilder.addTestDevice(TEST_DEVICE_ID);
                mInterstitialAd.loadAd(interstitialAdBuilder.build());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i(TAG, "Interstitial Ad Loadded...");
            }
        });

        // banner ads
        adView = new AdView(this);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i(TAG, "Ad Loadded...");
            }
        });

        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-4120235782147855/5980717523");
        AdRequest.Builder bannerAdBuilder = new AdRequest.Builder();

        bannerAdBuilder.addTestDevice(TEST_DEVICE_ID);

        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layout.addView(adView, adParams);
        adView.loadAd(bannerAdBuilder.build());

        // firebase
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null);
        setContentView(layout);
    }

    @Override
    public void showAds(boolean show) {
        handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
    }

    @Override
    public void showInterstitialAd() {
        if (!GameData.SHOW_ADS) return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
    }
}
