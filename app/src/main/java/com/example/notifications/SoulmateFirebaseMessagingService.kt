package com.example.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Receives FCM push notifications (new matches, messages, interests).
 *
 * Data payloads carry `title` / `body` / `type`; notification payloads are
 * displayed by the system tray automatically when the app is backgrounded.
 */
class SoulmateFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("SoulmateFCM", "Refreshed FCM token")
        // Persist so the ViewModel can register it with the user's account
        // as soon as the user is signed in (or update immediately otherwise).
        getSharedPreferences("soulmate_push", MODE_PRIVATE)
            .edit().putString("fcm_token", token).apply()
        PushTokenSync.scheduleSync(this)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title
            ?: message.data["title"]
            ?: "JODI Soulmate"
        val body = message.notification?.body
            ?: message.data["body"]
            ?: "You have a new update. Open the app to see it."
        val type = message.data["type"] ?: "SYSTEM"

        showNotification(title, body, type)
    }

    private fun showNotification(title: String, body: String, type: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra("push_type", type)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, type.hashCode(), intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = channelForType(type)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(type.hashCode(), builder.build())
    }

    private fun channelForType(type: String): String = when (type) {
        "MESSAGE" -> CHANNEL_MESSAGES
        "INTEREST", "MATCH" -> CHANNEL_MATCHES
        else -> CHANNEL_GENERAL
    }

    companion object {
        const val CHANNEL_GENERAL = "soulmate_general"
        const val CHANNEL_MESSAGES = "soulmate_messages"
        const val CHANNEL_MATCHES = "soulmate_matches"

        /** Idempotent — call from Application.onCreate and before posting. */
        fun createChannels(context: Context) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_GENERAL, "General Updates",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply { description = "Account, membership and app updates" },
                NotificationChannel(
                    CHANNEL_MESSAGES, "New Messages",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply { description = "Chat messages from your matches" },
                NotificationChannel(
                    CHANNEL_MATCHES, "Matches & Interests",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply { description = "New match requests and interests" }
            )
            channels.forEach { manager.createNotificationChannel(it) }
        }
    }
}
