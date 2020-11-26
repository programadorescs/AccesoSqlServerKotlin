package com.pcs.accesosqlserverkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pcs.accesosqlserverkotlin.R
import com.pcs.accesosqlserverkotlin.data.model.Persona

class PersonaAdapter(private val lista: ArrayList<Persona>, private val iOnClickListener: IOnClickListener):
    RecyclerView.Adapter<PersonaAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitulo: TextView = itemView.findViewById(R.id.tv_titulo)
        val txtSubtitulo: TextView = itemView.findViewById(R.id.tv_subtitulo)
    }

    interface IOnClickListener{
        fun clickItem(entidad: Persona)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.items_persona, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val entidad = lista[position]
        holder.txtTitulo.text = entidad.nombre
        holder.txtSubtitulo.text = entidad.dni

        holder.itemView.setOnClickListener {
            iOnClickListener.clickItem(entidad)
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}