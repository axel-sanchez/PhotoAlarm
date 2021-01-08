package com.example.photoalarm.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.photoalarm.R
import com.example.photoalarm.common.hide
import com.example.photoalarm.common.show
import com.example.photoalarm.databinding.FragmentChronometerBinding

class ChronometerFragment : Fragment() {

    private lateinit var threadPlay: ThreadChronometer
    var handler = Handler()

    var isPlaying = false
    var hour = 0
    var minutes = 0
    var seconds = 0
    var miliSeconds = 0

    private var fragmentChronometerBinding: FragmentChronometerBinding? = null
    private val binding get() = fragmentChronometerBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentChronometerBinding = FragmentChronometerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentChronometerBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        threadPlay = ThreadChronometer()
        threadPlay.start()

        binding.btnPlayPause.setOnClickListener {
            if (isPlaying) {
                binding.btnPlayPause.background = resources.getDrawable(R.drawable.ic_play_circle_24dp)
                isPlaying = false
            } else {
                binding.btnPlayPause.background = resources.getDrawable(R.drawable.ic_pause_circle_24dp)
                isPlaying = true
                binding.stop.show()
            }
        }

        binding.stop.setOnClickListener {
            binding.time.text = resources.getString(R.string.start_time)
            binding.stop.hide()
            binding.btnPlayPause.background = resources.getDrawable(R.drawable.ic_play_circle_24dp)
            isPlaying = false
            miliSeconds = 0
            minutes = 0
            seconds = 0
            hour = 0
        }
    }

    inner class ThreadChronometer : Thread() {
        @SuppressLint("SetTextI18n")
        override fun run() {
            while (true) {
                if (isPlaying) {
                    try {
                        sleep(1)
                    } catch (e: Exception) { e.printStackTrace() }
                    if(isPlaying) miliSeconds++
                    if (miliSeconds == 999) {
                        seconds++
                        miliSeconds = 0
                    }
                    if (seconds == 60) {
                        minutes++
                        seconds = 0
                    }

                    if(minutes == 60){
                        hour++
                        minutes = 0
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

                            s = if(seconds < 10) "0$seconds"
                            else seconds.toString()

                            mi = if(minutes < 10) "0$minutes"
                            else minutes.toString()

                            binding.time.text = "$mi:$s:$m"
                            binding.txtHour.text = hour.toString()
                        }.run()
                    }
                }
            }
        }
    }
}