package com.example.photoalarm.ui.view

import android.annotation.SuppressLint
import android.net.wifi.aware.WifiAwareNetworkInfo
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.photoalarm.R
import com.example.photoalarm.ui.view.customs.PhotoAlarmFragment
import com.ikovac.timepickerwithseconds.MyTimePickerDialog
import kotlinx.android.synthetic.main.fragment_timer.*
import java.util.*

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
    var miliSeconds = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
            }
        }

        stop.setOnClickListener {
            time.text = resources.getString(R.string.start_time)
            txtHour.text = "0"
            stop.visibility = View.GONE
            btnPlayPause.background = resources.getDrawable(R.drawable.ic_play_circle_24dp)
            isPlaying = false
            miliSeconds = 0
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
            MyTimePickerDialog.OnTimeSetListener { _, hourOfDay, minute, seconds -> // TODO Auto-generated method stub
                minutes = minute
                this.seconds = seconds
                var newTime = ""

                newTime += if(minutes<10) "0$minute:"
                else "$minute:"

                newTime += if(this.seconds<10) "0$seconds:"
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

    private fun sonarAlarma() {
        Toast.makeText(context, "Finalizó el tiempo", Toast.LENGTH_SHORT).show()
    }

    inner class ThreadTimer : Thread() {
        @SuppressLint("SetTextI18n")
        override fun run() {
            while (true) {
                if (isPlaying) {
                    try {
                        sleep(1)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (isPlaying) miliSeconds++
                    if (miliSeconds == 999) {
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
                            }
                        } else {
                            seconds--
                        }
                        miliSeconds = 0
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