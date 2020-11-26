package com.pcs.accesosqlserverkotlin.ui

import android.os.AsyncTask
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pcs.accesosqlserverkotlin.R
import com.pcs.accesosqlserverkotlin.adapter.PersonaAdapter
import com.pcs.accesosqlserverkotlin.communicator.PersonaComunicador
import com.pcs.accesosqlserverkotlin.data.model.Persona
import com.pcs.accesosqlserverkotlin.data.repositories.repoPersona
import com.pcs.accesosqlserverkotlin.utils.Utilidad

class PersonaFragment : Fragment(), PersonaAdapter.IOnClickListener {

    private lateinit var txtBuscar: EditText
    private lateinit var rvLista: RecyclerView
    private val iOnClickListener: PersonaAdapter.IOnClickListener = this

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_persona, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        txtBuscar = view.findViewById(R.id.et_buscar)
        rvLista = view.findViewById(R.id.rv_lista)
        rvLista.layoutManager = LinearLayoutManager(context)
        rvLista.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        txtBuscar.setOnKeyListener { _, i, keyEvent ->
            if ((keyEvent.action == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                Leer().execute(txtBuscar.text.toString().trim())
                Utilidad.OcultarTeclado(view.context, view);
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        fab.setOnClickListener {
            PersonaComunicador.newInstance()?.setEntidad(null)
            Navigation.findNavController(view).navigate(R.id.action_nav_persona_to_nav_operacion_persona)
        }
    }

    override fun onResume() {
        super.onResume()
        Leer().execute("")
    }

    private inner class Leer: AsyncTask<String, Void, PersonaAdapter>(){
        override fun doInBackground(vararg p0: String): PersonaAdapter {
            return PersonaAdapter(repoPersona.getInstancia().Listar(p0[0]), iOnClickListener)
        }

        override fun onPostExecute(result: PersonaAdapter?) {
            super.onPostExecute(result)
            rvLista.adapter = result
        }
    }

    private inner class Eliminar: AsyncTask<Persona, Void, Int>() {
        override fun doInBackground(vararg p0: Persona): Int {
            return repoPersona.getInstancia().Eliminar(p0[0])
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)

            if(result == 1) {
                Leer().execute("")
                Toast.makeText(context, "Registro eliminado correctamente", Toast.LENGTH_LONG).show()
            } else if(result == 2) {
                Toast.makeText(context, "Imposible eliminar, el registro esta asociado a otra entidad", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Error al eliminar el registro", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun clickItem(entidad: Persona) {
        val items = arrayOf("Editar", "Eliminar")
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle("Opciones")
        builder?.setItems(items) { _, i ->
            if (items[i] == "Eliminar") {
                val builder1 = context?.let { AlertDialog.Builder(it) }
                builder1?.setMessage("¿Esta seguro de eliminar?")
                builder1?.setCancelable(false)
                builder1?.setPositiveButton(
                    "Aceptar"
                ) { dialogInterface, _ ->
                    Eliminar().execute(entidad)
                    dialogInterface.dismiss()
                }
                builder1?.setNegativeButton("Cancelar") { dialogInterface, _ ->
                    dialogInterface.cancel()
                }
                builder1?.create()?.show()
            } else {
                PersonaComunicador.newInstance()?.setEntidad(entidad)
                view?.let {
                    Navigation.findNavController(it)
                        .navigate(R.id.action_nav_persona_to_nav_operacion_persona)
                }
            }
        }

        builder?.create()?.show()
    }
}