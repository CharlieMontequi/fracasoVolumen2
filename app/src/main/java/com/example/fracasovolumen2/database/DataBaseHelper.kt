package com.example.fracasovolumen2.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.fracasovolumen2.datos.Bar

class DataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "Picoteo"
        private const val DATABASE_VERSION = 1

        private const val TABLE_BARES = "Bares"

        private const val KEY_ID_BAR = "idBar"
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_DIRECCION = "direccion"
        private const val KEY_VALORACION = "valoracion"
        private const val KEY_LATITUD = "latitud"
        private const val KEY_LONGUITUD = "longitud"
        private const val KEY_WEB = "web"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val crearTablaBares = ("CREATE TABLE IF NOT EXISTS $TABLE_BARES (" +
                "$KEY_ID_BAR INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$KEY_NOMBRE TEXT," +
                "$KEY_DIRECCION TEXT," +
                "$KEY_VALORACION REAL," +
                "$KEY_LATITUD REAL," +
                "$KEY_LONGUITUD REAL," +
                "$KEY_WEB TEXT)")
        if (db != null) {
            db.execSQL(crearTablaBares)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_BARES")
            onCreate(db)
        }
    }

    //---------------------CREAR BAR NUEVO-----------------------------------------------
    fun crearBar(bar: Bar) {
        try {
            val db = this.writableDatabase
            val camposBar = ContentValues()
            camposBar.put(KEY_ID_BAR, bar.idBar)
            camposBar.put(KEY_NOMBRE, bar.nombre)
            camposBar.put(KEY_DIRECCION, bar.direccion)
            camposBar.put(KEY_VALORACION, bar.valoracion)
            camposBar.put(KEY_LATITUD, bar.latitud)
            camposBar.put(KEY_LONGUITUD, bar.longitud)
            camposBar.put(KEY_WEB, bar.web)

            db.insert(TABLE_BARES, null, camposBar)
            db.close()
        } catch (e: Exception) {
            Log.e("Error", "Error al crear el bar", e)
        }
    }

    //---------------------MODIFICAR UN BAR----------------------------------------------
    fun modificarBar(bar: Bar) {
        try {
            val db = this.writableDatabase
            val camposBar = ContentValues()
            camposBar.put(KEY_NOMBRE, bar.nombre)
            camposBar.put(KEY_DIRECCION, bar.direccion)
            camposBar.put(KEY_VALORACION, bar.valoracion)
            camposBar.put(KEY_LATITUD, bar.latitud)
            camposBar.put(KEY_LONGUITUD, bar.longitud)
            camposBar.put(KEY_WEB, bar.web)

            db.update(TABLE_BARES, camposBar, "$KEY_ID_BAR=?", arrayOf(bar.idBar.toString()))
            db.close()
        } catch (e: Exception) {
            Log.e("Error", "Error al actualizar el bar", e)
        }
    }

    //---------------------BORRAR UN BAR-------------------------------------------------
    fun borrarBar(id: Int) {
        try {
            val db = this.writableDatabase
            db.delete(TABLE_BARES, "$KEY_ID_BAR=?", arrayOf(id.toString()))
            db.close()
        } catch (e: Exception) {
            Log.e("Error", "Error al borrar el bar", e)
        }
    }

    //---------------------OBTENER TODOS LOS BARES---------------------------------------
    fun obtenerTodosLosBares(): MutableList<Bar> {
        val db = this.readableDatabase
        val sentencia = "SELECT * FROM $TABLE_BARES"
        val listado: MutableList<Bar> = mutableListOf()

        var cursor: Cursor? = null
        cursor = db.rawQuery(sentencia, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getColumnIndex(KEY_ID_BAR)
                val nombre = cursor.getColumnIndex(KEY_NOMBRE)
                val direccion = cursor.getColumnIndex(KEY_DIRECCION)
                val valoracion = cursor.getColumnIndex(KEY_VALORACION)
                val latitud = cursor.getColumnIndex(KEY_LATITUD)
                val longitud = cursor.getColumnIndex(KEY_LONGUITUD)
                val web = cursor.getColumnIndex(KEY_WEB)
                val bar = Bar(
                    idBar = cursor.getInt(id),
                    nombre = cursor.getString(nombre),
                    direccion = cursor.getString(direccion),
                    valoracion = cursor.getDouble(valoracion),
                    latitud = cursor.getDouble(latitud),
                    longitud = cursor.getDouble(longitud),
                    web = cursor.getString(web)
                )
                listado.add(bar)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return listado
    }

    //---------------------OBTENER UN BAR------------------------------------------------
    fun obtenerUnBarPorId(id: Int): Bar? {
        val db = this.readableDatabase
        val sentencia = "SELECT * FROM $TABLE_BARES WHERE $KEY_ID_BAR=?"
        var barObtenido: Bar? = null

        var cursor: Cursor? = null
        cursor = db.rawQuery(sentencia, arrayOf(id.toString()))

        if (cursor.moveToFirst()) {
            val nombre = cursor.getColumnIndex(KEY_NOMBRE)
            val direccion = cursor.getColumnIndex(KEY_DIRECCION)
            val valoracion = cursor.getColumnIndex(KEY_VALORACION)
            val latitud = cursor.getColumnIndex(KEY_LATITUD)
            val longitud = cursor.getColumnIndex(KEY_LONGUITUD)
            val web = cursor.getColumnIndex(KEY_WEB)
            barObtenido = Bar(
                idBar = id,
                nombre = cursor.getString(nombre),
                direccion = cursor.getString(direccion),
                valoracion = cursor.getDouble(valoracion),
                latitud = cursor.getDouble(latitud),
                longitud = cursor.getDouble(longitud),
                web = cursor.getString(web)
            )

        }
        cursor.close()
        db.close()
        return barObtenido
    }
    //---------------------OBTENER LATITUD------------------------------------------------
    fun obtenerLatitudDeBar(id:Int):Long?{
        val db = this.readableDatabase
        val sentencia = "SELECT $KEY_LATITUD FROM $TABLE_BARES WHERE $KEY_ID_BAR=?"
        var latitud:Long?=null
        var cursor :Cursor? = null
        cursor= db.rawQuery(sentencia, arrayOf(id.toString()))
        if(cursor.moveToFirst()){
            val latitudIdex = cursor.getColumnIndex(KEY_LATITUD)
            latitud= cursor.getLong(latitudIdex)
        }

        return latitud
    }

    //---------------------OBTENER LONGITUD-----------------------------------------------
    fun obtenerLongitudDeBar(id:Int):Long?{
        val db = this.readableDatabase
        val sentencia = "SELECT $KEY_LATITUD FROM $TABLE_BARES WHERE $KEY_ID_BAR=?"
        var longitud:Long?=null
        var cursor :Cursor? = null
        cursor= db.rawQuery(sentencia, arrayOf(id.toString()))
        if(cursor.moveToFirst()){
            val longitudIdex = cursor.getColumnIndex(KEY_LATITUD)
            longitud= cursor.getLong(longitudIdex)
        }
        return longitud
    }
    //---------------------COMPROBAR DATOS VACIOS-------------------------------------------
    fun tablaVacia(): Boolean{
        var estaVacia = false
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_BARES", null)

        var cantidadDatosVueltos = 0

        if (cursor.moveToFirst()) {
            cantidadDatosVueltos =
                cursor.getInt(0)// se recoge el valor del cursor para no perder el dato al cerrar el cursor
            if (cantidadDatosVueltos == 0) {
                estaVacia = true
            } else {
                estaVacia = false
            }
            cursor.close()
            db.close()

        } else {
            estaVacia = false
        }
        return estaVacia
    }

    //---------------------DATOS MINIMOS----------------------------------------------------
    fun datosMinimos(){
        val bares = listOf(
            Bar(null, "pepes", "c/polvora 7", null, 122.2, 534.2,"www.malditogames.es"),
            Bar(null, "lisa", "c/polvora 7", null, 122.0, 534.1,"www.malditogames.es"),
            Bar(null, "necesita", "c/polvora 7", null, 12.2, 53.4,"www.malditogames.es"),
            Bar(null, "un", "c/polvora 7", null, 12.2, 53.4,"www.malditogames.es"),
            Bar(null, "aparato", "c/polvora 7", null, 1.22, 53.4,"www.malditogames.es"),
            Bar(null, "seguro", "c/polvora 7", null, 12.2, 5.34,"www.malditogames.es"),
            Bar(null, "dental", "c/polvora 7", null, 1.22, 53.4,"www.malditogames.es"),
            Bar(null, "hola", "c/polvora 7", null, 1.22, 53.4,"www.malditogames.es"),
            Bar(null, "chato", "c/polvora 7", null, 12.2, 53.4,"www.malditogames.es"),
            Bar(null, "con la de", "c/polvora 7", null, 122.0, 53.4,"www.malditogames.es"),
            Bar(null, "hierro que tiene", "c/polvora 7", null, 122.987, 534.0,"www.malditogames.es")
        )
        bares.forEach{crearBar(it)}
    }
}