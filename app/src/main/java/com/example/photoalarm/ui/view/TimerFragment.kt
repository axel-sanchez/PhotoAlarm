package com.example.photoalarm.ui.view

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.photoalarm.R
import com.example.photoalarm.ui.view.customs.PhotoAlarmFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.ikovac.timepickerwithseconds.MyTimePickerDialog
import kotlinx.android.synthetic.main.fragment_timer.*

class TimerFragment : PhotoAlarmFragment() {

    override fun onBackPressFragment() = false

    private lateinit var btnPlayPause: ImageButton
    private lateinit var progress: ProgressBar
    private lateinit var time: TextView
    private lateinit var txtHour: TextView

    private lateinit var threadPlay: ThreadTimer
    var handler = Handler()

    var isPlaying = false
    var seconds = 0
    var minutes = 0
    var miliSeconds = 999

    var alert: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnPlayPause = view.findViewById(R.id.btnPlayPause)
        progress = view.findViewById(R.id.progress)
        time = view.findViewById(R.id.time)
        txtHour = view.findViewById(R.id.txtHour)

        btnPlayPause.setOnClickListener {
            if (isPlaying) {
                btnPlayPause.background = resources.getDrawable(R.drawable.ic_play_circle_24dp)
                isPlaying = false
            } else {
                btnPlayPause.background = resources.getDrawable(R.drawable.ic_pause_circle_24dp)
                isPlaying = true
                stop.visibility = View.VISIBLE
                seconds--
            }
        }

        stop.setOnClickListener {
            time.text = resources.getString(R.string.start_time)
            txtHour.text = "0"
            stop.visibility = View.GONE
            btnPlayPause.background = resources.getDrawable(R.drawable.ic_play_circle_24dp)
            isPlaying = false
            miliSeconds = 999
            minutes = 0
            seconds = 0
        }

        time.setOnClickListener { selectTime() }
        linearHora.setOnClickListener { selectTime() }
        progress.setOnClickListener { selectTime() }

        threadPlay = ThreadTimer()
        threadPlay.start()
    }

    @SuppressLint("SetTextI18n")
    private fun selectTime() {
        val mTimePicker = MyTimePickerDialog(
            context,
            MyTimePickerDialog.OnTimeSetListener { _, hourOfDay, minute, seconds ->
                minutes = minute
                this.seconds = seconds
                var newTime = ""

                newTime += if (minutes < 10) "0$minute:"
                else "$minute:"

                newTime += if (this.seconds < 10) "0$seconds:"
                else "$seconds:"

                newTime += "00"

                time.text = newTime
                txtHour.text = hourOfDay.toString()
            },
            0,
            0,
            0,
            true
        )
        mTimePicker.show()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun sonarAlarma() {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val r = RingtoneManager.getRingtone(context, notification)
        r.isLooping = true
        r.play()

        activity!!.runOnUiThread {
            Snackbar.make(view!!, "Timer Over", Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.RED)
                .setAction("OK") { r.stop() }
                .show()
        }
    }

    inner class ThreadTimer : Thread() {
        @RequiresApi(Build.VERSION_CODES.P)
        @SuppressLint("SetTextI18n")
        override fun run() {
            while (true) {
                if (isPlaying) {
                    try {
                        sleep(1)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (isPlaying) miliSeconds--
                    if (miliSeconds == 0) {
                        if (seconds == 0) {
                            if (minutes != 0) {
                                minutes--
                                seconds = 59
                            } else {
                                isPlaying = false
                                seconds = 0
                                btnPlayPause.background = resources.getDrawable(R.drawable.ic_play_circle_24dp)
                                stop.visibility = View.GONE
                                sonarAlarma()
                                return
                            }
                        } else {
                            seconds--
                        }
                        miliSeconds = 999
                    }

                    handler.post {
                        Runnable {
                            var m = ""
                            var s = ""
                            var mi = ""

                            m = when {
                                miliSeconds == 0 -> "00"
                                miliSeconds < 10 -> "00$miliSeconds"
                                miliSeconds < 100 -> "0$miliSeconds"
                                else -> miliSeconds.toString()
                            }

                            s = if (seconds < 10) "0$seconds"
                            else seconds.toString()

                            mi = if (minutes < 10) "0$minutes"
                            else minutes.toString()

                            time.text = "$mi:$s:$m"
                        }.run()
                    }
                }
            }
        }
    }
}