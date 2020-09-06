package com.example.photoalarm.ui.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
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
import com.example.photoalarm.data.TableDay
import com.example.photoalarm.data.models.Alarm
import com.example.photoalarm.data.repository.GenericRepository
import com.example.photoalarm.databinding.FragmentAlarmBinding
import com.example.photoalarm.ui.view.adapter.AlarmAdapter
import com.example.photoalarm.ui.view.customs.PhotoAlarmFragment
import java.util.*

@RequiresApi(Build.VERSION_CODES.KITKAT)
class AlarmFragment : PhotoAlarmFragment() {

    private lateinit var repository: GenericRepository
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>

    private lateinit var calendar: Calendar
    private var hour: Int = 0
    private var minute: Int = 0

    private lateinit var alarm: Alarm

    private lateinit var vibe: Vibrator

    override fun onBackPressFragment() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vibe = context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        calendar = Calendar.getInstance()
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

        repository = GenericRepository.getInstance(context!!)

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
                    mutableListOf(getCurrentDay()),
                    isActive = true,
                    requireVibrate = true
                )

                alarm.id = repository.insert(alarm)

                for (d in alarm.days) {
                    //Insertamos un dia x alarma
                    repository.insert(
                        alarm.id, repository.getDays(
                            arrayOf(TableDay.Columns.COLUMN_NAME_NAME), arrayOf(
                                d
                            ), null
                        ).first().id.toInt()
                    )
                }

                (viewAdapter as AlarmAdapter).add(alarm)

                binding.emptyState.showView(false)

                activateAlarm(hourOfDay, minute)

                //txtTime.text = "$horaFormateada:$minutoFormateado $periodo"
            }, //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
            hour, minute, false
        )
        timePickerDialog.window!!.setBackgroundDrawable(view!!.resources.getDrawable(R.drawable.corners_qk))
        timePickerDialog.show()
    }

    private fun activateAlarm(hourAlarm: Int, minuteAlarm: Int){
        val manager = context?.getSystemService(ALARM_SERVICE) as AlarmManager?
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)
        val innerCalendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hourAlarm)
            set(Calendar.MINUTE, minuteAlarm)
        }
        manager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            //System.currentTimeMillis(),
            innerCalendar.timeInMillis,
            1000 * 60 * 10,
            pendingIntent
        )
    }

    private fun getCurrentDay(): String {
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Lunes"
            Calendar.TUESDAY -> "Martes"
            Calendar.WEDNESDAY -> "Miércoles"
            Calendar.THURSDAY -> "Jueves"
            Calendar.FRIDAY -> "Viernes"
            Calendar.SATURDAY -> "Sábado"
            Calendar.SUNDAY -> "Domingo"
            else -> "Lunes"
        }
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