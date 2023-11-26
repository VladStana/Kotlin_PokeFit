package com.example.pokefit

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback
{

    private lateinit var mMap: GoogleMap
    lateinit var logoutButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_maps)
        logoutButton=findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener{
            val intent = Intent(this@MapsActivity, LoginActivity::class.java)
            startActivity(intent)}


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
        LoadPokemon()
    }

    var ACCESSLOCATION=123

    fun checkPermission()
    {

        if(Build.VERSION.SDK_INT>=25)
        {

            if(ActivityCompat.
                checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
            {

                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),ACCESSLOCATION)
                return
            }
        }

        GetUserLocation()
    }

    fun GetUserLocation(){
        Toast.makeText(this,"User location access on",Toast.LENGTH_LONG).show()

        var myLocation= MylocationListener()

        var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)

        var mythread=myThread()
        mythread.start()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){

            ACCESSLOCATION->{

                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    GetUserLocation()
                }else{
                    Toast.makeText(this,"Cannot access location",Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in bucuresti and move the camera

    }
    var location:Location?=null
    //Get user location

    inner class MylocationListener :LocationListener
    {
        constructor(){
            location= Location("Start")
            location!!.longitude=0.0
            location!!.longitude=0.0
        }
        override fun onLocationChanged(p0: Location)
        {
            location=p0
        }

    }

    var oldLocation:Location?=null
    inner class myThread : Thread
    {
        constructor():super(){
            oldLocation= Location("Start")
            oldLocation!!.longitude=0.0
            oldLocation!!.longitude=0.0
        }
        override fun run()
        {
            while (true) {
                try {

                    if(oldLocation!!.distanceTo(location!!)==0f){
                        continue
                    }

                    oldLocation=location
                    runOnUiThread {

                        mMap.clear()

                        //show player (eu)
                        val bucuresti = LatLng(
                            location!!.latitude,
                            location!!.longitude)

                        mMap.addMarker(
                            MarkerOptions()
                                .position(bucuresti)
                                .title("Me")
                                .snippet("My Location")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.player)))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bucuresti, 14f))
                        //show pokemons
                        for (i in 0 until listPokemons.size)
                        {
                            var newPokemon=listPokemons[i]

                            if (newPokemon.isCatch==false)
                            {
                                val pokemonLoc = LatLng(newPokemon.lat!!,newPokemon.long!!)

                                mMap.addMarker(
                                MarkerOptions()
                                    .position(pokemonLoc)
                                    .title(newPokemon.name!!)
                                    .snippet(newPokemon.des!!)
                                    .icon(BitmapDescriptorFactory.fromResource(newPokemon.image!!)))

                            }
                        }
                    }
                    Thread.sleep(1000)
                }
                catch (ex:Exception)
                {

                }
            }
        }
    }
    var listPokemons=ArrayList<Pokemon>()
    fun LoadPokemon(){
        listPokemons.add(Pokemon(R.drawable.aquachu,"Aquachu",
            "Rare water type Picachu",1340.0,44.4233,26.0408))
        listPokemons.add(Pokemon(R.drawable.earthchu,"Aquachu",
            "Rare earth type Picachu",1065.0,44.4193,26.0403))
        listPokemons.add(Pokemon(R.drawable.firechu,"Aquachu",
            "Very Rare fire type Picachu",1725.0,44.4181,26.0356))
        listPokemons.add(Pokemon(R.drawable.pinksi,"Pinksi",
            "Ultra Rare Pink cat Pokemon",2250.0,44.4206,26.0328))
    }

}