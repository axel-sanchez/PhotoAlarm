package com.example.photoalarm.ui.adapter

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Build
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.photoalarm.R
import com.example.photoalarm.data.models.Alarm
import com.example.photoalarm.data.room.Database
import com.example.photoalarm.databinding.ItemAlarmBinding
import com.example.photoalarm.helpers.AlarmHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*

@RequiresApi(Build.VERSION_CODES.M)
class AlarmAdapter(
    private val listData: MutableList<Alarm>,
    private val delete: (Alarm) -> Unit,
    private val vibrate: () -> Unit,
    private val room: Database,
    private val scope: LifecycleCoroutineScope
) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>(), KoinComponent {

    inner class ViewHolder(private val binding: ItemAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val calendar: Calendar by inject()
        private val alarmHelper: AlarmHelper by inject()

        private var isSelected = false

        private val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        private val minute: Int = calendar.get(Calendar.MINUTE)

        fun bind(alarm: Alarm, vibrate: () -> Unit, delete: (Alarm) -> Unit) {

            val alarmHour = alarm.time.substring(0, 2)
            val alarmMinute = alarm.time.substring(3, 5)

            binding.timeRest.text = AlarmHelper.getTimeRest(alarmHour.toInt(), alarmMinute.toInt())

            binding.colapsar.setOnClickListener {
                if (binding.expandableView.isShown) {
                    TransitionManager.endTransitions(binding.cardView)
                    binding.expandableView.visibility = View.GONE
                    binding.colapsar.setBackgroundResource(R.drawable.ic_expand)
                } else {
                    TransitionManager.beginDelayedTransition(binding.cardView)
                    binding.expandableView.visibility = View.VISIBLE
                    binding.colapsar.setBackgroundResource(R.drawable.ic_collapsed)
                }
            }

            binding.txtTime.text = alarm.time

            var stringDays = ""

            for (day in alarm.days) {
                stringDays += day
            }

            binding.txtDays.text = stringDays

            if (alarm.isActive) binding.switchActivate.isChecked = true

            binding.switchActivate.setOnClickListener {
                if (binding.switchActivate.isChecked) {
                    alarm.isActive = true
                    scope.launch {
                        room.productDao().updateAlarm(alarm)
                    }
                } else {
                    alarm.isActive = false
                    scope.launch {
                        room.productDao().updateAlarm(alarm)
                    }
                }
            }

            itemView.setOnClickListener {
                if (isSelected) {
                    binding.hide.visibility = View.GONE
                    binding.btnDelete.visibility = View.GONE
                    binding.switchActivate.visibility = View.VISIBLE
                    isSelected = false
                } else {
                    Toast.makeText(
                        itemView.context,
                        "Tengo que lograr como un tipo de zoom con motionLayout",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            itemView.setOnLongClickListener {
                if (!isSelected) {
                    vibrate()
                    isSelected = true
                    binding.hide.visibility = View.VISIBLE
                    binding.btnDelete.visibility = View.VISIBLE
                    binding.switchActivate.visibility = View.GONE

                    binding.btnDelete.setOnClickListener {
                        delete(alarm)
                    }
                } else {
                    binding.hide.visibility = View.GONE
                    binding.btnDelete.visibility = View.GONE
                    binding.switchActivate.visibility = View.VISIBLE
                    isSelected = false
                }

                true
            }

            binding.txtTime.setOnClickListener { selectTime() }
        }

        @SuppressLint("SetTextI18n")
        private fun selectTime() {
            val timePickerDialog = TimePickerDialog(
                itemView.context,
                R.style.TimePickerTheme,
                OnTimeSetListener { _, hourOfDay, minute -> //Formateo el hora obtenido: antepone el 0 si son menores de 10
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
                    binding.txtTime.text = "$horaFormateada:$minutoFormateado $periodo"
                }, //Estos valores deben ir en ese orden
                //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                //Pero el sistema devuelve la hora en formato 24 horas
                hour, minute, false
            )
            timePickerDialog.window!!.setBackgroundDrawable(itemView.resources.getDrawable(R.drawable.corners_qk))
            timePickerDialog.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val recyclerRowBinding: ItemAlarmBinding =
            ItemAlarmBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(recyclerRowBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listData[position], vibrate, delete)
    }

    override fun getItemCount() = listData.size

    fun remove(item: Alarm) {
        val position = listData.indexOf(listData.find { x -> x.id == item.id })
        listData.removeAt(position)
        notifyItemRemoved(position)
    }

    fun update(item: Alarm) {
        val position = listData.indexOf(listData.find { x -> x.id == item.id })
        listData[position] = item
        notifyItemChanged(position)
    }

    fun add(item: Alarm) {
        listData.add(item)
        notifyDataSetChanged()
    }
}