package com.pcs.accesosqlserverkotlin.data.repositories

import com.pcs.accesosqlserverkotlin.data.model.Persona

class repoPersona {
    companion object {
        private var instancia: repoPersona? = null

        fun getInstancia(): repoPersona {
            if(instancia == null)
                instancia = repoPersona()

            return instancia!!
        }
    }

    fun Listar(dato: String): ArrayList<Persona> {
        var lista = ArrayList<Persona>()

        try {
            val ps = Conexion.getInstancia().getCadena().prepareStatement("SELECT idpersona, nombre, dni, direccion, telefono FROM persona WHERE nombre LIKE ?;")
            ps.setString(1, '%' + dato + '%')
            val rs = ps.executeQuery()

            while (rs.next()) {
                val aux = Persona()
                aux.idpersona = rs.getInt("idpersona")
                aux.nombre = rs.getString("nombre")
                aux.dni = rs.getString("dni")
                aux.direccion = rs.getString("direccion")
                aux.telefono = rs.getString("telefono")
                lista.add(aux);
            }

            rs.close()
            ps.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return lista
    }

    fun Grabar(pEntidad: Persona): Int {
        try {
            val sqlVerificar = "SELECT isnull(Count(idpersona), 0) FROM Persona WHERE idpersona=? OR dni=?;"
            val sqlVerificar2 = "SELECT isnull(Count(idpersona), 0) FROM Persona WHERE idpersona<>? AND dni=?;"
            val sqlInsertar ="INSERT INTO Persona (nombre, dni, direccion, telefono) VALUES (?, ?, ?, ?);"
            val sqlActualizar ="UPDATE Persona SET nombre=?, dni=?, direccion=?, telefono=? WHERE idpersona=?;"

            var ps = Conexion.getInstancia().getCadena().prepareStatement(sqlVerificar)
            ps.setInt(1, pEntidad.idpersona)
            ps.setString(2, pEntidad.dni)

            val rs = ps.executeQuery()

            if(rs.next() && rs.getInt(1) > 0) {
                rs.close()

                if(pEntidad.idpersona == 0)
                    return 2 //"El registro ya esta grabado en el sistema"

                ps = Conexion.getInstancia().getCadena().prepareStatement(sqlVerificar2)
                ps.setInt(1, pEntidad.idpersona)
                ps.setString(2, pEntidad.dni)
                val resultSet = ps.executeQuery()

                if(resultSet.next() && resultSet.getInt(1) > 0)
                    return 3 //"No se puede grabar un valor duplicado"
                resultSet.close()

                ps.clearParameters()
                ps = Conexion.getInstancia().getCadena().prepareStatement(sqlActualizar)
                ps.setString(1, pEntidad.nombre)
                ps.setString(2, pEntidad.dni)
                ps.setString(3, pEntidad.direccion)
                ps.setString(4, pEntidad.telefono)
                ps.setInt(5, pEntidad.idpersona)

                if(ps.executeUpdate() > 0) return 1 else return 0
            } else {
                ps.clearParameters()
                ps = Conexion.getInstancia().getCadena().prepareStatement(sqlInsertar)
                ps.setString(1, pEntidad.nombre)
                ps.setString(2, pEntidad.dni)
                ps.setString(3, pEntidad.direccion)
                ps.setString(4, pEntidad.telefono)

                if(ps.executeUpdate() > 0) return 1 else return 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }

    fun Eliminar(pEntidad: Persona): Int {
        try {
            val sqlEliminar ="DELETE FROM Persona WHERE idpersona=?;"
            val ps = Conexion.getInstancia().getCadena().prepareStatement(sqlEliminar)
            ps.setInt(1, pEntidad.idpersona)

            if(ps.executeUpdate() > 0)
                return 1
            else
                return 0
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }
}