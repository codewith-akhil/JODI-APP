package com.example

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

/**
 * Application entry point.
 *
 * Installs Firebase App Check with the Play Integrity provider so that
 * Firebase Phone Authentication verifies silently in-app (Google Play
 * Integrity attestation) and never falls back to the reCAPTCHA browser
 * redirect on real devices with Google Play services.
 *
 * DEBUG builds use the DebugAppCheckProviderFactory instead, so local
 * emulator development is not blocked by attestation. The debug token is
 * printed to Logcat (filter: "AppCheck") and must be whitelisted once in
 * Firebase Console -> App Check -> Manage debug tokens.
 */
class SoulmateApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // 1. Initialize Firebase FIRST - before any Firebase service is touched.
        FirebaseApp.initializeApp(this)

        // 2. Install App Check immediately after Firebase init.
        //    - Release/Play Store builds -> Play Integrity (silent, in-app).
        //    - Debug builds -> Debug provider (emulator & local dev friendly).
        val factory = if (BuildConfig.DEBUG) {
            DebugAppCheckProviderFactory.getInstance()
        } else {
            PlayIntegrityAppCheckProviderFactory.getInstance()
        }

        FirebaseAppCheck.getInstance().apply {
            installAppCheckProviderFactory(factory)
            // Keep tokens fresh in the background so requests never stall
            // waiting for an attestation round-trip.
            setTokenAutoRefreshEnabled(true)
        }
    }
}
