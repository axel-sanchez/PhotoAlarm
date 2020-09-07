package com.example.photoalarm.ui

import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photoalarm.R
import com.example.photoalarm.data.models.Alarm
import com.example.photoalarm.data.models.Day
import com.example.photoalarm.data.repository.GenericRepository
import com.example.photoalarm.databinding.FragmentAlarmBinding
import com.example.photoalarm.helpers.AlarmHelper
import com.example.photoalarm.ui.adapter.AlarmAdapter
import com.example.photoalarm.ui.customs.PhotoAlarmFragment
import org.koin.android.ext.android.inject
import java.util.*

@RequiresApi(Build.VERSION_CODES.M)
class AlarmFragment : PhotoAlarmFragment() {

    private val days: List<Day> by inject()
    private val calendar: Calendar by inject()
    private val alarmHelper: AlarmHelper by inject()
    private val repository: GenericRepository by inject()

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    private var hour: Int = 0
    private var minute: Int = 0

    private lateinit var alarm: Alarm

    private lateinit var vibe: Vibrator

    override fun onBackPressFragment() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vibe = context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        hour = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)
    }

    private var fragmentAlarmBinding: FragmentAlarmBinding? = null
    private val binding get() = fragmentAlarmBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentAlarmBinding = FragmentAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentAlarmBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFast.setOnClickListener {
            binding.btnMenu.collapse()
            addAlarmFast()
        }

        setAdapter(repository.getAlarms(arrayOf(), arrayOf(), null))
    }

    private fun addAlarmFast() {
        val timePickerDialog = TimePickerDialog(
            view!!.context,
            R.style.TimePickerTheme,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute -> //Formateo el hora obtenido: antepone el 0 si son menores de 10
                val horaFormateada =
                    if (hourOfDay < 10) java.lang.String.valueOf("0$hourOfDay") else hourOfDay.toString()
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                val minutoFormateado =
                    if (minute < 10) java.lang.String.valueOf("0$minute") else minute.toString()
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                val periodo: String = if (hourOfDay < 12) {
                    "a.m."
                } else {
                    "p.m."
                }

                //Muestro la hora con el formato deseado
                alarm = Alarm(
                    0,
                    "FastAlarm",
                    "$horaFormateada:$minutoFormateado $periodo",
                    "default",
                    mutableListOf(alarmHelper.getCurrentDay()),
                    isActive = true,
                    requireVibrate = true
                )

                alarm.id = repository.insert(alarm)

                for (day in alarm.days) {
                    var idDay = days.find { dia -> dia.name == day }?.id?.toInt()?: kotlin.run { null }
                    idDay?.let {
                        //Insertamos un dia x alarma
                        repository.insert(
                            alarm.id,
                            it
                        )
                    }
                }

                (viewAdapter as AlarmAdapter).add(alarm)

                binding.emptyState.showView(false)

                alarmHelper.activateAlarm(hourOfDay, minute, context)

                //txtTime.text = "$horaFormateada:$minutoFormateado $periodo"
            }, //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
            hour, minute, false
        )
        timePickerDialog.window!!.setBackgroundDrawable(view!!.resources.getDrawable(R.drawable.corners_qk))
        timePickerDialog.show()
    }

    private fun setAdapter(alarms: MutableList<Alarm>) {
        if (alarms.isEmpty()) binding.emptyState.showView(true)

        viewAdapter = AlarmAdapter(alarms, { delete(it) }, { vibrate() })

        viewManager = LinearLayoutManager(this.requireContext())

        binding.rvAlarms.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    private fun delete(item: Alarm) {
        (viewAdapter as AlarmAdapter).remove(item)
        showEmptyState()
        repository.deleteDayXAlarm(item.id)
        repository.deleteAlarm(item.id)
    }

    private fun showEmptyState() {
        if (viewAdapter.itemCount > 0) {
            binding.emptyState.showView(false)
        } else {
            binding.emptyState.showView(true)
        }
    }

    private fun vibrate() {
        vibe.vibrate(150)
    }
}