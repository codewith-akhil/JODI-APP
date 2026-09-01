package com.example.notifications

import android.content.Context
import com.example.network.FirebaseManager
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Keeps the user's FCM registration token in sync with their account node
 * (users/{uid}/fcmToken) so Cloud Functions can deliver targeted pushes.
 */
object PushTokenSync {

    private const val PREFS = "soulmate_push"
    private const val KEY = "fcm_token"

    fun scheduleSync(context: Context) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) return@addOnCompleteListener
            val token = task.result
            prefs.edit().putString(KEY, token).apply()
            if (FirebaseManager.isSignedIn()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        FirebaseManager.upsertUser(
                            FirebaseManager.currentUid ?: return@launch,
                            mapOf("fcmToken" to token)
                        )
                    } catch (_: Exception) { /* retried on next login */ }
                }
            }
        }
    }

    /** Called right after a successful login. */
    fun syncAfterLogin(scope: CoroutineScope) {
        scope.launch {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                FirebaseManager.upsertUser(
                    FirebaseManager.currentUid ?: return@launch,
                    mapOf("fcmToken" to token)
                )
            } catch (_: Exception) { /* offline tolerant */ }
        }
    }
}
