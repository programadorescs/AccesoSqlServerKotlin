package com.pcs.accesosqlserverkotlin.ui

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pcs.accesosqlserverkotlin.R
import com.pcs.accesosqlserverkotlin.communicator.PersonaComunicador
import com.pcs.accesosqlserverkotlin.data.model.Persona
import com.pcs.accesosqlserverkotlin.data.repositories.repoPersona
import com.pcs.accesosqlserverkotlin.utils.Utilidad

class OperacionPersonaFragment : Fragment() {

    private lateinit var txtNombre: EditText
    private lateinit var txtDni: EditText
    private lateinit var txtDireccion: EditText
    private lateinit var txtTelefono: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_operacion_persona, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtNombre = view.findViewById(R.id.et_nombre)
        txtDni = view.findViewById(R.id.et_dni)
        txtDireccion = view.findViewById(R.id.et_direccion)
        txtTelefono = view.findViewById(R.id.et_telefono)
        val fab = view.findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener {
            Utilidad.OcultarTeclado(view.context, view)

            if(txtNombre.text.trim().isEmpty() ||
                txtDni.text.trim().isEmpty()) {
                Toast.makeText(context, "Faltan datos", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val entidad = Persona()

            if(PersonaComunicador.newInstance()?.getEntidad() != null)
                entidad.idpersona = PersonaComunicador.newInstance()?.getEntidad()!!.idpersona

            entidad.nombre = txtNombre.text.toString().trim()
            entidad.dni = txtDni.text.toString().trim()
            entidad.direccion = txtDireccion.text.toString().trim()
            entidad.telefono = txtTelefono.text.toString().trim()

            Grabar().execute(entidad)
        }
    }

    override fun onResume() {
        super.onResume()

        if(PersonaComunicador.newInstance()?.getEntidad() != null) {
            txtNombre.setText(PersonaComunicador.newInstance()?.getEntidad()?.nombre)
            txtDni.setText(PersonaComunicador.newInstance()?.getEntidad()?.dni)
            txtDireccion.setText(PersonaComunicador.newInstance()?.getEntidad()?.direccion)
            txtTelefono.setText(PersonaComunicador.newInstance()?.getEntidad()?.telefono)
        }
    }

    private inner class Grabar: AsyncTask<Persona, Void, Int>() {
        override fun doInBackground(vararg p0: Persona): Int {
            return repoPersona.getInstancia().Grabar(p0[0])
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)

            if(result == 1) {
                view?.let { Utilidad.LimpiarEditText(it) }
                PersonaComunicador.newInstance()?.setEntidad(null)
                Toast.makeText(context, "Datos grabados correctamente", Toast.LENGTH_LONG).show()
            } else if(result == 2) {
                Toast.makeText(context, "El registro ya esta grabado en el sistema", Toast.LENGTH_LONG).show()
            } else if(result == 3) {
                Toast.makeText(context, "No se puede grabar un valor duplicado", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Error al grabar registro", Toast.LENGTH_LONG).show()
            }
        }
    }
}