package com.dbvertex.job.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.NavDeepLinkBuilder
import com.dbvertex.job.MainActivity
import com.dbvertex.job.R
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationFirebaseServices  : FirebaseMessagingService(),LifecycleObserver {
    val TAG = "Service"
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    lateinit var pendingIntent:PendingIntent
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        showNotification("DB-JOB", remoteMessage.getData().get("message").toString())
             //  Toast.makeText(JobApp.instance.applicationContext, "Success ${remoteMessage.data}", Toast.LENGTH_SHORT).show()

    }
    fun showNotification(
        title: String?,
        message: String?
    ) {

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("notificationsss","1")

      if(!SharedPrefrenceHelper.user.userid.isNullOrEmpty()){
           pendingIntent =  NavDeepLinkBuilder(applicationContext)
              .setGraph(R.navigation.map_navigation_graph)
              .setDestination(R.id.jobBoardMain)
              .setArguments(null)
              .createPendingIntent()
      }else{
          pendingIntent =  NavDeepLinkBuilder(applicationContext)
              .setGraph(R.navigation.map_navigation_graph)
              .setDestination(R.id.splashFragment)
              .setArguments(null)
              .createPendingIntent()
      }



      //  val contentView = RemoteViews("com.pahadi.uncle", com.pahadi.uncle.R.layout.fragment_notification_view)

        // checking if android version is greater than oreo(API 26) or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)

            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)

                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(message)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
        } else {
            builder = Notification.Builder(this)
                // .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(message)
                .setContentIntent(pendingIntent)
        }
        val notificationId: Long = System.currentTimeMillis()
        notificationManager.notify(notificationId.toInt(), builder.build())
    }
}