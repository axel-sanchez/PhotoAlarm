package com.example.photoalarm.ui.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.photoalarm.R
import com.example.photoalarm.data.models.Alarm

class AlarmAdapter(
    val listaDatos: List<Alarm>,
    val vibrate: () -> Unit
) : RecyclerView.Adapter<AlarmAdapter.ViewHolderDatos>() {

    class ViewHolderDatos(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var isSelected = false

        private var btnDelete: Button = itemView.findViewById(R.id.btnDelete)
        private var hide: View = itemView.findViewById(R.id.hide)

        fun bind(alarm: Alarm, vibrate: () -> Unit) {

            itemView.setOnClickListener {
                if (isSelected) {
                    hide.visibility = View.GONE
                    btnDelete.visibility = View.GONE
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
                    vibrate
                    isSelected = true
                    hide.visibility = View.VISIBLE
                    btnDelete.visibility = View.VISIBLE
                    btnDelete.setOnClickListener {
                        //onDelete(alarm) por ahora solo internamente
                        hide.visibility = View.GONE
                        btnDelete.visibility = View.GONE
                    }
                } else {
                    hide.visibility = View.GONE
                    btnDelete.visibility = View.GONE
                    isSelected = false
                }

                true
            }
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