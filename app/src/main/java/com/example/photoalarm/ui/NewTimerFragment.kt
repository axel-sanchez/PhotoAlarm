package com.example.photoalarm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.photoalarm.databinding.FragmentNewTimerBinding

/**
 * The first [Fragment].
 * @author Axel Sanchez
 */
class NewTimerFragment : Fragment() {

    private var fragmentNewTimerBinding: FragmentNewTimerBinding? = null
    private val binding get() = fragmentNewTimerBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentNewTimerBinding = FragmentNewTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentNewTimerBinding = null
    }

}