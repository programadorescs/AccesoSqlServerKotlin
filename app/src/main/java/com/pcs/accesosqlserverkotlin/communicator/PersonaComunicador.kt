package com.pcs.accesosqlserverkotlin.communicator

import com.pcs.accesosqlserverkotlin.data.model.Persona

class PersonaComunicador {
    private var entidad: Persona? = null

    fun getEntidad():Persona? {
        return entidad
    }

    fun setEntidad(entidad: Persona?) {
        this.entidad = entidad
    }

    companion object {
        private var instancia: PersonaComunicador? = null

        fun newInstance(): PersonaComunicador? {
            if(instancia == null)
                instancia = PersonaComunicador()

            return instancia
        }
    }
}