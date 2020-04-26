package com.example.photoalarm.ui.view.adapter

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.photoalarm.R
import com.example.photoalarm.data.models.Alarm
import java.util.*


class AlarmAdapter(
    val listaDatos: List<Alarm>,
    val vibrate: () -> Unit
) : RecyclerView.Adapter<AlarmAdapter.ViewHolderDatos>() {

    class ViewHolderDatos(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var isSelected = false

        private var btnDelete: Button = itemView.findViewById(R.id.btnDelete)
        private var checkbox: Switch = itemView.findViewById(R.id.checkbox)
        private var txtTime: TextView = itemView.findViewById(R.id.txtTime)
        private var hide: View = itemView.findViewById(R.id.hide)

        //Calendario para obtener fecha & hora
        private val calendar: Calendar = Calendar.getInstance()

        //Variables para obtener la hora hora
        private val hora: Int = calendar.get(Calendar.HOUR_OF_DAY)
        private val minute: Int = calendar.get(Calendar.MINUTE)

        fun bind(alarm: Alarm, vibrate: () -> Unit) {

            itemView.setOnClickListener {
                if (isSelected) {
                    hide.visibility = View.GONE
                    btnDelete.visibility = View.GONE
                    checkbox.visibility = View.VISIBLE
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
                    hide.visibility = View.VISIBLE
                    btnDelete.visibility = View.VISIBLE
                    checkbox.visibility = View.GONE

                    btnDelete.setOnClickListener {
                        //onDelete(alarm) por ahora solo internamente
                        hide.visibility = View.GONE
                        btnDelete.visibility = View.GONE
                        checkbox.visibility = View.VISIBLE
                    }
                } else {
                    hide.visibility = View.GONE
                    btnDelete.visibility = View.GONE
                    checkbox.visibility = View.VISIBLE
                    isSelected = false
                }

                true
            }

            txtTime.setOnClickListener { selectTime() }
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
                    txtTime.text = "$horaFormateada:$minutoFormateado $periodo"
                }, //Estos valores deben ir en ese orden
                //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                //Pero el sistema devuelve la hora en formato 24 horas
                hora, minute, false
            )
            timePickerDialog.window!!.setBackgroundDrawable(itemView.resources.getDrawable(R.drawable.corners_qk))
            timePickerDialog.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDatos {
        var view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, null, false)
        return ViewHolderDatos(view)
    }

    override fun onBindViewHolder(holder: ViewHolderDatos, position: Int) {
        holder.bind(listaDatos[position], vibrate)
    }

    override fun getItemCount(): Int {
        return listaDatos.size
    }
}