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
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.photoalarm.R
import com.example.photoalarm.common.hide
import com.example.photoalarm.common.show
import com.example.photoalarm.data.models.Alarm
import com.example.photoalarm.data.models.AlarmXDay
import com.example.photoalarm.data.models.Day
import com.example.photoalarm.data.room.Database
import com.example.photoalarm.databinding.FragmentAlarmBinding
import com.example.photoalarm.helpers.AlarmHelper
import com.example.photoalarm.ui.adapter.AlarmAdapter
import com.example.photoalarm.workmanager.AlarmWorker
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.time.Duration
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class AlarmFragment : Fragment() {

    private val days: List<Day> by inject()
    private val calendar: Calendar = Calendar.getInstance()
    private val alarmHelper: AlarmHelper by inject()
    private val room: Database by inject()

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    private var hour: Int = 0
    private var minute: Int = 0

    private lateinit var alarm: Alarm

    private lateinit var vibe: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vibe = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

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

        lifecycleScope.launch {
            setAdapter(room.productDao().getAllAlarms().toMutableList())
        }

    }

    private fun addAlarmFast() {
        val timePickerDialog = TimePickerDialog(
            requireView().context,
            R.style.TimePickerTheme,
            { _, hourOfDay, minute -> //Formateo el hora obtenido: antepone el 0 si son menores de 10
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

                lifecycleScope.launch {
                    alarm.id = room.productDao().insertAlarm(alarm)
                }

                for (day in alarm.days) {
                    var idDay = days.find { dia -> dia.name == day }?.id?.toInt()?: kotlin.run { null }
                    idDay?.let {
                        lifecycleScope.launch {
                            //Insertamos un dia x alarma
                            room.productDao().insertAlarmXDay(AlarmXDay(0, alarm.id,it))
                        }
                    }
                }

                (viewAdapter as AlarmAdapter).add(alarm)

                binding.emptyState.hide()

                WorkManager.getInstance(requireContext()).enqueue(buildAlarmWorker(alarm.id))

                //alarmHelper.activateAlarm(hourOfDay, minute, context, alarm.id.toInt())

                //txtTime.text = "$horaFormateada:$minutoFormateado $periodo"
            }, //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
            hour, minute, false
        )
        timePickerDialog.window!!.setBackgroundDrawable(requireView().resources.getDrawable(R.drawable.corners_qk))
        timePickerDialog.show()
    }

    private fun buildAlarmWorker(idAlarm: Long) =
        OneTimeWorkRequestBuilder<AlarmWorker>()
        .setInitialDelay(Duration.ofSeconds(2))
        .setInputData(workDataOf("idAlarm" to idAlarm))
        .build()

    private fun setAdapter(alarms: MutableList<Alarm>) {
        if (alarms.isEmpty()) binding.emptyState.show()

        viewAdapter = AlarmAdapter(alarms, { delete(it) }, { vibrate() }, room, lifecycleScope)

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
        lifecycleScope.launch {
            room.productDao().deleteAlarmXDay(item.id)
            room.productDao().deleteAlarm(item.id)
        }
    }

    private fun showEmptyState() {
        if (viewAdapter.itemCount > 0) {
            binding.emptyState.hide()
        } else {
            binding.emptyState.show()
        }
    }

    private fun vibrate() {
        vibe.vibrate(150)
    }
}