package com.example.wanderlust

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    //step 3(a) the below variable are required to access user's current location
    var locationManager:LocationManager?=null
    var locationListener:LocationListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
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

        //step 3(b): code to fetch user's current location
        locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //displaying user details
        locationListener=object: LocationListener{
            override fun onLocationChanged(location: Location?) {
                //location variable here stores users current location
                if(location!=null){
                    var userLocation=LatLng(location!!.latitude,location!!.longitude)
                    //we extract user's latitude and longitude from location variable as 'userLocation'
                    mMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
                    //the above adds a marker at this location
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,17f))
                    //this line of code moves camera to user's current location by zooming in by a factor of 17f (f=float)

                }


            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onProviderDisabled(provider: String?) {

            }

        }

        //step 4(a)
        //if permission not granted
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            //ask for an array of permissions,in our case only 1 permission of fine location
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)

        }else{//step 6
            //when we already have a permission
            //same code as on requestPermission when our request has been granted
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,2f,locationListener)

            //step 7-

            val intent=intent
            val info=intent.getStringExtra("info")


            //when a user add's up a new location(i.e info=new)
            if(info.equals("new")){

                mMap.clear()//clear any existing pointer on map
                val lastLocation=locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                //fetch user's last known location so that on clicking on add place the map opens on the last location of user
                //which will be the nearest to him/her
                var lastUserLocation=LatLng(lastLocation.latitude,lastLocation.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,14f))
            }else{

            }
        }

    }

    //adding marker to a place on long click
    val myListener = object:GoogleMap.OnMapLongClickListener{
        override fun onMapLongClick(p0: LatLng?) {
           val geocoder= Geocoder(applicationContext, Locale.getDefault())
            var address=""
            try{
                //setting the onclick address to thoroughfare and sub-thoroughfare
                    val addressList=geocoder.getFromLocation(p0!!.latitude,p0!!.longitude,1)
                if(addressList!=null && addressList.size>0){

                    if(addressList[0].thoroughfare!=null){
                        address+=addressList[0].thoroughfare

                        if(addressList[0].subThoroughfare!=null){
                            address+=addressList[0].subThoroughfare
                        }
                    }

                }else{
                    address="New Place"
                }


            }catch(e:Exception){
               e.printStackTrace()
            }

        }


    }
    
    //step 5
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if(grantResults.size>0){//the grantResult variable has some value
            //and that value is permission granted
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                //when permission has been granted
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,2f,locationListener)
                //this code will request for user's location after every 2 minutes with an accuracy of 2f distance.
                //though it is better in term of accuracy if we use 0,0f but that will consume lot of user battery
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
