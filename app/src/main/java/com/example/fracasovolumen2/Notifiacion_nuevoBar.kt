package com.example.fracasovolumen2

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class Notifiacion_nuevoBar:BroadcastReceiver() {

    //usar mismo canal id que en lugar en el que se llama
    // mierdas para la notificacion
    private val ID_CANAL_NOTIFICACION = "canal_notificacion_1"
    private val idNotificacion = 101

    override fun onReceive(context: Context?, intent: Intent?) {
        // Obtengo el sistema de notificaciones, que lo casteo como un NotificationManager
        val administradorNotificaciones = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



        // Creo una notificación con el método Builder, pasando el contexto y aseguro que este no
        // será nulo, le paso además el ID del canal
        // Le paso los parámetros para crearla.
        val notificacion = NotificationCompat.Builder(context!!, ID_CANAL_NOTIFICACION)
            .setContentTitle("Nuevo Bar Creado")
            .setContentText("Se ha añadido un nuevo bar.")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)  // La notificación se cierra automáticamente al hacer clic en ella
            .build()

        // Muestra la notificación
        administradorNotificaciones.notify(idNotificacion, notificacion)
    }
}