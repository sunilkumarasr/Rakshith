package com.village.villagevegetables.Config

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.village.villagevegetables.Activitys.NotificationsActivity
import com.village.villagevegetables.R
import org.json.JSONException
import org.json.JSONObject

class MyFirebaseMessagingService : FirebaseMessagingService(){

    var title: String = ""
    var body: String = ""
    var type: String = ""
    var email: String = ""

    companion object {
        private var title: String = ""
        var token: String? = null
        var body: String? = ""
        var type: String? = ""
        var receiver_id: String? = ""
        var id: String? = null
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("onMessageReceived: ", remoteMessage.data.toString())

        if (remoteMessage.data.isNotEmpty()) {
            val data = remoteMessage.data
            val message = data["notification_body"] ?: return

            try {
                // Try parsing the "body" as JSON
                val obj = JSONObject(message)

                // Check if it contains "notification" object
                if (obj.has("notification")) {
                    val notification = obj.getJSONObject("notification")
                    title = notification.optString("notification_title", title)
                    body = notification.optString("notification_body", body)
                    type = notification.optString("type", type)
                    email = notification.optString("email", email)
                } else {
                    // If no "notification" object, try direct keys in root
                    title = obj.optString("notification_title", title)
                    body = obj.optString("notification_body", body)
                    type = obj.optString("type", type)
                    email = obj.optString("email", email)
                }
            } catch (e: JSONException) {
                // If JSON parsing fails, fallback to keys directly from data map
                title = data["notification_title"] ?: data["title"] ?: title
                body = data["notification_body"] ?: message
                type = data["type"] ?: ""
                email = data["email"] ?: ""
            }

            createNotification(body)

        }
    }

    private fun createNotification(body: String) {
        Log.e("asdfghj_","123")

        val intent = Intent(this, NotificationsActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val channelId = "1"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            checkNotificationChannel(channelId)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.single_logo)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.single_logo)) // Full-color image
            .setContentTitle("Village Vegetables")
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(pendingIntent, true)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkNotificationChannel(channelId: String) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            channelId,
            "Village_Vegetables",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = body
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Companion.token = token
        Log.e("FCM Token", token)
    }

}