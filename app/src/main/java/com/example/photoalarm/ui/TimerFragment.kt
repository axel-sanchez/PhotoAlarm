package com.example.photoalarm.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.photoalarm.R
import com.example.photoalarm.common.hide
import com.example.photoalarm.common.show
import com.example.photoalarm.databinding.FragmentTimerBinding
import com.google.android.material.snackbar.Snackbar
import com.ikovac.timepickerwithseconds.MyTimePickerDialog

class TimerFragment : Fragment() {

    private lateinit var threadPlay: ThreadTimer
    var handler = Handler()

    var isPlaying = false
    var seconds = 0
    var minutes = 0
    var milliSeconds = 999

    private var fragmentTimerBinding: FragmentTimerBinding? = null
    private val binding get() = fragmentTimerBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentTimerBinding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentTimerBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPlayPause.setOnClickListener {
            if (isPlaying) {
                binding.btnPlayPause.background = resources.getDrawable(R.drawable.ic_play_circle_24dp)
                isPlaying = false
            } else {
                binding.btnPlayPause.background = resources.getDrawable(R.drawable.ic_pause_circle_24dp)
                isPlaying = true
                binding.stop.show()
                seconds--
            }
        }

        binding.stop.setOnClickListener {
            binding.time.text = resources.getString(R.string.start_time)
            binding.txtHour.text = "0"
            binding.stop.hide()
            binding.btnPlayPause.background = resources.getDrawable(R.drawable.ic_play_circle_24dp)
            isPlaying = false
            milliSeconds = 999
            minutes = 0
            seconds = 0
        }

        binding.time.setOnClickListener { selectTime() }
        binding.linearHora.setOnClickListener { selectTime() }
        binding.progress.setOnClickListener { selectTime() }

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

                binding.time.text = newTime
                binding.txtHour.text = hourOfDay.toString()
            },
            0,
            0,
            0,
            true
        )
        mTimePicker.show()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun sonarAlarm() {
        val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val r = RingtoneManager.getRingtone(context, notification)
        r.isLooping = true
        r.play()

        activity?.runOnUiThread {
            Snackbar.make(view!!, getString(R.string.time_over), Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.RED)
                .setAction(getString(R.string.ok)) { r.stop() }
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
                    if (isPlaying) milliSeconds--
                    if (milliSeconds == 0) {
                        if (seconds == 0) {
                            if (minutes != 0) {
                                minutes--
                                seconds = 59
                            } else {
                                isPlaying = false
                                seconds = 0
                                binding.btnPlayPause.background = resources.getDrawable(R.drawable.ic_play_circle_24dp)
                                binding.stop.hide()
                                sonarAlarm()
                                return
                            }
                        } else {
                            seconds--
                        }
                        milliSeconds = 999
                    }

                    handler.post {
                        Runnable {
                            var m = ""
                            var s = ""
                            var mi = ""

                            m = when {
                                milliSeconds == 0 -> "00"
                                milliSeconds < 10 -> "00$milliSeconds"
                                milliSeconds < 100 -> "0$milliSeconds"
                                else -> milliSeconds.toString()
                            }

                            s = if (seconds < 10) "0$seconds"
                            else seconds.toString()

                            mi = if (minutes < 10) "0$minutes"
                            else minutes.toString()

                            binding.time.text = "$mi:$s:$m"
                        }.run()
                    }
                }
            }
        }
    }
}