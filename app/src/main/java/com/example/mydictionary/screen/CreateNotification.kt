package com.example.mydictionary.screen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import com.example.mydictionary.R
import kotlin.random.Random

fun createNotification(context: Context, note: String){
    //var notificationManager: NotificationManager = LocalContext.current.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("TA", "LearnEnglish", importance).apply {
            description = null
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    val builder = NotificationCompat.Builder(context, "TA")
        .setSmallIcon(R.drawable.pending)
        .setContentTitle("My notification")
        .setContentText(note)
        .setStyle(NotificationCompat.BigTextStyle()
        .bigText(note))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
    val notificationManager= context.getSystemService(NotificationManager::class.java)
    notificationManager.notify(
        Random.nextInt(),
        builder
    )
}