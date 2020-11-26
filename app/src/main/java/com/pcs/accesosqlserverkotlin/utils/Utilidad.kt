package com.pcs.accesosqlserverkotlin.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class Utilidad {
    companion object {
        fun existeConexionInternet(contexto: Context): Boolean {
            val cm = contexto.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork?.isConnectedOrConnecting == true
        }

        fun OcultarTeclado(context: Context, view: View) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun LimpiarEditText(view: View) {
            val it: Iterator<View> = view.touchables.iterator()
            while (it.hasNext()) {
                val v = it.next()
                if (v is EditText) v.setText("")
            }
        }
    }
}