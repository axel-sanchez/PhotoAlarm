package com.example.photoalarm.workmanager

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.photoalarm.R
import com.example.photoalarm.ui.AlarmActivity

/**
 * @author Axel Sanchez
 */
@RequiresApi(Build.VERSION_CODES.P)
class AlarmWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    companion object{
        var r : Ringtone? = null
    }

    override fun doWork(): Result {
        val idAlarm = inputData.getLong("idAlarm", 0)
        showAlarmNotification(idAlarm)
        return Result.success()
    }

    private fun soundAlarm() {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        r = RingtoneManager.getRingtone(applicationContext, notification)
        r?.isLooping = true
        r?.play()
    }

    private fun showAlarmNotification(idAlarm: Long) {
        soundAlarm()
        createChannel()
        val notification: Notification = createNotification(idAlarm)

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(idAlarm.toInt(), notification)
    }

    private fun createNotification(idAlarm: Long): Notification {
        val intent = Intent(applicationContext, AlarmActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        return NotificationCompat.Builder(applicationContext, "channel01")
            .setSmallIcon(R.drawable.ic_alarm_24dp)
            .setContentTitle("Test $idAlarm")
            .setContentText("You see me!")
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // heads-up
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun createChannel() {
        val channel = NotificationChannel("channel01", "name", NotificationManager.IMPORTANCE_HIGH)
        channel.description = "description"

        val notificationManager: NotificationManager = applicationContext.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}