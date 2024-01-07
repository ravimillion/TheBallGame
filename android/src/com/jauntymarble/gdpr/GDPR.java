package com.jauntymarble.gdpr;

import android.app.Activity;
import android.util.Log;

import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

public class GDPR {
    private static String TAG = "GDPR";
    private GDPRListener gdprListener;

    public void OnGDPRProvided(GDPRListener gdprListener) {
        this.gdprListener = gdprListener;
    }

    public void init(Activity activity) {
        // Create a ConsentRequestParameters object.
        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(activity)
                .addTestDeviceHashedId("CDFD4A5F98DCD44551678A926EBF99A9")
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .build();

        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .setConsentDebugSettings(debugSettings)
                .build();


        ConsentInformation consentInformation = UserMessagingPlatform.getConsentInformation(activity);
        consentInformation.requestConsentInfoUpdate(
                activity,
                params,
                (ConsentInformation.OnConsentInfoUpdateSuccessListener) () -> {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            activity,
                            (ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                                if (loadAndShowError != null) {
                                    // Consent gathering failed.
                                    Log.w(TAG, String.format("%s: %s",
                                            loadAndShowError.getErrorCode(),
                                            loadAndShowError.getMessage()));
                                }

                                Log.w(TAG, "Already obtained");
                                // Consent has been gathered.
                                this.gdprListener.OnObtained();
                                if (consentInformation.canRequestAds()) {
//                                    initializeMobileAdsSdk();
                                }
                            }
                    );
                },
                (ConsentInformation.OnConsentInfoUpdateFailureListener) requestConsentError -> {
                    this.gdprListener.OnObtained();
                    // Consent gathering failed.
                    Log.w(TAG, String.format("%s: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()));
                });
    }
}
