package com.example.photoalarm.ui.view

import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photoalarm.R
import com.example.photoalarm.data.models.Alarm
import com.example.photoalarm.ui.view.adapter.AlarmAdapter
import com.example.photoalarm.ui.view.customs.PhotoAlarmFragment

class AlarmFragment : PhotoAlarmFragment() {

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var rvAlarms: RecyclerView

    private lateinit var vibe: Vibrator

    override fun onBackPressFragment() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvAlarms = view.findViewById(R.id.rvAlarms)

        setAdapter(mutableListOf(Alarm(), Alarm(), Alarm(), Alarm(), Alarm(), Alarm()))
    }

    private fun setAdapter(alarms: MutableList<Alarm>){
        viewAdapter = AlarmAdapter(alarms, { vibrate() })

        viewManager = LinearLayoutManager(this.requireContext())

        rvAlarms.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    private fun vibrate(){
        vibe.vibrate(50)
    }
}