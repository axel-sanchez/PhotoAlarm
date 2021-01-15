package com.example.photoalarm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.photoalarm.R
import com.example.photoalarm.common.hide
import com.example.photoalarm.common.show
import com.example.photoalarm.data.models.MyTime
import com.example.photoalarm.databinding.FragmentChronometerBinding
import com.example.photoalarm.viewmodel.ChronometerViewModel

class FirstFragment : Fragment() {

    private var milliSecondsString = ""
    private var secondsString = ""
    private var minutesString = ""

    private val viewModel: ChronometerViewModel by activityViewModels()

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

        setUpObserverViewModel()

    }

    private fun setUpObserverViewModel() {
        viewModel.getTimeLiveData().observe(viewLifecycleOwner, {
            it?.let { time ->
                activity?.runOnUiThread {
                    if(viewModel.isPlaying){
                        binding.btnPlayPause.background = resources.getDrawable(R.drawable.ic_pause_circle_24dp)
                        binding.stop.show()
                    } else{
                        binding.btnPlayPause.background = resources.getDrawable(R.drawable.ic_play_circle_24dp)
                    }
                    milliSecondsString = when {
                        time.milliSeconds == 0 -> "00"
                        time.milliSeconds < 10 -> "00${time.milliSeconds}"
                        time.milliSeconds < 100 -> "0${time.milliSeconds}"
                        else -> time.milliSeconds.toString()
                    }

                    secondsString = if (time.seconds < 10) "0${time.seconds}"
                    else time.seconds.toString()

                    minutesString = if (time.minutes < 10) "0${time.minutes}"
                    else time.minutes.toString()

                    binding.time.text = "$minutesString:$secondsString:$milliSecondsString"
                    binding.txtHour.text = time.hour.toString()
                }
            }
        })
    }

    private fun listenerStop() {
        binding.stop.setOnClickListener {
            binding.time.text = resources.getString(R.string.start_time)
            binding.stop.hide()
            binding.btnPlayPause.background = resources.getDrawable(R.drawable.ic_play_circle_24dp)
            viewModel.isPlaying = false
            viewModel.time = MyTime(0, 0, 0, 0)
        }
    }

    private fun listenerPlayPause() {
        binding.btnPlayPause.setOnClickListener {
            try {
                if (viewModel.isPlaying) {
                    binding.btnPlayPause.background = resources.getDrawable(R.drawable.ic_play_circle_24dp)
                    viewModel.isPlaying = false
                } else {
                    binding.btnPlayPause.background = resources.getDrawable(R.drawable.ic_pause_circle_24dp)
                    viewModel.isPlaying = true
                    binding.stop.show()
                    viewModel.getTime()
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}