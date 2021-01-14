package com.example.photoalarm.workmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.photoalarm.R


/**
 * @author Axel Sanchez
 */
@RequiresApi(Build.VERSION_CODES.O)
class AlarmWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {

        println("LLegó el worker")

        val idAlarm = inputData.getLong("idAlarm", 0)

        openAlarmInNewActivity(idAlarm)

        return Result.success()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "1"
            val descriptionText = "Notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            val channel = NotificationChannel("1", name, importance).apply {
                description = descriptionText
                enableVibration(true)
                setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), att)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun openAlarmInNewActivity(idAlarm: Long) {

        /*createNotificationChannel()

        var notification = NotificationCompat.Builder(applicationContext, "1")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Alarma")
            .setContentText("Se activó una alarma")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
 //           .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setTicker("Nueva notificación")
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            //.setCustomHeadsUpContentView()
            .build()

        val nm: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(1, notification)*/





        channel()

        val notification: Notification = NotificationCompat.Builder(applicationContext, "channel01")
            .setSmallIcon(R.drawable.ic_alarm_24dp)
            .setContentTitle("Test $idAlarm")
            .setContentText("You see me!")
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // heads-up
            .setCustomHeadsUpContentView(RemoteViews(applicationContext.packageName, R.layout.notification_small))
            .build()

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(0, notification)





/*        with(NotificationManagerCompat.from(applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, notification.build())
        }
*/
        println("Sonó la alarma")
        /*val intentAlarm = Intent(applicationContext, AlarmActivity::class.java)
        intentAlarm.putExtra("idAlarm", idAlarm)
        intentAlarm.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        applicationContext.startActivity(intentAlarm)*/
    }

    private fun channel() {
        val channel = NotificationChannel(
            "channel01", "name",
            NotificationManager.IMPORTANCE_HIGH
        ) // for heads-up notifications

        channel.description = "description"

        // Register channel with system
        val notificationManager: NotificationManager =
            applicationContext.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}