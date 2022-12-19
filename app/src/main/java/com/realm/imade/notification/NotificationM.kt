package com.realm.imade.notification


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.realm.imade.NotificationActivity
import com.realm.imade.R
import java.io.IOException
import java.io.InputStream
import java.net.URL


object NotificationM {


    fun Context.createChannel(int: Int) {
        val vibrate = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        val managerN = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            managerN.createNotificationChannel(
                NotificationChannel(
                    "image", "New Image", int
                ).apply {
                    enableLights(true)
                    lightColor = Color.RED
                    enableVibration(true)
                    vibrationPattern = vibrate
                })
        }
    }

    fun Context.showNotification(title: String?, body: String?) {
        var bmp: Bitmap? = null
        try {
            val ins: InputStream =
                URL("https://tourofhonor.com/appimages/2019fl4.jpg").openStream()
            bmp = BitmapFactory.decodeStream(ins)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, "image")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.backgroud)
                .setContentTitle(title)
                .setContentText(body)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setLargeIcon(bmp)
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bmp)
                        .bigLargeIcon(null)
                )
        val intent = Intent(this, NotificationActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("notificationUrl", "https://www.tourofhonor.com/appimages/2019fl4.jpg")
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        mBuilder.setContentIntent(pendingIntent)
        notificationManager.notify(1, mBuilder.build())

    }

}