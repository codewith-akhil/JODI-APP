package com.example.network

import android.app.Activity
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

/**
 * Google Sign-In helper built on AndroidX Credential Manager.
 *
 * The returned Google ID Token is exchanged for a Firebase session by
 * [FirebaseManager.signInWithGoogleIdToken].
 *
 * NOTE: requires the OAuth web client (client_type 3) present in
 * google-services.json — the release configuration of this project ships it.
 */
object GoogleSignInHelper {

    private const val TAG = "GoogleSignInHelper"

    /**
     * Launches the Google account picker and returns the ID token,
     * or null when the user cancels / no credentials are available.
     */
    suspend fun getGoogleIdToken(activity: Activity, serverClientId: String): String? {
        return try {
            val credentialManager = CredentialManager.create(activity)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(serverClientId)
                .setFilterByAuthorizedAccounts(false) // show all accounts → real sign-up flow
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(activity, request)
            val credential = result.credential
            when (credential) {
                is GoogleIdTokenCredential -> credential.idToken
                else -> {
                    Log.w(TAG, "Unexpected credential type: ${credential.type}")
                    null
                }
            }
        } catch (e: GetCredentialException) {
            Log.e(TAG, "Google credential error: ${e.type} — ${e.message}")
            null
        } catch (e: Exception) {
            Log.e(TAG, "Google sign-in failed: ${e.message}", e)
            null
        }
    }

    /** Clears the credential state after logout so the picker shows again next time. */
    suspend fun clearCredentialState(activity: Activity) {
        try {
            CredentialManager.create(activity)
                .clearCredentialState(ClearCredentialStateRequest())
        } catch (e: Exception) {
            Log.w(TAG, "Clear credential state failed: ${e.message}")
        }
    }
}
