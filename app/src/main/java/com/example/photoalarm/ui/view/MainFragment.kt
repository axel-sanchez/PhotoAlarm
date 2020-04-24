package com.example.photoalarm.ui.view

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.photoalarm.R
import com.example.photoalarm.ui.view.customs.PhotoAlarmFragment
import com.example.photoalarm.ui.view.interfaces.INavigationHost
import kotlinx.android.synthetic.main.fragment_navigation.*

const val ARG_ITEM = "nombre"

class MainFragment: PhotoAlarmFragment() {

    private val firstItem = R.id.alarm
    private var itemSelected = firstItem

    override fun onBackPressFragment()= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //todo: Por el momento no tengo un toolbar
        // (activity as AppCompatActivity).setSupportActionBar(toolbar)
        // (activity as AppCompatActivity).supportActionBar?.apply {
        //  setHomeButtonEnabled(true)
        //  setDisplayHomeAsUpEnabled(true)
        //  setDisplayShowTitleEnabled(false)}

        navegador.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.clock -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.weather -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.alarm -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.timer -> {
                itemSelected = item.itemId
                navigateTo(TimerFragment(), false)
                return@OnNavigationItemSelectedListener true
            }
            R.id.chronometer -> {
                itemSelected = item.itemId
                navigateTo(ChronometerFragment(), false)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {

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
        navegador.selectedItemId = itemSelected
    }
}