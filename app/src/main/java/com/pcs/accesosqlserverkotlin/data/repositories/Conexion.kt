package com.pcs.accesosqlserverkotlin.data.repositories

import net.sourceforge.jtds.jdbc.Driver
import java.sql.Connection
import java.sql.DriverManager

class Conexion {
    private var cadena: Connection? = null

    fun getCadena(): Connection {
        if(cadena == null) run {
            Driver().javaClass
            try {
                cadena = DriverManager.getConnection(
                    "jdbc:jtds:sqlserver://192.168.0.11:1433/dbandroid",
                    "jacksoft",
                    "1234"
                )
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }

        return cadena!!
    }

    companion object {
        private var instancia: Conexion? = null

        fun getInstancia(): Conexion {
            if(instancia == null)
                instancia = Conexion()

            return instancia!!
        }
    }
}