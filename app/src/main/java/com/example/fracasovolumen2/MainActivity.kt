package com.example.fracasovolumen2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fracasovolumen2.database.DataBaseHelper
import com.example.fracasovolumen2.frgaments.Fragment_detalles
import com.example.fracasovolumen2.frgaments.Fragment_listado

class MainActivity : AppCompatActivity(), Comunicador {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = DataBaseHelper(this)
        if (db.tablaVacia()) {
            db.datosMinimos()
        } else {
            Toast.makeText(this, "Datos minimos cargados", Toast.LENGTH_SHORT).show()
        }
        // la toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar_main)
        setSupportActionBar(toolbar)


        // instancia de los fragments
        if (savedInstanceState == null) {
            val fragmentListado = Fragment_listado()
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_listado, fragmentListado)
                .commit()
        }
        // Se recoger la instancia de guardado para asegurar que el fragment que muestra un dato en
        // concreto no se recarga cada vez que se abre la app o se hace algo con el anteior
        if (savedInstanceState == null) {
            // haria falta un bundle que tome los datos de la isntancia

            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_detalle, Fragment_detalles())
                .commit()
        }

    }

    // inflar el menu y cargar las opciones dadas en el mismo
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_app, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menuItem_crearBar -> {
                envioDeIds(-1)
                true
            }

            R.id.menuItem_acercaDe -> {
                Toast.makeText(this, " Carlos Montequi", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun envioDeIds(id: Int) {
        var fragmentDetalleBar =
            supportFragmentManager.findFragmentById(R.id.frame_detalle) as? Fragment_detalles
        fragmentDetalleBar?.actualizarId(id)

        val fragmentListado =supportFragmentManager.findFragmentById(R.id.frame_listado) as? Fragment_listado
        fragmentListado?.actualizarId(id)
    }
}