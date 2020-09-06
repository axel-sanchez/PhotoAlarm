package com.example.photoalarm.ui.view

import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.photoalarm.databinding.ActivityAlarmReceiverBinding

class AlarmActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAlarmReceiverBinding
    lateinit var r: Ringtone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmReceiverBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        sonarAlarma()

        binding.stop.setOnClickListener {
            r.stop()
            val activityIntent = Intent(this, MainActivity::class.java)
            startActivity(activityIntent)
        }
    }

    private fun sonarAlarma() {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        r = RingtoneManager.getRingtone(this, notification)
        r.isLooping = true
        r.play()
    }
}