package com.example.fracasovolumen2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fracasovolumen2.database.DataBaseHelper

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.fracasovolumen2.databinding.ActivityMapasBinding

class Mpas_actividad : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapasBinding
    private var id =0
    private var latitud=0L// porque son long las latitudes y longitudes en la bbdd
    private var longitud=0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        id= intent.getIntExtra("ID_BAR", 0)
        obtenerCoordenadas(id)
        Toast.makeText(this, "la id recuperada es $id", Toast.LENGTH_SHORT).show()
        // Add a marker in Sydney and move the camera
        val barEnMapa = LatLng(latitud.toDouble(), longitud.toDouble())
        mMap.addMarker(MarkerOptions().position(barEnMapa).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(barEnMapa))
        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
    }

    fun obtenerCoordenadas(id:Int){
        val db = DataBaseHelper(this)
        if(id>0){
            latitud= db.obtenerLatitudDeBar(id)!!
            longitud= db.obtenerLongitudDeBar(id)!!
        }
    }
}