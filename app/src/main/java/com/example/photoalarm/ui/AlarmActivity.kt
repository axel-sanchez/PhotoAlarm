package com.example.photoalarm.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.photoalarm.data.models.Alarm
import com.example.photoalarm.databinding.ActivityAlarmReceiverBinding

@RequiresApi(Build.VERSION_CODES.P)
class AlarmActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAlarmReceiverBinding
    lateinit var r: Ringtone

    var idAlarm = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlarmReceiverBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        idAlarm = intent.extras?.let { it.getInt("idAlarm", 0)}?.let { it }?:0

        binding.label.text = "${binding.label.text} $idAlarm"

        soundAlarm()

        binding.stop.setOnClickListener {
            r.stop()
            disableAlarm()
            val activityIntent = Intent(this, MainActivity::class.java)
            startActivity(activityIntent)
        }

        binding.postergar.setOnClickListener {
            r.stop()
            val activityIntent = Intent(this, MainActivity::class.java)
            startActivity(activityIntent)
        }
    }

    private fun disableAlarm() {
        val intent = Intent(this, Alarm::class.java)
        val sender = PendingIntent.getBroadcast(this, 0, intent, 0)
        (getSystemService(Context.ALARM_SERVICE) as AlarmManager?)?.cancel(sender)
    }

    private fun soundAlarm() {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        r = RingtoneManager.getRingtone(this, notification)
        r.isLooping = true
        r.play()
    }
}