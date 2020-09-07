package com.example.photoalarm.helpers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.photoalarm.ui.AlarmActivity

@RequiresApi(Build.VERSION_CODES.P)
class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Sonó la alarma", Toast.LENGTH_SHORT).show()
        val intentAlarm = Intent(context, AlarmActivity::class.java)
        intentAlarm.putExtra("idAlarm", intent?.getIntExtra("idAlarm", 0))
        intentAlarm.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context!!.startActivity(intentAlarm)
    }
}