package com.example.fracasovolumen2.frgaments

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.fracasovolumen2.Comunicador
import com.example.fracasovolumen2.Mpas_actividad
import com.example.fracasovolumen2.R
import com.example.fracasovolumen2.database.DataBaseHelper
import com.example.fracasovolumen2.datos.Bar
import java.security.Guard

class Fragment_detalles : Fragment() {

    private lateinit var txtNombre: EditText
    private lateinit var txtDireccion: EditText
    private lateinit var txtWeb: EditText
    private lateinit var txtLatitud: EditText
    private lateinit var txtLongitud: EditText
    private lateinit var valoracion: RatingBar
    private lateinit var imb_editar: ImageButton
    private lateinit var imb_ubicacion: ImageButton
    private lateinit var bGuardar: Button
    private lateinit var bBorrar: Button
    private var idFragment = -1

    // para que se cambien los nombres del listview
    private var comunicador: Comunicador? = null

    // mierdas para la notificacion
    private val ID_CANAL_NOTIFICACION = "canal_notificacion_1"
    private val idNotificacion = 101
    private val REQUEST_CODE_NOTIFICATIONS = 1// constante del codigo para pedir permiso de la notifiacion

    // se llama solo al destruir el frgament
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("ID_BAR_GUARDADA", idFragment)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        comunicador = context as? Comunicador
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_detalle, container, false)

        if (savedInstanceState != null) {
            idFragment = savedInstanceState.getInt("ID_BAR_GUARDADA")
        }

        return vista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = DataBaseHelper(requireContext())



        // pedir los permisos de la notificacion
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Comprueba si el permiso no ha sido concedido
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Solicita el permiso al usuario
                ActivityCompat.requestPermissions(requireContext() as Activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_NOTIFICATIONS)
            }
        }

        // elementos de la vista
        txtNombre = view.findViewById(R.id.etxt_nombreBarDetalle)
        txtDireccion = view.findViewById(R.id.etxt_direccionBarDetalle)
        txtWeb = view.findViewById(R.id.etxt_webBarDetalle)
        txtLatitud = view.findViewById(R.id.etxt_latitudBarDetalle)
        txtLongitud = view.findViewById(R.id.etxt_longitudBarDetalle)
        valoracion = view.findViewById(R.id.ratingBar_valoracionBarDetalle)
        imb_editar = view.findViewById(R.id.imb_editarBarDetalle)
        imb_ubicacion = view.findViewById(R.id.imb_ubicacionBarDetalle)
        bGuardar = view.findViewById(R.id.b_guardarBarDetalle)
        bBorrar = view.findViewById(R.id.b_borrarBarDetalle)


        // comprobar que el id ha cambiado
        if (idFragment > 0) {
            //  Toast.makeText(requireContext(), " id es $idFragment", Toast.LENGTH_SHORT).show()
            mostrarBar(idFragment!!)
            modoVisualizacion()
            actualizarId(idFragment!!)
        } else {
            modoCero()
            modoEdicion()

        }
        val verid = view.findViewById<Button>(R.id.b_verid)
        verid.setOnClickListener {
            Toast.makeText(requireContext(), " id es $idFragment", Toast.LENGTH_SHORT).show()

        }

        // funcionalidad botones
        imb_editar.setOnClickListener {
            modoEdicion()
        }

        imb_ubicacion.setOnClickListener {
            val intentMpas = Intent(requireContext(), Mpas_actividad::class.java)
            intentMpas.putExtra("ID_BAR", idFragment)
            startActivity(intentMpas)
        }
        bBorrar.setOnClickListener {

            db.borrarBar(idFragment)
            modoCero()
        }
        bGuardar.setOnClickListener {

            var bar: Bar
            if (idFragment > 0) {
                bar = Bar(
                    idFragment,
                    txtNombre.text.toString(),
                    txtDireccion.text.toString(),
                    valoracion.rating.toDouble(),
                    txtLatitud.text.toString().toDouble(),
                    txtLongitud.text.toString().toDouble(),
                    txtWeb.text.toString()
                )
                db.modificarBar(bar)
            } else {
                bar = Bar(
                    null,
                    txtNombre.text.toString(),
                    txtDireccion.text.toString(),
                    valoracion.rating.toDouble(),
                    txtLatitud.text.toString().toDouble(),
                    txtLongitud.text.toString().toDouble(),
                    txtWeb.text.toString()
                )
                db.crearBar(bar)
                //mostrarNotificacion(bar.nombre)
                //crear el canal de notificacion
                crearCanalNotificacion()

                // Obtengo el sistema de notificaciones, que lo casteo como un NotificationManager
                val administradorNotificaciones = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



                // Creo una notificación con el método Builder, pasando el contexto y aseguro que este no
                // será nulo, le paso además el ID del canal
                // Le paso los parámetros para crearla.
                val notificacion = NotificationCompat.Builder(requireContext(), ID_CANAL_NOTIFICACION)
                    .setContentTitle("Nuevo Bar Creado")
                    .setContentText("Se ha añadido un nuevo bar.")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)  // La notificación se cierra automáticamente al hacer clic en ella
                    .build()

                // Muestra la notificación
                administradorNotificaciones.notify(idNotificacion, notificacion)
            }
            modoVisualizacion()
        }

    }

    fun actualizarId(id: Int) {
        idFragment = id
        modoCero()
        if (id > 0) {
            mostrarBar(id)
            modoVisualizacion()
        }

    }

    fun mostrarBar(id: Int) {
        val db = DataBaseHelper(requireContext())
        val bar = db.obtenerUnBarPorId(id)

        // si no es nuelo carga los datos en los campos que corresponden
        if (bar != null) {
            txtNombre.setText(bar.nombre)
            txtDireccion.setText(bar.direccion)
            txtWeb.setText(bar.web)
            txtLongitud.setText(bar.longitud.toString())
            txtLatitud.setText(bar.latitud.toString())
            valoracion.rating = (bar.valoracion?.toFloat() ?: 0f)
        }
    }

    fun modoCero() {
        txtNombre.setText(null)
        txtDireccion.setText(null)
        txtWeb.setText(null)
        txtLatitud.setText(null)
        txtLongitud.setText(null)
        valoracion.rating = 0f
    }

    fun modoVisualizacion() {
        txtNombre.isEnabled = false
        txtDireccion.isEnabled = false
        txtWeb.isEnabled = false
        txtLatitud.isEnabled = false
        txtLongitud.isEnabled = false
        valoracion.isEnabled = false
        imb_editar.isEnabled = true
        imb_ubicacion.isEnabled = true
        bGuardar.isEnabled = false
        bBorrar.isEnabled = false
    }

    fun modoEdicion() {
        txtNombre.isEnabled = true
        txtDireccion.isEnabled = true
        txtWeb.isEnabled = true
        txtLatitud.isEnabled = true
        txtLongitud.isEnabled = true
        valoracion.isEnabled = true
        imb_editar.isEnabled = false
        imb_ubicacion.isEnabled = true
        bGuardar.isEnabled = true
        bBorrar.isEnabled = true
    }


    //-------------- permisos segun juan
    fun crearCanalNotificacion(){
        // pedir los permisos
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            // definir nombre y descripcion del canal
            val nombreCanalNotificaion ="Canal bares nuevos"
            val descripionCnalNotificacion ="Canal para avisar de que se ha creado un bar nuevo"
            // dar de importancia a la notifiacion para que el sistema la reconozca y la muerte por encima de otras apps
            val importanciaCanalNotificacion = NotificationManager.IMPORTANCE_HIGH

            // crear el canal de la notificacion
            val canalCreado = NotificationChannel(ID_CANAL_NOTIFICACION, nombreCanalNotificaion, importanciaCanalNotificacion).apply { description = descripionCnalNotificacion }

            // registrar el canal de notificacion
            val notificationManager :NotificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canalCreado)



        }
    }

    //--------------FUNCION DE LA NOTIFICACION---CHATGPT--------
    /*
    fun mostrarNotificacion(nombreBar: String) {
        val intent = Intent(requireContext(), Mpas_actividad::class.java)
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(requireContext(), "BAR_CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Reemplaza con tu icono
            .setContentTitle("Nuevo Bar Agregado")
            .setContentText("Se ha creado el bar: $nombreBar")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(requireContext())) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                //PEDIR PERMISOS
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                //pedir permisos para la notificacion
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            android.Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermissions(
                            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                            1
                        )
                    }
                }
                return
            }
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
    */
}
