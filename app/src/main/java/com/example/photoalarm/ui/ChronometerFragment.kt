package com.example.photoalarm.ui

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.photoalarm.R
import com.example.photoalarm.common.hide
import com.example.photoalarm.common.show
import com.example.photoalarm.databinding.FragmentChronometerBinding

class ChronometerFragment : Fragment() {

    private var running = false
    private var pauseOffset = 0L

    private var fragmentChronometerBinding: FragmentChronometerBinding? = null
    private val binding get() = fragmentChronometerBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentChronometerBinding = FragmentChronometerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentChronometerBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listenerPlayPause()
        listenerStop()

        binding.time.base = SystemClock.elapsedRealtime()
    }

    private fun startChronometer() {
        if (!running) {
            binding.time.base = SystemClock.elapsedRealtime() - pauseOffset
            binding.time.start()
            running = true
        }
    }

    private fun pauseChronometer() {
        if (running) {
            binding.time.stop()
            pauseOffset = SystemClock.elapsedRealtime() - binding.time.base
            running = false
        }
    }

    private fun resetChronometer() {
        binding.time.base = SystemClock.elapsedRealtime()
        pauseOffset = 0L
    }

    private fun listenerStop() {
        binding.stop.setOnClickListener {
            binding.stop.hide()
            binding.btnPlayPause.background = resources.getDrawable(R.drawable.ic_play_circle_24dp)
            pauseChronometer()
            resetChronometer()
            running = false
        }
    }

    private fun listenerPlayPause() {
        binding.btnPlayPause.setOnClickListener {
            try {
                if (running) {
                    binding.btnPlayPause.background = resources.getDrawable(R.drawable.ic_play_circle_24dp)
                    pauseChronometer()
                } else {
                    binding.btnPlayPause.background = resources.getDrawable(R.drawable.ic_pause_circle_24dp)
                    binding.stop.show()
                    startChronometer()
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}