package com.example

import android.app.Application
import com.example.notifications.SoulmateFirebaseMessagingService
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

/**
 * Application entry point.
 *
 * Installs Firebase App Check so Firebase Phone Authentication verifies
 * silently in-app and NEVER falls back to the reCAPTCHA browser redirect:
 *
 *  - Release/Play Store builds -> Play Integrity attestation (built-in).
 *  - Debug builds -> Debug provider seeded with a FIXED debug token
 *    (FIREBASE_APPCHECK_DEBUG_TOKEN from .env via the Secrets plugin).
 *
 * The debug token must be whitelisted ONCE in:
 *   Firebase Console -> App Check -> Apps -> Manage debug tokens
 * After that, debug builds exchange it for a valid App Check token and
 * phone verification stays inside the app — no browser captcha.
 */
class SoulmateApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // 0. Notification channels for push (idempotent)
        SoulmateFirebaseMessagingService.createChannels(this)

        // 1. Initialize Firebase FIRST - before any Firebase service is touched.
        FirebaseApp.initializeApp(this)

        if (BuildConfig.DEBUG) {
            // 2a. Pre-seed the debug provider's storage with our FIXED debug
            //     token so it is deterministic across reinstalls. The provider
            //     reads it from:
            //     prefs "com.google.firebase.appcheck.debug.store.<persistenceKey>"
            //     key   "com.google.firebase.appcheck.debug.DEBUG_SECRET"
            seedAppCheckDebugToken()

            FirebaseAppCheck.getInstance().apply {
                installAppCheckProviderFactory(DebugAppCheckProviderFactory.getInstance())
                setTokenAutoRefreshEnabled(true)
            }
        } else {
            // 2b. Production: silent Play Integrity attestation.
            FirebaseAppCheck.getInstance().apply {
                installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance())
                setTokenAutoRefreshEnabled(true)
            }
        }
    }

    private fun seedAppCheckDebugToken() {
        val token: String? = try {
            val field = BuildConfig::class.java.getField("FIREBASE_APPCHECK_DEBUG_TOKEN")
            field.get(null) as? String
        } catch (_: Exception) {
            null
        }
        if (token.isNullOrBlank()) return
        try {
            val persistenceKey = FirebaseApp.getInstance().persistenceKey
            getSharedPreferences(
                "com.google.firebase.appcheck.debug.store.$persistenceKey",
                MODE_PRIVATE
            ).edit()
                .putString("com.google.firebase.appcheck.debug.DEBUG_SECRET", token)
                .apply()
        } catch (_: Exception) {
            // Never block app startup over the debug token
        }
    }
}
