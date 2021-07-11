package com.example.todo.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.todo.MainActivity
import com.example.todo.MainActivity.Companion.appContext
import java.util.*
import java.util.concurrent.TimeUnit

class Notification {
    private val context: Context = appContext
    private val manager = Context.NOTIFICATION_SERVICE as NotificationManager
    lateinit var workManager: WorkManager



    class DailyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
        override fun doWork(): Result {
            val currentDate: Calendar = Calendar.getInstance()
            val dueDate: Calendar = Calendar.getInstance()
            dueDate.set(Calendar.HOUR_OF_DAY, 8)
            dueDate.set(Calendar.MINUTE, 0)
            dueDate.set(Calendar.SECOND, 0)

            if (dueDate.before(currentDate)) {
                dueDate.add(Calendar.HOUR_OF_DAY, 24)
            }
            val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

            val request = OneTimeWorkRequestBuilder<DailyWorker>()
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance(applicationContext).enqueue(request)
            return Result.success()
        }

    }


        fun sendNotification(taskAmount: Int) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(MainActivity.CHANNEL_ID, "ToDo", importance)
        channel.description = "На сегодня есть задачи: $taskAmount шт. Не забудьте"
        channel.enableLights(true)
        channel.lightColor = Color.BLUE
        manager.createNotificationChannel(channel)
        val resultIntent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
            .setContentTitle("ToDo")
            .setContentText("На сегодня есть задачи: $taskAmount шт. Не забудьте")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setChannelId(MainActivity.CHANNEL_ID)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        manager.notify(MainActivity.NOTIFICATION_ID, notification)
    }
}