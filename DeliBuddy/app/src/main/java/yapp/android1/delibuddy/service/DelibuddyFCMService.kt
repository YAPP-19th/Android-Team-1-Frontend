package yapp.android1.delibuddy.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import yapp.android1.delibuddy.R
import yapp.android1.delibuddy.ui.splash.SplashActivity

class DelibuddyFCMService : FirebaseMessagingService() {
    enum class Channel(val channelId: String, val channelName: String) {
        COMMENT("delibuddy_comment", "comment"),
        PARTY_STATUS("delibuddy_party_status", "comment")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.w("fcm ${message.data}")

        // 조건 체크
        val title: String = message.data["title"] ?: ""
        val content: String = message.data["message"] ?: ""
        val route: String = message.data["route"] ?: ""

        if (title.isNotBlank()) {
            createNotificationChannel()

            val intent = Intent(this, SplashActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                Timber.w("route: $route")
                if (route.isNotBlank()) {
                    putExtra("route", route)
                }
            }
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )


            val builder = NotificationCompat.Builder(this, Channel.COMMENT.channelId)
                .setSmallIcon(R.mipmap.icon_delibuddy)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.icon_delibuddy))
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {
                val notificationId = SystemClock.uptimeMillis().toInt()
                notify(notificationId, builder.build())
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText = "댓글 알림"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                Channel.COMMENT.channelId,
                Channel.COMMENT.channelName,
                importance
            ).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}