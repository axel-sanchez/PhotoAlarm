package com.example.photoalarm.ui

import android.os.Build
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.photoalarm.R
import com.example.photoalarm.databinding.FragmentNavigationBinding
import com.example.photoalarm.ui.customs.PhotoAlarmFragment

@RequiresApi(Build.VERSION_CODES.KITKAT)
class MainFragment: PhotoAlarmFragment() {

    private val firstItem = R.id.alarm
    private var itemSelected = firstItem

    override fun onBackPressFragment()= false

    private var fragmentNavigationBinding: FragmentNavigationBinding? = null
    private val binding get() = fragmentNavigationBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentNavigationBinding = FragmentNavigationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentNavigationBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navegador.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.clock -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.weather -> {
                itemSelected = item.itemId
                replaceTo(WeatherFragment(), false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.alarm -> {
                itemSelected = item.itemId
                replaceTo(AlarmFragment(), false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.timer -> {
                itemSelected = item.itemId
                replaceTo(TimerFragment(), false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.chronometer -> {
                itemSelected = item.itemId
                replaceTo(ChronometerFragment(), false)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun replaceTo(fragment: Fragment, addToBackstack: Boolean) {

        val transaction = childFragmentManager
            .beginTransaction()
            .replace(R.id.contenedor, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        binding.navegador.selectedItemId = itemSelected
    }
}