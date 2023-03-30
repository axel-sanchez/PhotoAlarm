package com.example.photoalarm.workmanager

import android.app.*
import android.app.ActivityManager.RunningAppProcessInfo
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
import com.example.photoalarm.ui.MainActivity


/**
 * @author Axel Sanchez
 */
@RequiresApi(Build.VERSION_CODES.P)
class AlarmWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    private lateinit var intentAlarmActivity: Intent
    private lateinit var intentMainActivity: Intent

    companion object{
        var r : Ringtone? = null
    }

    override fun doWork(): Result {
        val idAlarm = inputData.getLong("idAlarm", 0)
        intentAlarmActivity = Intent(applicationContext, AlarmActivity::class.java)
        intentMainActivity = Intent(applicationContext, MainActivity::class.java)
        showAlarmNotification(idAlarm)
        return Result.success()
    }

    private fun soundAlarm() {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        r = RingtoneManager.getRingtone(applicationContext, notification)
        r?.isLooping = true
        r?.play()
    }

    private fun isAppInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningProcesses = activityManager.runningAppProcesses
        if (runningProcesses != null) {
            val packageName = context.packageName
            for (processInfo in runningProcesses) {
                if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND && processInfo.processName.equals(packageName)) {
                    return true
                }
            }
        }
        return false
    }

    private fun showAlarmNotification(idAlarm: Long) {
        if(isAppInForeground(applicationContext)){
            intentAlarmActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(intentAlarmActivity)
        } else{
            soundAlarm()
            createChannel()
            val notification: Notification = createNotification(idAlarm)

            val notificationManager = NotificationManagerCompat.from(applicationContext)
            notificationManager.notify(idAlarm.toInt(), notification)
        }
    }

    private fun createNotification(idAlarm: Long): Notification {
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intentMainActivity, 0)

        return NotificationCompat.Builder(applicationContext, "channel01")
            .setSmallIcon(R.drawable.ic_alarm_24dp)
            .setContentTitle("Test $idAlarm")
            .setContentText("You see me!")
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
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